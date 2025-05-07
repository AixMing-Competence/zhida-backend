package com.aixming.zhida.controller;

import com.aixming.zhida.common.BaseResponse;
import com.aixming.zhida.common.ResultUtils;
import com.aixming.zhida.mapper.UserAnswerMapper;
import com.aixming.zhida.model.dto.statistic.AppAnswerCountDTO;
import com.aixming.zhida.model.dto.statistic.AppAnswerResultCountDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Duzeming
 * @since 2024-08-10 17:37:12
 */
@RestController
@RequestMapping("/app/statistic")
public class AppStatisticController {

    @Resource
    private UserAnswerMapper userAnswerMapper;

    /**
     * 热门应用（top 10）
     *
     * @return
     */
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAnswerCount() {
        List<AppAnswerCountDTO> appAnswerCountDTOList = userAnswerMapper.getAnswerCount();
        return ResultUtils.success(appAnswerCountDTOList);
    }

    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAnswerResultCount(Long appId) {
        List<AppAnswerResultCountDTO> appAnswerResultCountDTOList = userAnswerMapper.getAnswerResultCount(appId);
        return ResultUtils.success(appAnswerResultCountDTOList);
    }

}
