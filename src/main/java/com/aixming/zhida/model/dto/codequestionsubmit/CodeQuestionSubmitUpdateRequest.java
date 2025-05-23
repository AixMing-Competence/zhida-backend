package com.aixming.zhida.model.dto.codequestionsubmit;

import com.aixming.zhida.judge.codesandbox.model.JudgeInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新题目提交请求
 *
 * @author AixMing
 */
@Data
public class CodeQuestionSubmitUpdateRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;

}
