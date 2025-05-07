package com.aixming.zhida.scoring;

import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.exception.BusinessException;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.model.entity.App;
import com.aixming.zhida.model.entity.UserAnswer;
import com.aixming.zhida.model.enums.AppTypeEnum;
import com.aixming.zhida.model.enums.ScoringStrategyEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评分策略全局执行器
 *
 * @author Duzeming
 * @since 2024-08-03 18:17:14
 */
@Service
public class ScoringStrategyExecutor {

    @Resource
    private List<ScoringStrategy> scoringStrategyList;

    public UserAnswer doScore(List<String> choices, App app) throws InterruptedException {
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(app.getAppType());
        ScoringStrategyEnum scoringStrategyEnum = ScoringStrategyEnum.getEnumByValue(app.getScoringStrategy());
        ThrowUtils.throwIf(appTypeEnum == null || scoringStrategyEnum == null, ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
        for (ScoringStrategy scoringStrategy : scoringStrategyList) {
            ScoringStrategyConfig annotation = scoringStrategy.getClass().getAnnotation(ScoringStrategyConfig.class);
            if (annotation.appType() == appTypeEnum.getValue() && annotation.scoringStrategy() == scoringStrategyEnum.getValue()) {
                return scoringStrategy.doScore(choices, app);
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
    }
}
