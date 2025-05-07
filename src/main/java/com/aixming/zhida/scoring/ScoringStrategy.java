package com.aixming.zhida.scoring;

import com.aixming.zhida.model.entity.App;
import com.aixming.zhida.model.entity.UserAnswer;

import java.util.List;

/**
 * @author Duzeming
 * @since 2024-08-02 14:49:12
 */
public interface ScoringStrategy {
    
    UserAnswer doScore(List<String> choices, App app) throws InterruptedException;
}
