package com.aixming.zhida.judge.codesandbox.service;

import com.aixming.zhida.model.entity.CodeQuestionSubmit;

/**
 * 判题服务
 *
 * @author AixMing
 * @since 2025-03-19 20:19:52
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    CodeQuestionSubmit doJudge(long questionSubmitId);

}
