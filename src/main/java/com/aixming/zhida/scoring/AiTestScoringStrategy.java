package com.aixming.zhida.scoring;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.aixming.zhida.manager.AiManager;
import com.aixming.zhida.model.dto.question.QuestionContentDTO;
import com.aixming.zhida.model.dto.userAnswer.UserAnswerDTO;
import com.aixming.zhida.model.entity.App;
import com.aixming.zhida.model.entity.Question;
import com.aixming.zhida.model.entity.UserAnswer;
import com.aixming.zhida.model.vo.QuestionVO;
import com.aixming.zhida.service.QuestionService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private AiManager aiManager;

    /**
     * 本地缓存
     */
    private final Cache<String, String> answerCacheMap =
            Caffeine.newBuilder()
                    .initialCapacity(1024)
                    .expireAfterAccess(1, TimeUnit.DAYS)
                    .build();

    // /**
    //  * 分布式锁 key
    //  */
    // private static final String AI_ANSWER_LOCK = "AI_ANSWER_LOCK";

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
    public UserAnswer doScore(List<String> choices, App app) throws InterruptedException {
        // 1. 根据 id 获取应用题目
        Long appId = app.getId();
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );

        // 查缓存，如果命中则直接返回
        String cacheKey = buildCacheKey(appId, choices.toString());
        String answerJson = answerCacheMap.getIfPresent(cacheKey);
        if (StrUtil.isNotBlank(answerJson)) {
            UserAnswer userAnswer = JSONUtil.toBean(answerJson, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JSONUtil.toJsonStr(choices));
            return userAnswer;
        }

        // 定义锁
        // RLock lock = redissonClient.getLock(AI_ANSWER_LOCK + cacheKey);

        try {
            // 竞争锁，waitTime，leaseTime
            // boolean res = lock.tryLock(3, 15, TimeUnit.SECONDS);
            // if (!res) {
            //     return null;
            // }
            // 2. 调用 AI 评分
            List<QuestionContentDTO> questionContent = QuestionVO.objToVo(question).getQuestionContent();
            String userMessage = getAiUserMessage(app, questionContent, choices);
            String resultJsonStr = aiManager.doSyncStableRequest(SYSTEM_MESSAGE, userMessage);

            // 截取字符串
            int start = resultJsonStr.indexOf("{");
            int end = resultJsonStr.indexOf("}");
            String result = resultJsonStr.substring(start, end + 1);
            UserAnswer userAnswer = JSONUtil.toBean(result, UserAnswer.class);

            // 写入缓存
            answerCacheMap.put(cacheKey, JSONUtil.toJsonStr(userAnswer));

            // 3. 封装返回对象
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JSONUtil.toJsonStr(choices));

            return userAnswer;
        } finally {
            // 保证锁一定会被释放掉
            // if (lock != null && lock.isLocked()) {
            //     // 防止锁自动释放后释放掉别人的锁
            //     if (lock.isHeldByCurrentThread()) {
            //         lock.unlock();
            //     }
            // }
        }
    }

    /**
     * 获取 用户 prompt
     *
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

    /**
     * 构建缓存 key
     *
     * @param appId
     * @param choices
     * @return
     */
    private String buildCacheKey(Long appId, String choices) {
        return DigestUtil.md5Hex(appId + ":" + choices);
    }
}
