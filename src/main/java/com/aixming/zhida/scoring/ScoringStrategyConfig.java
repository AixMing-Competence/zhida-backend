package com.aixming.zhida.scoring;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Duzeming
 * @since 2024-08-03 18:27:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ScoringStrategyConfig {

    int appType();

    int scoringStrategy();
}
