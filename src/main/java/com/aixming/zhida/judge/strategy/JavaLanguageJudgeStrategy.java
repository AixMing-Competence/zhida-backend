package com.aixming.zhida.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;
import com.aixming.zhida.model.entity.CodeQuestion;
import com.aixming.zhida.model.entity.JudgeCase;
import com.aixming.zhida.model.entity.JudgeConfig;
import com.aixming.zhida.model.enums.JudgeMessageEnum;

import java.util.List;

/**
 * Java 程序的判题策略
 *
 * @author AixMing
 * @since 2025-03-19 21:13:45
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        CodeQuestion codeQuestion = judgeContext.getCodeQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Long time = judgeInfo.getTime();
        Long memory = judgeInfo.getMemory();

        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        // 判断当前输出结果数量是否与输入用例的数量不同，则答案错误
        if (outputList.size() != inputList.size()) {
            judgeInfoResponse.setMessage(JudgeMessageEnum.WRONG_ANSWER.getValue());
            return judgeInfoResponse;
        }
        // 判断输出结果和预期是否相同
        for (int i = 0; i < judgeCaseList.size(); i++) {
            String output = judgeCaseList.get(i).getOutput();
            if (!output.equals(outputList.get(i))) {
                judgeInfoResponse.setMessage(JudgeMessageEnum.WRONG_ANSWER.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断当前是否有时间、内存超出
        JudgeConfig judgeConfig = JSONUtil.toBean(codeQuestion.getJudgeConfig(), JudgeConfig.class);
        // java 程序本身需要额外执行1秒钟
        long JAVA_PROGRAM_TIME = 1000L;
        if (time != null && time - JAVA_PROGRAM_TIME > judgeConfig.getTimeLimit()) {
            judgeInfoResponse.setMessage(JudgeMessageEnum.TIME_LIMIT_EXCEEDED.getValue());
            return judgeInfoResponse;
        }
        if (memory!= null && memory > judgeConfig.getMemoryLimit()) {
            judgeInfoResponse.setMessage(JudgeMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(JudgeMessageEnum.ACCEPT.getValue());
        return judgeInfoResponse;
    }
}
