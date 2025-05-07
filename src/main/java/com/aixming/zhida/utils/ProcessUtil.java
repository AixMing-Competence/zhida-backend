package com.aixming.zhida.utils;

import cn.hutool.core.util.StrUtil;
import com.aixming.zhida.judge.codesandbox.model.ExecuteCodeMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 进程工具类
 *
 * @author AixMing
 * @since 2025-03-26 16:25:30
 */
public class ProcessUtil {

    /**
     * 非交互命令执行
     *
     * @param command
     * @param operation
     */
    public static void executeCmd(String[] command, String operation) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            // 合并输出和错误流
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            // 打印输出信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 程序退出码
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println(operation + "成功");

            } else {
                System.out.println(operation + "失败");
            }
            reader.close();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行交互式命令
     *
     * @param command
     * @param operation
     * @param inputList
     * @return
     */
    public static List<ExecuteCodeMessage> executeInteractCmd(String[] command, String operation, List<String> inputList) {
        ArrayList<ExecuteCodeMessage> executeCodeMessageList = new ArrayList<>();
        for (String input : inputList) {
            try {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                ExecuteCodeMessage executeCodeMessage = new ExecuteCodeMessage();
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                Process process = processBuilder.start();

                // 处理输入参数
                String[] inputArgs = input.split(" ");
                // 参数之间加回车
                String inputArgsStr = StrUtil.join("\n", inputArgs) + "\n";
                OutputStream outputStream = process.getOutputStream();
                outputStream.write(inputArgsStr.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();


                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder outputMessageStringBuilder = new StringBuilder();
                // 程序退出码
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println(operation + "成功");
                    // 打印输出信息
                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputMessageStringBuilder.append(line);
                    }
                    executeCodeMessage.setMessage(outputMessageStringBuilder.toString());
                    reader.close();
                } else {
                    System.out.println(operation + "失败");
                    // 打印错误信息
                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputMessageStringBuilder.append(line);
                    }
                    executeCodeMessage.setErrorMessage(outputMessageStringBuilder.toString());
                }
                stopWatch.stop();
                executeCodeMessage.setTime(stopWatch.getTotalTimeMillis());
                executeCodeMessageList.add(executeCodeMessage);
                reader.close();
                outputStream.close();
                process.destroy();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return executeCodeMessageList;
    }

}
