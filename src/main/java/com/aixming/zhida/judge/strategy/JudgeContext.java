package com.aixming.zhida.judge.strategy;

import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;
import com.aixming.zhida.model.entity.CodeQuestion;
import com.aixming.zhida.model.entity.CodeQuestionSubmit;
import com.aixming.zhida.model.entity.JudgeCase;
import lombok.Data;

import java.util.List;

/**
 * @author AixMing
 * @since 2025-03-19 21:11:21
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private CodeQuestion codeQuestion;

    private List<JudgeCase> judgeCaseList;

    private CodeQuestionSubmit codeQuestionSubmit;

}
