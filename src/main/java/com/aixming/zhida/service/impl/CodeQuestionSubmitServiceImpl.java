package com.aixming.zhida.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.constant.CommonConstant;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.judge.codesandbox.service.JudgeService;
import com.aixming.zhida.mapper.CodeQuestionSubmitMapper;
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitAddRequest;
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitQueryRequest;
import com.aixming.zhida.model.entity.CodeQuestion;
import com.aixming.zhida.model.entity.CodeQuestionSubmit;
import com.aixming.zhida.model.entity.User;
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
    
}
