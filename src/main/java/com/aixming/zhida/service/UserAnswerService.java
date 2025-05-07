package com.aixming.zhida.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aixming.zhida.model.dto.userAnswer.UserAnswerQueryRequest;
import com.aixming.zhida.model.entity.UserAnswer;
import com.aixming.zhida.model.vo.UserAnswerVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户答题服务
 *
 * @author AixMing
 */
public interface UserAnswerService extends IService<UserAnswer> {

    /**
     * 校验数据
     *
     * @param userAnswer
     * @param add 对创建的数据进行校验
     */
    void validUserAnswer(UserAnswer userAnswer, boolean add);

    /**
     * 获取查询条件
     *
     * @param userAnswerQueryRequest
     * @return
     */
    QueryWrapper<UserAnswer> getQueryWrapper(UserAnswerQueryRequest userAnswerQueryRequest);
    
    /**
     * 获取用户答题封装
     *
     * @param userAnswer
     * @param request
     * @return
     */
    UserAnswerVO getUserAnswerVO(UserAnswer userAnswer, HttpServletRequest request);

    /**
     * 分页获取用户答题封装
     *
     * @param userAnswerPage
     * @param request
     * @return
     */
    Page<UserAnswerVO> getUserAnswerVOPage(Page<UserAnswer> userAnswerPage, HttpServletRequest request);
}
