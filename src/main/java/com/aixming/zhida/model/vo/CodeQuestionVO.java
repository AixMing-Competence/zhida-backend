package com.aixming.zhida.model.vo;

import cn.hutool.json.JSONUtil;
import com.aixming.zhida.model.entity.CodeQuestion;
import com.aixming.zhida.model.entity.JudgeCase;
import com.aixming.zhida.model.entity.JudgeConfig;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图
 *
 * @author AixMing
 */
@Data
public class CodeQuestionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 判题用例（json 数组）
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param codeQuestionVO
     * @return
     */
    public static CodeQuestion voToObj(CodeQuestionVO codeQuestionVO) {
        if (codeQuestionVO == null) {
            return null;
        }
        CodeQuestion codeQuestion = new CodeQuestion();
        BeanUtils.copyProperties(codeQuestionVO, codeQuestion);
        codeQuestion.setTags(JSONUtil.toJsonStr(codeQuestionVO.getTags()));
        codeQuestion.setJudgeCase(JSONUtil.toJsonStr(codeQuestionVO.getJudgeCase()));
        codeQuestion.setJudgeConfig(JSONUtil.toJsonStr(codeQuestionVO.getJudgeConfig()));
        return codeQuestion;
    }

    /**
     * 对象转封装类
     *
     * @param codeQuestion
     * @return
     */
    public static CodeQuestionVO objToVo(CodeQuestion codeQuestion) {
        if (codeQuestion == null) {
            return null;
        }
        CodeQuestionVO codeQuestionVO = new CodeQuestionVO();
        BeanUtils.copyProperties(codeQuestion, codeQuestionVO);
        codeQuestionVO.setTags(JSONUtil.toList(codeQuestion.getTags(), String.class));
        codeQuestionVO.setJudgeCase(JSONUtil.toList(codeQuestion.getJudgeCase(), JudgeCase.class));
        codeQuestionVO.setJudgeConfig(JSONUtil.toBean(codeQuestion.getJudgeConfig(), JudgeConfig.class));
        return codeQuestionVO;
    }

}
