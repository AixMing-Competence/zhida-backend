package com.aixming.yudada;

import com.aixming.yudada.common.BaseResponse;
import com.aixming.yudada.controller.QuestionController;
import com.aixming.yudada.model.dto.question.AiGenerateQuestionRequest;
import com.aixming.yudada.model.dto.question.QuestionContentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Duzeming
 * @since 2024-08-07 22:47:25
 */
@SpringBootTest
public class QuestionControllerTest {
    
    @Resource
    private QuestionController questionController;
    
    @Test
    public void aiGenerate(){
        AiGenerateQuestionRequest aiGenerateQuestionRequest = new AiGenerateQuestionRequest();
        aiGenerateQuestionRequest.setAppId(1821197856724246530L);
        aiGenerateQuestionRequest.setQuestionNumber(5);
        aiGenerateQuestionRequest.setOptionNumber(2);
        BaseResponse<List<QuestionContentDTO>> listBaseResponse = questionController.aiGenerate(aiGenerateQuestionRequest);
    }
}
