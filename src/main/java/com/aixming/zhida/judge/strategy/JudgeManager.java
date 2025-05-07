package com.aixming.zhida.judge.strategy;

import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;
import com.aixming.zhida.model.entity.CodeQuestionSubmit;
import com.aixming.zhida.model.enums.CodeQuestionSubmitLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 *
 * @author AixMing
 * @since 2025-03-19 21:36:56
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        CodeQuestionSubmit codeQuestionSubmit = judgeContext.getCodeQuestionSubmit();
        String language = codeQuestionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (CodeQuestionSubmitLanguageEnum.JAVA.getValue().equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
    
}
