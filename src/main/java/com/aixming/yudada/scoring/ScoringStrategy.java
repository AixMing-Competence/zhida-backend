package com.aixming.yudada.scoring;

import com.aixming.yudada.model.entity.App;
import com.aixming.yudada.model.entity.UserAnswer;

import java.util.List;

/**
 * @author Duzeming
 * @since 2024-08-02 14:49:12
 */
public interface ScoringStrategy {
    
    UserAnswer doScore(List<String> choices, App app) throws InterruptedException;
}
