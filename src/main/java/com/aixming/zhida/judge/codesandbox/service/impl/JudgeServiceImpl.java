package com.aixming.zhida.judge.codesandbox.service.impl;

import cn.hutool.json.JSONUtil;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.exception.BusinessException;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.judge.codesandbox.CodeSandbox;
import com.aixming.zhida.judge.codesandbox.CodeSandboxFactory;
import com.aixming.zhida.judge.codesandbox.model.ExecuteCodeRequest;
import com.aixming.zhida.judge.codesandbox.model.ExecuteCodeResponse;
import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;
import com.aixming.zhida.judge.codesandbox.service.JudgeService;
import com.aixming.zhida.judge.strategy.JudgeContext;
import com.aixming.zhida.judge.strategy.JudgeManager;
import com.aixming.zhida.model.entity.CodeQuestion;
import com.aixming.zhida.model.entity.CodeQuestionSubmit;
import com.aixming.zhida.model.entity.JudgeCase;
import com.aixming.zhida.model.enums.CodeQuestionSubmitStatusEnum;
import com.aixming.zhida.service.CodeQuestionService;
import com.aixming.zhida.service.CodeQuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AixMing
 * @since 2025-03-19 20:21:39
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private CodeQuestionSubmitService codeQuestionSubmitService;

    @Resource
    private CodeQuestionService codeQuestionService;

    @Value("${codesandbox.type}")
    private String codeSandboxType;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public CodeQuestionSubmit doJudge(long codeQuestionSubmitId) {
        // 获取题目提交
        CodeQuestionSubmit codeQuestionSubmit = codeQuestionSubmitService.getById(codeQuestionSubmitId);
        ThrowUtils.throwIf(codeQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        Long questionId = codeQuestionSubmit.getQuestionId();
        CodeQuestion codeQuestion = codeQuestionService.getById(questionId);
        ThrowUtils.throwIf(codeQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        // 如果不为等待状态，则抛出异常
        Integer status = codeQuestionSubmit.getStatus();
        CodeQuestionSubmitStatusEnum questionSubmitStatusEnum = CodeQuestionSubmitStatusEnum.getEnumByValue(status);
        if (!CodeQuestionSubmitStatusEnum.WAITING.equals(questionSubmitStatusEnum)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 修改状态为“判题中”，防止重复执行
        CodeQuestionSubmit codeQuestionSubmitUpdate = new CodeQuestionSubmit();
        codeQuestionSubmitUpdate.setId(codeQuestionSubmitId);
        codeQuestionSubmitUpdate.setStatus(CodeQuestionSubmitStatusEnum.JUDGING.getValue());
        boolean update = codeQuestionSubmitService.updateById(codeQuestionSubmitUpdate);
        ThrowUtils.throwIf(!update, ErrorCode.SYSTEM_ERROR, "题目提交状态更新失败");

        String language = codeQuestionSubmit.getLanguage();
        String code = codeQuestionSubmit.getCode();
        // 获取测试用例
        List<JudgeCase> judgeCaseList = JSONUtil.toList(codeQuestion.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        // 调用代码沙箱，获取执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(codeSandboxType);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .language(language)
                .code(code)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 判题
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setCodeQuestion(codeQuestion);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setCodeQuestionSubmit(codeQuestionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 更新数据库中题目提交信息
        codeQuestionSubmitUpdate = new CodeQuestionSubmit();
        codeQuestionSubmitUpdate.setId(codeQuestionSubmitId);
        codeQuestionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        codeQuestionSubmitUpdate.setStatus(CodeQuestionSubmitStatusEnum.SUCCESS.getValue());
        update = codeQuestionSubmitService.updateById(codeQuestionSubmitUpdate);
        ThrowUtils.throwIf(!update, ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        CodeQuestionSubmit questionSubmitResult = codeQuestionSubmitService.getById(codeQuestionSubmitId);
        return questionSubmitResult;
    }
}
