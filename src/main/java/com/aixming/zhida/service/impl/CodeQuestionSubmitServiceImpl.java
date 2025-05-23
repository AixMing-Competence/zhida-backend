package com.aixming.zhida.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.constant.CommonConstant;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;
import com.aixming.zhida.judge.codesandbox.service.JudgeService;
import com.aixming.zhida.manager.AiManager;
import com.aixming.zhida.mapper.CodeQuestionSubmitMapper;
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitAddRequest;
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitQueryRequest;
import com.aixming.zhida.model.entity.*;
import com.aixming.zhida.model.enums.CodeQuestionSubmitLanguageEnum;
import com.aixming.zhida.model.enums.CodeQuestionSubmitStatusEnum;
import com.aixming.zhida.model.vo.CodeQuestionSubmitVO;
import com.aixming.zhida.service.CodeQuestionService;
import com.aixming.zhida.service.CodeQuestionSubmitService;
import com.aixming.zhida.service.UserService;
import com.aixming.zhida.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhipu.oapi.service.v4.model.Choice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 题目提交服务实现
 *
 * @author AixMing
 */
@Service
@Slf4j
public class CodeQuestionSubmitServiceImpl extends ServiceImpl<CodeQuestionSubmitMapper, CodeQuestionSubmit> implements CodeQuestionSubmitService {

    @Resource
    private UserService userService;

    @Resource
    private CodeQuestionService codeQuestionService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    @Resource
    private AiManager aiManager;

    private static final String CODE_SYSTEM_MESSAGE = "你是一个严谨的算法判题专家，我会给你如下信息：\n" +
            "```\n" +
            "算法题目，\n" +
            "算法题目描述：\n" +
            "时间和内存限制要求\n" +
            "题目的测试用例" +
            "用户提交的代码，\n" +
            "" +
            "```\n" +
            "请你根据以上信息，判断用户的代码是否正确，给我返回你的评价结果，200 个字左右，评价结果包括以下几方面内容：\n" +
            "1、代码是否正确" +
            "2、是否符合测试用例要求\n" +
            "3、时间和空间复杂度是否符合要求";

    /**
     * 校验数据
     *
     * @param codeQuestionsubmit
     * @param add                对创建的数据进行校验
     */
    @Override
    public void validCodeQuestionSubmit(CodeQuestionSubmit codeQuestionsubmit, boolean add) {
        ThrowUtils.throwIf(codeQuestionsubmit == null, ErrorCode.PARAMS_ERROR);

        // 创建数据时，参数不能为空
        // if (add) {
        //     ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        // }
        // 修改数据时，有参数则校验
        // if (StringUtils.isNotBlank(title)) {
        //     ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        // }
    }

    /**
     * 获取查询条件
     *
     * @param codeQuestionsubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<CodeQuestionSubmit> getQueryWrapper(CodeQuestionSubmitQueryRequest codeQuestionsubmitQueryRequest) {
        QueryWrapper<CodeQuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (codeQuestionsubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long id = codeQuestionsubmitQueryRequest.getId();
        String language = codeQuestionsubmitQueryRequest.getLanguage();
        Integer status = codeQuestionsubmitQueryRequest.getStatus();
        Long questionId = codeQuestionsubmitQueryRequest.getQuestionId();
        Long userId = codeQuestionsubmitQueryRequest.getUserId();
        Long notId = codeQuestionsubmitQueryRequest.getNotId();
        String sortField = codeQuestionsubmitQueryRequest.getSortField();
        String sortOrder = codeQuestionsubmitQueryRequest.getSortOrder();

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);

        // 排序规则
        queryWrapper.orderBy(
                SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                sortField
        );
        return queryWrapper;
    }

    /**
     * 获取题目提交封装
     *
     * @param codeQuestionSubmit
     * @param request
     * @return
     */
    @Override
    public CodeQuestionSubmitVO getCodeQuestionSubmitVO(CodeQuestionSubmit codeQuestionSubmit, HttpServletRequest request) {
        // 对象转封装类
        CodeQuestionSubmitVO codeQuestionSubmitVO = CodeQuestionSubmitVO.objToVo(codeQuestionSubmit);
        // 1. 关联查询用户信息
        Long userId = codeQuestionSubmit.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        codeQuestionSubmitVO.setUser(userService.getUserVO(user));
        // 2. 处理脱敏
        // 只有管理员和本人才能看到用户提交的代码
        User loginUser = userService.getLoginUser(request);
        if (!loginUser.getId().equals(userId) && !userService.isAdmin(loginUser)) {
            codeQuestionSubmitVO.setCode(null);
        }
        return codeQuestionSubmitVO;
    }

