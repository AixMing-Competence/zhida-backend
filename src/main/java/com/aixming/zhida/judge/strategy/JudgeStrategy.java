package com.aixming.zhida.judge.strategy;

import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;

/**
 * 判题策略
 *
 * @author AixMing
 * @since 2025-03-19 21:07:52
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);

}
