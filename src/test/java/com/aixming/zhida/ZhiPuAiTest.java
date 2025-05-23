package com.aixming.zhida;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Duzeming
 * @since 2024-08-06 22:23:48
 */
@SpringBootTest
public class ZhiPuAiTest {

    @Test
    void aiGenerateTest() {
        ClientV4 client = new ClientV4.Builder("{apiSecretKey}").build();
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "作为一名营销专家，请为智谱开放平台创作一个吸引人的slogan");
        messages.add(chatMessage);
        String requestId = String.valueOf(System.currentTimeMillis());

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        System.out.println("model output:" + invokeModelApiResp.getMsg());
        invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent();
    }
    
    @Test
    void RxJavaDemo() throws InterruptedException {
        Flowable<Long> flowable = Flowable.interval(1, TimeUnit.SECONDS)
                .map(i -> i + 1)
                .subscribeOn(Schedulers.io());

        flowable.observeOn(Schedulers.io())
                .doOnNext(System.out::println)
                .subscribe();
        
        Thread.sleep(10000);
    }
}
