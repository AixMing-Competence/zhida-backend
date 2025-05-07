package com.aixming.zhida.model.vo;

import cn.hutool.json.JSONUtil;
import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;
import com.aixming.zhida.model.entity.CodeQuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交视图
 *
 * @author AixMing
 */
@Data
public class CodeQuestionSubmitVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 对象）
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

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
     * @param codeQuestionSubmitVO
     * @return
     */
    public static CodeQuestionSubmit voToObj(CodeQuestionSubmitVO codeQuestionSubmitVO) {
        if (codeQuestionSubmitVO == null) {
            return null;
        }
        CodeQuestionSubmit questionSubmit = new CodeQuestionSubmit();
        BeanUtils.copyProperties(codeQuestionSubmitVO, questionSubmit);
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(codeQuestionSubmitVO.getJudgeInfo()));
        return questionSubmit;
    }

    /**
     * 对象转封装类
     *
     * @param codeQuestionSubmit
     * @return
     */
    public static CodeQuestionSubmitVO objToVo(CodeQuestionSubmit codeQuestionSubmit) {
        if (codeQuestionSubmit == null) {
            return null;
        }
        CodeQuestionSubmitVO questionSubmitVO = new CodeQuestionSubmitVO();
        BeanUtils.copyProperties(codeQuestionSubmit, questionSubmitVO);
        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(codeQuestionSubmit.getJudgeInfo(), JudgeInfo.class));
        return questionSubmitVO;
    }

}
