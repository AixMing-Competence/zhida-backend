package com.aixming.yudada.scoring;

import cn.hutool.json.JSONUtil;
import com.aixming.yudada.model.dto.question.QuestionContentDTO;
import com.aixming.yudada.model.entity.App;
import com.aixming.yudada.model.entity.Question;
import com.aixming.yudada.model.entity.ScoringResult;
import com.aixming.yudada.model.entity.UserAnswer;
import com.aixming.yudada.model.vo.QuestionVO;
import com.aixming.yudada.model.vo.ScoringResultVO;
import com.aixming.yudada.service.QuestionService;
import com.aixming.yudada.service.ScoringResultService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义测评类应用评分策略
 *
 * @author Duzeming
 * @since 2024-08-02 14:51:12
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {

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

        // 获取该应用的所有评分结果
        List<ScoringResult> scoringResults = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, app.getId())
        );

        // 获取每个选项的数量
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (QuestionContentDTO questionContentDTO : questionContent) {
            for (String choice : choices) {
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    if (option.getKey().equals(choice)) {
                        String result = option.getResult();
                        if (!hashMap.containsKey(result)) {
                            hashMap.put(result, 0);
                        }
                        hashMap.put(result, hashMap.get(result) + 1);
                    }
                }
            }
        }

        int maxScore = 0;
        ScoringResult maxScoringResult = scoringResults.get(0);

        // 遍历 resultProps 算出得分最高的
        for (ScoringResult scoringResult : scoringResults) {
            int score = 0;
            for (String resultProp : ScoringResultVO.objToVo(scoringResult).getResultProp()) {
                score += hashMap.getOrDefault(resultProp, 0);
            }
            if (score > maxScore) {
                maxScore = score;
                maxScoringResult = scoringResult;
            }
        }

        // 封装评分结果
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());

        return userAnswer;
    }
}
