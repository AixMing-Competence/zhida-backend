package com.aixming.zhida.mapper;

import com.aixming.zhida.model.dto.statistic.AppAnswerCountDTO;
import com.aixming.zhida.model.dto.statistic.AppAnswerResultCountDTO;
import com.aixming.zhida.model.entity.UserAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Entity com.aixming.zhida.model.entity.UserAnswer
 */
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    @Select("select appId,count(userId) as answerCount from user_answer\n" +
            "group by appId order by answerCount desc limit 10;")
    List<AppAnswerCountDTO> getAnswerCount();

    @Select("select resultName,count(userId) as resultCount from user_answer\n" +
            "where appId = #{appId} group by resultName order by resultCount desc;")
    List<AppAnswerResultCountDTO> getAnswerResultCount(Long appId);
}
