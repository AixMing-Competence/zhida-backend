package com.aixming.zhida.controller;

import cn.hutool.json.JSONUtil;
import com.aixming.zhida.annotation.AuthCheck;
import com.aixming.zhida.common.BaseResponse;
import com.aixming.zhida.common.DeleteRequest;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.common.ResultUtils;
import com.aixming.zhida.constant.UserConstant;
import com.aixming.zhida.exception.BusinessException;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.model.dto.codequestion.CodeQuestionAddRequest;
import com.aixming.zhida.model.dto.codequestion.CodeQuestionEditRequest;
import com.aixming.zhida.model.dto.codequestion.CodeQuestionQueryRequest;
import com.aixming.zhida.model.dto.codequestion.CodeQuestionUpdateRequest;
import com.aixming.zhida.model.entity.CodeQuestion;
import com.aixming.zhida.model.entity.User;
import com.aixming.zhida.model.vo.CodeQuestionVO;
import com.aixming.zhida.service.CodeQuestionService;
import com.aixming.zhida.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 代码题目接口
 *
 * @author AixMing
 */
@RestController
@RequestMapping("/code_question")
@Slf4j
public class CodeQuestionController {

    @Resource
    private CodeQuestionService codeQuestionService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建题目
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCodeQuestion(@RequestBody CodeQuestionAddRequest questionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        CodeQuestion question = new CodeQuestion();
        BeanUtils.copyProperties(questionAddRequest, question);
        question.setTags(JSONUtil.toJsonStr(questionAddRequest.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(questionAddRequest.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(questionAddRequest.getJudgeConfig()));
        // 数据校验
        codeQuestionService.validCodeQuestion(question, true);
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = codeQuestionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newCodeQuestionId = question.getId();
        return ResultUtils.success(newCodeQuestionId);
    }

    /**
     * 删除题目
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCodeQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        CodeQuestion oldCodeQuestion = codeQuestionService.getById(id);
        ThrowUtils.throwIf(oldCodeQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldCodeQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = codeQuestionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题目（仅管理员可用）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCodeQuestion(@RequestBody CodeQuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CodeQuestion question = new CodeQuestion();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        question.setTags(JSONUtil.toJsonStr(questionUpdateRequest.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(questionUpdateRequest.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(questionUpdateRequest.getJudgeConfig()));
        // 数据校验
        codeQuestionService.validCodeQuestion(question, false);
        // 判断是否存在
        long id = questionUpdateRequest.getId();
        CodeQuestion oldCodeQuestion = codeQuestionService.getById(id);
        ThrowUtils.throwIf(oldCodeQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = codeQuestionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题目（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CodeQuestionVO> getCodeQuestionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        CodeQuestion question = codeQuestionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(codeQuestionService.getCodeQuestionVO(question, request));
    }

    /**
     * 分页获取题目列表（仅管理员可用）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<CodeQuestion>> listCodeQuestionByPage(@RequestBody CodeQuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 查询数据库
        Page<CodeQuestion> questionPage = codeQuestionService.page(new Page<>(current, size),
                codeQuestionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取题目列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CodeQuestionVO>> listCodeQuestionVOByPage(@RequestBody CodeQuestionQueryRequest questionQueryRequest,
                                                                       HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<CodeQuestion> questionPage = codeQuestionService.page(new Page<>(current, size),
                codeQuestionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(codeQuestionService.getCodeQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前登录用户创建的题目列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CodeQuestionVO>> listMyCodeQuestionVOByPage(@RequestBody CodeQuestionQueryRequest questionQueryRequest,
                                                                         HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<CodeQuestion> questionPage = codeQuestionService.page(new Page<>(current, size),
                codeQuestionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(codeQuestionService.getCodeQuestionVOPage(questionPage, request));
    }

    /**
     * 编辑题目（给用户使用）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editCodeQuestion(@RequestBody CodeQuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CodeQuestion question = new CodeQuestion();
        BeanUtils.copyProperties(questionEditRequest, question);
        question.setTags(JSONUtil.toJsonStr(questionEditRequest.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(questionEditRequest.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(questionEditRequest.getJudgeConfig()));
        // 数据校验
        codeQuestionService.validCodeQuestion(question, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = questionEditRequest.getId();
        CodeQuestion oldCodeQuestion = codeQuestionService.getById(id);
        ThrowUtils.throwIf(oldCodeQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldCodeQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = codeQuestionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion

}
