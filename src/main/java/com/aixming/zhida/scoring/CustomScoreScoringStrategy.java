package com.aixming.zhida.scoring;

import cn.hutool.json.JSONUtil;
import com.aixming.zhida.model.dto.question.QuestionContentDTO;
import com.aixming.zhida.model.entity.App;
import com.aixming.zhida.model.entity.Question;
import com.aixming.zhida.model.entity.ScoringResult;
import com.aixming.zhida.model.entity.UserAnswer;
import com.aixming.zhida.model.vo.QuestionVO;
import com.aixming.zhida.service.QuestionService;
import com.aixming.zhida.service.ScoringResultService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义评分类应用评分策略
 *
 * @author Duzeming
 * @since 2024-08-02 14:51:12
 */
@ScoringStrategyConfig(appType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {

    @Resource
    private ScoringResultService scoringResultService;

    @Resource
    private QuestionService questionService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) {

        // 获取题目
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getAppId, app.getId())
        );
        List<QuestionContentDTO> questionContent = QuestionVO.objToVo(question).getQuestionContent();

        // 获取该应用的所有评分结果，按评分范围降序
        List<ScoringResult> scoringResults = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, app.getId())
                        .orderByDesc(ScoringResult::getResultScoreRange)
        );

        int totalScore = 0;
        // 计算用户总得分
        // 遍历题目
        for (int i = 0; i < questionContent.size(); i++) {
            String choice = choices.get(i);
            // 遍历题目选项
            for (QuestionContentDTO.Option option : questionContent.get(i).getOptions()) {
                if (choice.equals(option.getKey())) {
                    totalScore += option.getScore();
                    break;
                }
            }
        }

        ScoringResult matchedScoringResult = scoringResults.get(0);
        // 遍历评分结果
        for (ScoringResult scoringResult : scoringResults) {
            if (totalScore >= scoringResult.getResultScoreRange()) {
                matchedScoringResult = scoringResult;
                break;
            }
        }

        // 封装评分结果
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(matchedScoringResult.getId());
        userAnswer.setResultName(matchedScoringResult.getResultName());
        userAnswer.setResultDesc(matchedScoringResult.getResultDesc());
        userAnswer.setResultPicture(matchedScoringResult.getResultPicture());
        userAnswer.setResultScore(totalScore);

        return userAnswer;
    }
}