    /**
     * 分页获取题目提交封装
     *
     * @param codeQuestionsubmitPage
     * @param request
     * @return
     */
    @Override
    public Page<CodeQuestionSubmitVO> getCodeQuestionSubmitVOPage(Page<CodeQuestionSubmit> codeQuestionsubmitPage, HttpServletRequest request) {
        List<CodeQuestionSubmit> codeQuestionsubmitList = codeQuestionsubmitPage.getRecords();
        Page<CodeQuestionSubmitVO> codeQuestionsubmitVOPage = new Page<>(codeQuestionsubmitPage.getCurrent(), codeQuestionsubmitPage.getSize(), codeQuestionsubmitPage.getTotal());
        if (CollUtil.isEmpty(codeQuestionsubmitList)) {
            return codeQuestionsubmitVOPage;
        }
        // 对象列表 => 封装对象列表
        List<CodeQuestionSubmitVO> codeQuestionsubmitVOList = codeQuestionsubmitList.stream().map(codeQuestionSubmit -> {
            return CodeQuestionSubmitVO.objToVo(codeQuestionSubmit);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = codeQuestionsubmitList.stream().map(CodeQuestionSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        codeQuestionsubmitVOList.forEach(questionsubmitVO -> {
            Long userId = questionsubmitVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionsubmitVO.setUser(userService.getUserVO(user));
        });
        // endregion

        codeQuestionsubmitVOPage.setRecords(codeQuestionsubmitVOList);
        return codeQuestionsubmitVOPage;
    }

    @Override
    public long doCodeQuestionSubmit(CodeQuestionSubmitAddRequest codeQuestionSubmitAddRequest, User loginUser) {
        // 参数校验
        String language = codeQuestionSubmitAddRequest.getLanguage();
        Long questionId = codeQuestionSubmitAddRequest.getQuestionId();

        CodeQuestionSubmitLanguageEnum languageEnum = CodeQuestionSubmitLanguageEnum.getEnumByValue(language);
        ThrowUtils.throwIf(languageEnum == null, ErrorCode.PARAMS_ERROR, "编程语言错误");
        CodeQuestion codeQuestion = codeQuestionService.getById(questionId);
        ThrowUtils.throwIf(codeQuestion == null, ErrorCode.NOT_FOUND_ERROR, "不存在题目");

        CodeQuestionSubmit codeQuestionSubmit = new CodeQuestionSubmit();
        BeanUtils.copyProperties(codeQuestionSubmitAddRequest, codeQuestionSubmit);
        codeQuestionSubmit.setJudgeInfo("{}");
        codeQuestionSubmit.setStatus(CodeQuestionSubmitStatusEnum.WAITING.getValue());
        codeQuestionSubmit.setUserId(loginUser.getId());
        codeQuestionSubmit.setLanguage(language);
        codeQuestionSubmit.setCode(codeQuestionSubmitAddRequest.getCode());
        codeQuestionSubmit.setQuestionId(questionId);

        boolean result = save(codeQuestionSubmit);
        ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR, "提交失败");

        // 执行判题服务
        Long questionSubmitId = codeQuestionSubmit.getId();
        CompletableFuture.runAsync(() -> judgeService.doJudge(questionSubmitId));

        return questionSubmitId;
    }

    @Override
    public long doAiCodeQuestionSubmit(CodeQuestionSubmitAddRequest codeQuestionSubmitAddRequest, User loginUser) {
        // 参数校验
        String language = codeQuestionSubmitAddRequest.getLanguage();
        String code = codeQuestionSubmitAddRequest.getCode();
        Long questionId = codeQuestionSubmitAddRequest.getQuestionId();

        CodeQuestionSubmitLanguageEnum codeLanguageEnum = CodeQuestionSubmitLanguageEnum.getEnumByValue(language);
        ThrowUtils.throwIf(codeLanguageEnum == null, ErrorCode.NOT_FOUND_ERROR, "编程语言错误");
        CodeQuestion codeQuestion = codeQuestionService.getById(questionId);
        ThrowUtils.throwIf(codeQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        // 先保存题目提交记录
        CodeQuestionSubmit codeQuestionSubmit = new CodeQuestionSubmit();
        codeQuestionSubmit.setLanguage(language);
        codeQuestionSubmit.setCode(code);
        codeQuestionSubmit.setStatus(CodeQuestionSubmitStatusEnum.WAITING.getValue());
        codeQuestionSubmit.setQuestionId(questionId);
        codeQuestionSubmit.setUserId(loginUser.getId());
        save(codeQuestionSubmit);

        // ai 判题
        String title = codeQuestion.getTitle();
        String content = codeQuestion.getContent();
        JudgeConfig judgeConfig = JSONUtil.toBean(codeQuestion.getJudgeConfig(), JudgeConfig.class);
        List<JudgeCase> judgeCaseList = JSONUtil.toList(codeQuestion.getJudgeCase(), JudgeCase.class);
        String codeUserMessage = getCodeUserMessage(title, content, judgeConfig, judgeCaseList, code);
        String result = aiManager.doSyncStableRequest(CODE_SYSTEM_MESSAGE, codeUserMessage);
        Choice choice = JSONUtil.toBean(result, Choice.class);
        result = choice.getMessage().getContent().toString();

        CodeQuestionSubmit newCodeQuestionSubmit = new CodeQuestionSubmit();
        newCodeQuestionSubmit.setId(codeQuestionSubmit.getId());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(result);
        newCodeQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        newCodeQuestionSubmit.setStatus(CodeQuestionSubmitStatusEnum.SUCCESS.getValue());
        updateById(newCodeQuestionSubmit);

        return newCodeQuestionSubmit.getId();
    }

    /**
     * 获取算法题的用户 prompt
     *
     * @param title
     * @param content
     * @param judgeConfig
     * @param judgeCaseList
     * @param code
     * @return
     */
    private String getCodeUserMessage(String title, String content, JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList, String code) {
        StringBuilder userMessageBuilder = new StringBuilder();
        userMessageBuilder.append("算法题目：").append(title).append("\n");
        userMessageBuilder.append("算法题目描述：").append(content).append("\n");
        userMessageBuilder.append("时间复杂度（ms）：").append(judgeConfig.getTimeLimit()).append("\n");
        userMessageBuilder.append("空间复杂度（kb）：").append(judgeConfig.getMemoryLimit()).append("\n");
        for (JudgeCase judgeCase : judgeCaseList) {
            userMessageBuilder.append("题目测试用例：")
                    .append("输入：").append(judgeCase.getInput())
                    .append("输出：").append(judgeCase.getOutput())
                    .append("\n");
        }
        userMessageBuilder.append("用户提交代码如下：\n").append(code);
        return userMessageBuilder.toString();
    }

    public static void main(String[] args) {
        String result = "{\"finish_reason\":\"stop\",\"index\":0,\"message\":{\"content\":\"评价结果：\\n1. 代码正确：用户提交的代码能够正确读取两个整数输入，并输出它们的和，符合题目要求。\\n2. 符合测试用例要求：提交的代码能够处理给定的测试用例，对于输入1 2输出3，输入11 21输出33，均能正确执行。\\n3. 时间和空间复杂度符合要求：该代码的时间复杂度为O(1)，因为它只执行了固定次数的操作，不依赖于输入大小。空间复杂度也为O(1)，因为它使用了固定数量的变量。代码没有使用额外的数据结构，因此空间使用符合要求。\",\"role\":\"assistant\"},\"delta\":null}";
        Choice choice = JSONUtil.toBean(result, Choice.class);
        System.out.println(choice.getMessage().getContent().toString());
    }

}
