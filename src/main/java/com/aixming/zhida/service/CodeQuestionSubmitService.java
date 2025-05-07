package com.aixming.zhida.service;


import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitAddRequest;
import com.aixming.zhida.model.dto.codequestionsubmit.CodeQuestionSubmitQueryRequest;
import com.aixming.zhida.model.entity.CodeQuestionSubmit;
import com.aixming.zhida.model.entity.User;
import com.aixming.zhida.model.vo.CodeQuestionSubmitVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 代码题目提交服务
 *
 * @author AixMing
 */
public interface CodeQuestionSubmitService extends IService<CodeQuestionSubmit> {

    /**
     * 校验数据
     *
     * @param codeQuestionSubmit
     * @param add            对创建的数据进行校验
     */
    void validCodeQuestionSubmit(CodeQuestionSubmit codeQuestionSubmit, boolean add);

    /**
     * 获取查询条件
     *
     * @param codeQuestionSubmitQueryRequest
     * @return
     */
    QueryWrapper<CodeQuestionSubmit> getQueryWrapper(CodeQuestionSubmitQueryRequest codeQuestionSubmitQueryRequest);

    /**
     * 获取题目提交封装
     *
     * @param codeQuestionSubmit
     * @param request
     * @return
     */
    CodeQuestionSubmitVO getCodeQuestionSubmitVO(CodeQuestionSubmit codeQuestionSubmit, HttpServletRequest request);

    /**
     * 分页获取题目提交封装
     *
     * @param codeQuestionSubmitPage
     * @param request
     * @return
     */
    Page<CodeQuestionSubmitVO> getCodeQuestionSubmitVOPage(Page<CodeQuestionSubmit> codeQuestionSubmitPage, HttpServletRequest request);

    /**
     * 提交题目
     *
     * @param codeQuestionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doCodeQuestionSubmit(CodeQuestionSubmitAddRequest codeQuestionSubmitAddRequest, User loginUser);
}
