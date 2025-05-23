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
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitAddRequest;
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitEditRequest;
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitQueryRequest;
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitUpdateRequest;
import com.aixming.zhida.model.entity.CodeQuestionSubmit;
import com.aixming.zhida.model.entity.User;
import com.aixming.zhida.model.vo.CodeQuestionSubmitVO;
import com.aixming.zhida.service.CodeQuestionSubmitService;
import com.aixming.zhida.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 代码题目提交接口
 *
 * @author AixMing
 */
@RestController
@RequestMapping("/code_question_submit")
@Slf4j
public class CodeQuestionSubmitController {

    @Resource
    private CodeQuestionSubmitService codeQuestionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目并判题
     *
     * @param codeQuestionSubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Long> doCodeQuestionSubmit(@RequestBody CodeQuestionSubmitAddRequest codeQuestionSubmitAddRequest, HttpServletRequest request) {
        if (codeQuestionSubmitAddRequest == null || codeQuestionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 是否登录
        User loginUser = userService.getLoginUser(request);
        long codeQuestionSubmitId = codeQuestionSubmitService.doCodeQuestionSubmit(codeQuestionSubmitAddRequest, loginUser);
        return ResultUtils.success(codeQuestionSubmitId);
    }

    /**
     * 提交题目并进行 ai 判题
     *
     * @param codeQuestionSubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/ai")
    public BaseResponse<Long> doAiCodeQuestionSubmit(@RequestBody CodeQuestionSubmitAddRequest codeQuestionSubmitAddRequest, HttpServletRequest request) {
        if (codeQuestionSubmitAddRequest == null || codeQuestionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 是否登录
        User loginUser = userService.getLoginUser(request);
        long codeQuestionSubmitId = codeQuestionSubmitService.doAiCodeQuestionSubmit(codeQuestionSubmitAddRequest, loginUser);
        return ResultUtils.success(codeQuestionSubmitId);
    }

    // region 增删改查

    /**
     * 创建题目提交
     *
     * @param codeQuestionsubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCodeQuestionSubmit(@RequestBody CodeQuestionSubmitAddRequest codeQuestionsubmitAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(codeQuestionsubmitAddRequest == null, ErrorCode.PARAMS_ERROR);
        CodeQuestionSubmit codeQuestionSubmit = new CodeQuestionSubmit();
        BeanUtils.copyProperties(codeQuestionsubmitAddRequest, codeQuestionSubmit);
        // 数据校验
        codeQuestionSubmitService.validCodeQuestionSubmit(codeQuestionSubmit, true);
        User loginUser = userService.getLoginUser(request);
        codeQuestionSubmit.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = codeQuestionSubmitService.save(codeQuestionSubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newCodeQuestionSubmitId = codeQuestionSubmit.getId();
        return ResultUtils.success(newCodeQuestionSubmitId);
    }

    /**
     * 删除题目提交
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCodeQuestionSubmit(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        CodeQuestionSubmit oldCodeQuestionSubmit = codeQuestionSubmitService.getById(id);
        ThrowUtils.throwIf(oldCodeQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldCodeQuestionSubmit.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = codeQuestionSubmitService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题目提交（仅管理员可用）
     *
     * @param codeQuestionsubmitUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCodeQuestionSubmit(@RequestBody CodeQuestionSubmitUpdateRequest codeQuestionsubmitUpdateRequest) {
        if (codeQuestionsubmitUpdateRequest == null || codeQuestionsubmitUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CodeQuestionSubmit codeQuestionsubmit = new CodeQuestionSubmit();
        BeanUtils.copyProperties(codeQuestionsubmitUpdateRequest, codeQuestionsubmit);
        codeQuestionsubmit.setJudgeInfo(JSONUtil.toJsonStr(codeQuestionsubmitUpdateRequest.getJudgeInfo()));
        // 数据校验
        codeQuestionSubmitService.validCodeQuestionSubmit(codeQuestionsubmit, false);
        // 判断是否存在
        long id = codeQuestionsubmitUpdateRequest.getId();
        CodeQuestionSubmit oldCodeQuestionSubmit = codeQuestionSubmitService.getById(id);
        ThrowUtils.throwIf(oldCodeQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = codeQuestionSubmitService.updateById(codeQuestionsubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题目提交（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CodeQuestionSubmitVO> getCodeQuestionSubmitVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        CodeQuestionSubmit questionsubmit = codeQuestionSubmitService.getById(id);
        ThrowUtils.throwIf(questionsubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(codeQuestionSubmitService.getCodeQuestionSubmitVO(questionsubmit, request));
    }

    /**
     * 分页获取题目提交列表（仅管理员可用）
     *
     * @param questionCodeSubmitQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<CodeQuestionSubmit>> listCodeQuestionSubmitByPage(@RequestBody CodeQuestionSubmitQueryRequest questionCodeSubmitQueryRequest) {
        long current = questionCodeSubmitQueryRequest.getCurrent();
        long size = questionCodeSubmitQueryRequest.getPageSize();
        // 查询数据库
        Page<CodeQuestionSubmit> questionsubmitPage = codeQuestionSubmitService.page(new Page<>(current, size),
                codeQuestionSubmitService.getQueryWrapper(questionCodeSubmitQueryRequest));
        return ResultUtils.success(questionsubmitPage);
    }

    /**
     * 分页获取题目提交列表（封装类）
     *
     * @param questionsubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CodeQuestionSubmitVO>> listQuestionSubmitVOByPage(@RequestBody CodeQuestionSubmitQueryRequest questionsubmitQueryRequest,
                                                                               HttpServletRequest request) {
        long current = questionsubmitQueryRequest.getCurrent();
        long size = questionsubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<CodeQuestionSubmit> questionsubmitPage = codeQuestionSubmitService.page(new Page<>(current, size),
                codeQuestionSubmitService.getQueryWrapper(questionsubmitQueryRequest));
        // 获取封装类
        return ResultUtils.success(codeQuestionSubmitService.getCodeQuestionSubmitVOPage(questionsubmitPage, request));
    }

    /**
     * 分页获取当前登录用户创建的题目提交列表
     *
     * @param questionsubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CodeQuestionSubmitVO>> listMyQuestionSubmitVOByPage(@RequestBody CodeQuestionSubmitQueryRequest questionsubmitQueryRequest,
                                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(questionsubmitQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionsubmitQueryRequest.setUserId(loginUser.getId());
        long current = questionsubmitQueryRequest.getCurrent();
        long size = questionsubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<CodeQuestionSubmit> questionsubmitPage = codeQuestionSubmitService.page(new Page<>(current, size),
                codeQuestionSubmitService.getQueryWrapper(questionsubmitQueryRequest));
        // 获取封装类
        return ResultUtils.success(codeQuestionSubmitService.getCodeQuestionSubmitVOPage(questionsubmitPage, request));
    }

    /**
     * 编辑题目提交（给用户使用）
     *
     * @param codeQuestionSubmitEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestionSubmit(@RequestBody CodeQuestionSubmitEditRequest codeQuestionSubmitEditRequest, HttpServletRequest request) {
        if (codeQuestionSubmitEditRequest == null || codeQuestionSubmitEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CodeQuestionSubmit codeQuestionSubmit = new CodeQuestionSubmit();
        BeanUtils.copyProperties(codeQuestionSubmitEditRequest, codeQuestionSubmit);
        codeQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(codeQuestionSubmitEditRequest));
        // 数据校验
        codeQuestionSubmitService.validCodeQuestionSubmit(codeQuestionSubmit, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = codeQuestionSubmitEditRequest.getId();
        CodeQuestionSubmit oldQuestionSubmit = codeQuestionSubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestionSubmit.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = codeQuestionSubmitService.updateById(codeQuestionSubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
