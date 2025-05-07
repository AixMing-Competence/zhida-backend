package com.aixming.zhida.manager;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Duzeming
 * @since 2024-08-07 14:30:15
 */
@Component
public class AiManager {

    @Resource
    private ClientV4 client;

    /**
     * 稳定的随机数
     */
    private static final float STABLE_TEMPERATURE = 0.05f;

    /**
     * 不稳定的随机数
     */
    private static final float UNSTABLE_TEMPERATURE = 0.99f;

    /**
     * 通用流式请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature) {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        messages.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        messages.add(userChatMessage);
        return doStreamRequest(messages, temperature);
    }

    /**
     * 通用流式请求
     *
     * @param messages
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4Flash)
                .stream(Boolean.TRUE)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getFlowable();
    }

    /**
     * 同步请求（答案不稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncUnstableRequest(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, false, UNSTABLE_TEMPERATURE);
    }

    /**
     * 同步请求（答案稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncStableRequest(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, false, STABLE_TEMPERATURE);
    }

    /**
     * 同步请求
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
        return doRequest(systemMessage, userMessage, false, temperature);
    }

    /**
     * 通用请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        messages.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        messages.add(userChatMessage);
        return doRequest(messages, stream, temperature);
    }

    /**
     * 通用请求
     *
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4Flash)
                .stream(stream)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getData().getChoices().get(0).toString();
    }
}
