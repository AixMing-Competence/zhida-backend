package com.aixming.yudada.scoring;

import com.aixming.yudada.common.ErrorCode;
import com.aixming.yudada.exception.BusinessException;
import com.aixming.yudada.exception.ThrowUtils;
import com.aixming.yudada.model.entity.App;
import com.aixming.yudada.model.entity.UserAnswer;
import com.aixming.yudada.model.enums.AppTypeEnum;
import com.aixming.yudada.model.enums.ScoringStrategyEnum;
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
