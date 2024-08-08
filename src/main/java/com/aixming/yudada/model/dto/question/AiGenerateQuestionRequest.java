package com.aixming.yudada.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Duzeming
 * @since 2024-08-07 19:50:55
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {

    private Long appId;

    private Integer questionNumber;

    private Integer optionNumber;

    private static final long serialVersionUID = 1L;
}
