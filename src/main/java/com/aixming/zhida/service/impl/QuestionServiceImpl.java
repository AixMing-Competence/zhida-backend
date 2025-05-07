package com.aixming.zhida.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.constant.CommonConstant;
import com.aixming.zhida.exception.BusinessException;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.manager.AiManager;
import com.aixming.zhida.mapper.QuestionMapper;
import com.aixming.zhida.model.dto.question.AiGenerateQuestionRequest;
import com.aixming.zhida.model.dto.question.QuestionContentDTO;
import com.aixming.zhida.model.dto.question.QuestionQueryRequest;
import com.aixming.zhida.model.entity.App;
import com.aixming.zhida.model.entity.Question;
import com.aixming.zhida.model.entity.User;
import com.aixming.zhida.model.vo.QuestionVO;
import com.aixming.zhida.model.vo.UserVO;
import com.aixming.zhida.service.AppService;
import com.aixming.zhida.service.QuestionService;
import com.aixming.zhida.service.UserService;
import com.aixming.zhida.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 应用题目服务实现
 *
 * @author AixMing
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    @Resource
    private AiManager aiManager;

    private static final String SYSTEM_MESSAGE = "你是一位严谨的出题专家，我会给你如下信息：\n" +
            "```\n" +
            "应用名称，\n" +
            "【【【应用描述】】】，\n" +
            "要生成的题目数，\n" +
            "每个题目的选项数\n" +
            "```\n" +
            "\n" +
            "请你根据上述信息，按照以下要求来出题：\n" +
            "1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复\n" +
            "2. 严格按照下面的 json 格式输出题目和选项\n" +
            "```\n" +
            "[{\"options\":[{\"value\":\"选项内容\",\"key\":\"A\"},{\"value\":\"\",\"key\":\"B\"}],\"title\":\"题目标题\"}]\n" +
            "```\n" +
            "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容\n" +
            "3. 检查题目是否包含序号，若包含序号则去除序号\n" +
            "4. 返回的题目列表格式必须为 JSON 数组";

    /**
     * 校验数据
     *
     * @param question
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String questionContent = question.getQuestionContent();
        Long appId = question.getAppId();

        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(questionContent), ErrorCode.PARAMS_ERROR, "题目列表不能为空");
            ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (appId != null) {
            App app = appService.getById(appId);
            ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        }
    }

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionQueryRequest.getId();
        String questionContent = questionQueryRequest.getQuestionContent();
        Long appId = questionQueryRequest.getAppId();
        Long userId = questionQueryRequest.getUserId();
        Long notId = questionQueryRequest.getNotId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(questionContent), "questionContent", questionContent);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), "appId", appId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取应用题目封装
     *
     * @param question
     * @param request
     * @return
     */
    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        // 对象转封装类
        QuestionVO questionVO = QuestionVO.objToVo(question);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUser(userVO);
        // endregion

        return questionVO;
    }

    /**
     * 分页获取应用题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            return QuestionVO.objToVo(question);
        }).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionVOList.forEach(questionVO -> {
            Long userId = questionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUser(userService.getUserVO(user));
        });
        // endregion

        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public List<QuestionContentDTO> aiGenerateQuestion(AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        Long appId = aiGenerateQuestionRequest.getAppId();
        Integer questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        Integer optionNumber = aiGenerateQuestionRequest.getOptionNumber();

        // 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        if (questionNumber <= 0 || questionNumber > 10 || optionNumber <= 0 || optionNumber > 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成题目失败，请检查参数");
        }

        // 调用 AI 生成题目
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        String resultJsonStr = aiManager.doSyncUnstableRequest(SYSTEM_MESSAGE, userMessage);

        // 截取 json 字符串
        int start = resultJsonStr.indexOf("[");
        int end = resultJsonStr.lastIndexOf("]");
        String result = resultJsonStr.substring(start, end + 1);
        String escape = StringEscapeUtils.unescapeJson(result);

        List<QuestionContentDTO> questionContentList = JSONUtil.toList(escape, QuestionContentDTO.class);
        return questionContentList;
    }

    @Override
    public SseEmitter aiGenerateQuestionSSE(AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        Long appId = aiGenerateQuestionRequest.getAppId();
        Integer questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        Integer optionNumber = aiGenerateQuestionRequest.getOptionNumber();

        // 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        if (questionNumber <= 0 || questionNumber > 10 || optionNumber <= 0 || optionNumber > 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成题目失败，请检查参数");
        }

        SseEmitter sseEmitter = new SseEmitter(0L);
        // 计数器
        AtomicInteger atomicInteger = new AtomicInteger(0);
        // 用于拼接完整的一道题目
        StringBuilder stringBuilder = new StringBuilder();

        // 调用 AI 生成题目
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        Flowable<ModelData> modelDataFlowable = aiManager.doStreamRequest(SYSTEM_MESSAGE, userMessage, null);
        modelDataFlowable.observeOn(Schedulers.io())
                .map(modelData -> modelData.getChoices().get(0).getDelta().getContent())
                .map(message -> message.replace("\\s", ""))
                .filter(StrUtil::isNotBlank)
                .flatMap(message -> {
                    ArrayList<Character> characters = new ArrayList<>();
                    for (char c : message.toCharArray()) {
                        characters.add(c);
                    }
                    return Flowable.fromIterable(characters);
                })
                // 截取完整的一道题目
                .doOnNext(c -> {
                    if (c == '{') {
                        atomicInteger.addAndGet(1);
                    }
                    if (atomicInteger.get() > 0) {
                        stringBuilder.append(c);
                    }
                    if (c == '}') {
                        atomicInteger.addAndGet(-1);
                        if (atomicInteger.get() == 0) {
                            // 将完整题目返回给前端
                            sseEmitter.send(JSONUtil.toJsonStr(stringBuilder));
                            // 重置
                            stringBuilder.setLength(0);
                        }
                    }
                })
                .doOnError(e -> log.error("sse error", e))
                .doOnComplete(sseEmitter::complete)
                .subscribe();
        return sseEmitter;
    }

    private String getGenerateQuestionUserMessage(App app, int questionNumber, int optionNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(app.getAppName()).append("\n");
        stringBuilder.append(app.getAppDesc()).append("\n");
        stringBuilder.append(questionNumber).append("\n");
        stringBuilder.append(optionNumber).append("\n");
        return stringBuilder.toString();
    }

}
