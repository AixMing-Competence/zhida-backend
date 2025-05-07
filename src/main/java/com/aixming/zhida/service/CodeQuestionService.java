package com.aixming.zhida.service;

import com.aixming.zhida.model.dto.codequestion.CodeQuestionQueryRequest;
import com.aixming.zhida.model.entity.CodeQuestion;
import com.aixming.zhida.model.vo.CodeQuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 题目服务
 *
 * @author AixMing
 */
public interface CodeQuestionService extends IService<CodeQuestion> {

    /**
     * 校验数据
     *
     * @param codeQuestion
     * @param add          对创建的数据进行校验
     */
    void validCodeQuestion(CodeQuestion codeQuestion, boolean add);

    /**
     * 获取查询条件
     *
     * @param codeQuestionQueryRequest
     * @return
     */
    QueryWrapper<CodeQuestion> getQueryWrapper(CodeQuestionQueryRequest codeQuestionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    CodeQuestionVO getCodeQuestionVO(CodeQuestion question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param codeQuestionPage
     * @param request
     * @return
     */
    Page<CodeQuestionVO> getCodeQuestionVOPage(Page<CodeQuestion> codeQuestionPage, HttpServletRequest request);

}
