package com.aixming.yudada.scoring;

import cn.hutool.json.JSONUtil;
import com.aixming.yudada.manager.AiManager;
import com.aixming.yudada.model.dto.question.QuestionContentDTO;
import com.aixming.yudada.model.dto.userAnswer.UserAnswerDTO;
import com.aixming.yudada.model.entity.App;
import com.aixming.yudada.model.entity.Question;
import com.aixming.yudada.model.entity.UserAnswer;
import com.aixming.yudada.model.vo.QuestionVO;
import com.aixming.yudada.service.QuestionService;
import com.aixming.yudada.service.ScoringResultService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AI测评类应用评分策略
 *
 * @author Duzeming
 * @since 2024-8-7 15:54:48
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
public class AiTestScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;
    
    @Resource
    private AiManager aiManager;

    public static final String SYSTEM_MESSAGE = "你是一名严谨的判题专家，我会给你如下信息：\n" +
            "```\n" +
            "应用名称，\n" +
            "应用描述，\n" +
            "题目和用户回答：格式为 [{ title: \"题目\",answer: \"用户回答\"}]\n" +
            "```\n" +
            "请你根据以上信息，按照以下要求来对用户的回答进行评价：\n" +
            "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量描述，100 ~ 200 个字）\n" +
            "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" +
            "```\n" +
            "{resultName: \"评价名称\", resultDesc: \"评价描述\"}\n" +
            "```\n" +
            "3. 返回结果必须为 json 对象\n";

    @Override
    public UserAnswer doScore(List<String> choices, App app) {
        // 1. 根据 id 获取应用题目
        Long appId = app.getId();
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );
        // 2. 调用 AI 评分
        List<QuestionContentDTO> questionContent = QuestionVO.objToVo(question).getQuestionContent();
        String userMessage = getAiUserMessage(app, questionContent, choices);
        String resultJsonStr = aiManager.doSyncStableRequest(SYSTEM_MESSAGE, userMessage);
        
        // 截取字符串
        int start = resultJsonStr.indexOf("{");
        int end = resultJsonStr.indexOf("}");
        String result = resultJsonStr.substring(start, end + 1);

        // 3. 封装返回对象
        UserAnswer userAnswer = JSONUtil.toBean(result, UserAnswer.class);
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));

        return userAnswer;
    }

    /**
     * 获取 用户 prompt
     * @param app
     * @param questionContentList
     * @param choices
     * @return
     */
    private String getAiUserMessage(App app, List<QuestionContentDTO> questionContentList, List<String> choices) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(app.getAppName()).append("\n");
        stringBuilder.append(app.getAppDesc()).append("\n");

        ArrayList<UserAnswerDTO> userAnswerList = new ArrayList<>();
        for (int i = 0; i < questionContentList.size(); i++) {
            QuestionContentDTO questionContent = questionContentList.get(i);
            UserAnswerDTO userAnswerDTO = new UserAnswerDTO();
            for (QuestionContentDTO.Option option : questionContent.getOptions()) {
                if (option.getKey().equals(choices.get(i))) {
                    userAnswerDTO.setAnswer(option.getValue());
                    break;
                }
            }
            userAnswerDTO.setTitle(questionContent.getTitle());
            userAnswerList.add(userAnswerDTO);
        }

        stringBuilder.append(JSONUtil.toJsonStr(userAnswerList));
        return stringBuilder.toString();
    }
}
