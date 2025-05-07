package com.aixming.zhida.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.exception.BusinessException;
import com.aixming.zhida.judge.codesandbox.CodeSandbox;
import com.aixming.zhida.judge.codesandbox.model.ExecuteCodeRequest;
import com.aixming.zhida.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 远程代码沙箱
 *
 * @author AixMing
 * @since 2025-03-19 19:48:31
 */
@Service
public class RemoteCodeSandbox implements CodeSandbox {
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 发送远程请求
        String url = "http://192.168.115.131:8121/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "request remote execute code error, message=" + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
