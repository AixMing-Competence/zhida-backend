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
 * @author Duzeming
 * @since 2024-08-03 15:19:57
 */
@Deprecated
@Service
public class ScoringStrategyContext implements ScoringStrategy {

    @Resource
    private CustomScoreScoringStrategy customScoreScoringStrategy;

    @Resource
    private CustomTestScoringStrategy customTestScoringStrategy;

    @Override
    public UserAnswer doScore(List<String> choices, App app) {
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(app.getAppType());
        ScoringStrategyEnum scoringStrategyEnum = ScoringStrategyEnum.getEnumByValue(app.getScoringStrategy());
        ThrowUtils.throwIf(appTypeEnum == null || scoringStrategyEnum == null, ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
        switch (appTypeEnum) {
            case TEST:
                switch (scoringStrategyEnum) {
                    case CUSTOM:
                        return customTestScoringStrategy.doScore(choices, app);
                    case AI:
                        break;
                }
                break;
            case SCORE:
                switch (scoringStrategyEnum) {
                    case CUSTOM:
                        return customScoreScoringStrategy.doScore(choices, app);
                    case AI:
                        break;
                }
                break;
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
    }

}
