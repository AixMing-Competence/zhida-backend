package com.aixming.zhida.judge.codesandbox.impl;

import cn.hutool.core.io.FileUtil;
import com.aixming.zhida.judge.codesandbox.CodeSandbox;
import com.aixming.zhida.judge.codesandbox.model.ExecuteCodeMessage;
import com.aixming.zhida.judge.codesandbox.model.ExecuteCodeRequest;
import com.aixming.zhida.judge.codesandbox.model.ExecuteCodeResponse;
import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;
import com.aixming.zhida.model.entity.User;
import com.aixming.zhida.model.enums.JudgeMessageEnum;
import com.aixming.zhida.service.UserService;
import com.aixming.zhida.utils.ProcessUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 本地代码沙箱
 *
 * @author AixMing
 * @since 2025-03-19 19:48:31
 */
@Component
public class JavaNativeCodeSandbox implements CodeSandbox {

    @Resource
    private UserService userService;

    private static final String GLOBAL_CODE_FILENAME = ".temp";

    private static final String JAVA_FILE_NAME = "Main.java";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();

        // 1. 将代码复制到本地
        String projectPath = System.getProperty("user.dir");
        String globalCodePath = projectPath + File.separator + GLOBAL_CODE_FILENAME;
        // 代码隔离
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        User loginUser = userService.getLoginUser(request);
        String codeParentPath = globalCodePath + File.separator + loginUser.getId() + File.separator + UUID.randomUUID();
        String codeFilePath = codeParentPath + File.separator + JAVA_FILE_NAME;
        // 如果文件不存在就创建
        if (!FileUtil.exist(codeFilePath)) {
            FileUtil.newFile(codeFilePath);
        }
        // 将代码写入文件当中
        File codeFile = FileUtil.writeUtf8String(code, codeFilePath);
        // 2. 编译代码
        // javac -encoding UTF-8 Main.java
        ProcessUtil.executeCmd(new String[]{"javac", "-encoding", "UTF-8", codeFilePath}, "编译");
        // 3. 执行代码
        // java -cp codeParentPath Main
        List<ExecuteCodeMessage> executeCodeMessageList = ProcessUtil.executeInteractCmd(new String[]{"java", "-cp", codeParentPath, "Main"}, "运行", inputList);
        // 4. 收集整理输出信息
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        long maxTime = 0;
        ArrayList<String> outputList = new ArrayList<>();
        for (ExecuteCodeMessage executeCodeMessage : executeCodeMessageList) {
            String message = executeCodeMessage.getMessage();
            String errorMessage = executeCodeMessage.getErrorMessage();
            Long time = executeCodeMessage.getTime();
            if (StringUtils.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(message);
            maxTime = Math.max(maxTime, time);
        }

        if (outputList.size() == inputList.size()) {
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage(JudgeMessageEnum.ACCEPT.getValue());
            judgeInfo.setTime(maxTime);
            executeCodeResponse.setOutputList(outputList);
            executeCodeResponse.setMessage(JudgeMessageEnum.ACCEPT.getValue());
            executeCodeResponse.setJudgeInfo(judgeInfo);
            executeCodeResponse.setStatus(1);
        }

        return executeCodeResponse;
    }
}
