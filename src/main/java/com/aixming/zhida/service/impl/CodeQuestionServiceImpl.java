package com.aixming.zhida.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.constant.CommonConstant;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.mapper.CodeQuestionMapper;
import com.aixming.zhida.model.dto.codequestion.CodeQuestionQueryRequest;
import com.aixming.zhida.model.entity.CodeQuestion;
import com.aixming.zhida.model.entity.User;
import com.aixming.zhida.model.vo.CodeQuestionVO;
import com.aixming.zhida.model.vo.UserVO;
import com.aixming.zhida.service.CodeQuestionService;
import com.aixming.zhida.service.UserService;
import com.aixming.zhida.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题目服务实现
 *
 * @author AixMing
 */
@Service
@Slf4j
public class CodeQuestionServiceImpl extends ServiceImpl<CodeQuestionMapper, CodeQuestion> implements CodeQuestionService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param codeQuestion
     * @param add          对创建的数据进行校验
     */
    @Override
    public void validCodeQuestion(CodeQuestion codeQuestion, boolean add) {
        ThrowUtils.throwIf(codeQuestion == null, ErrorCode.PARAMS_ERROR);
        String title = codeQuestion.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR, "标题不能为空");
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param codeQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<CodeQuestion> getQueryWrapper(CodeQuestionQueryRequest codeQuestionQueryRequest) {
        QueryWrapper<CodeQuestion> queryWrapper = new QueryWrapper<>();
        if (codeQuestionQueryRequest == null) {
            return queryWrapper;
        }

        Long id = codeQuestionQueryRequest.getId();
        String title = codeQuestionQueryRequest.getTitle();
        String content = codeQuestionQueryRequest.getContent();
        List<String> tags = codeQuestionQueryRequest.getTags();
        String answer = codeQuestionQueryRequest.getAnswer();
        Integer submitNum = codeQuestionQueryRequest.getSubmitNum();
        Integer acceptedNum = codeQuestionQueryRequest.getAcceptedNum();
        Integer thumbNum = codeQuestionQueryRequest.getThumbNum();
        Integer favourNum = codeQuestionQueryRequest.getFavourNum();
        Long userId = codeQuestionQueryRequest.getUserId();
        Long notId = codeQuestionQueryRequest.getNotId();
        String searchText = codeQuestionQueryRequest.getSearchText();
        String sortField = codeQuestionQueryRequest.getSortField();
        String sortOrder = codeQuestionQueryRequest.getSortOrder();

        // 搜索字段
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("title", searchText).or().like("content", searchText);
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        // JSON 数组查询
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(submitNum), "submitNum", submitNum);
        queryWrapper.eq(ObjectUtils.isNotEmpty(acceptedNum), "acceptedNum", acceptedNum);
        queryWrapper.eq(ObjectUtils.isNotEmpty(thumbNum), "thumbNum", thumbNum);
        queryWrapper.eq(ObjectUtils.isNotEmpty(favourNum), "favourNum", favourNum);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题目封装
     *
     * @param codeQuestion
     * @param request
     * @return
     */
    @Override
    public CodeQuestionVO getCodeQuestionVO(CodeQuestion codeQuestion, HttpServletRequest request) {
        // 对象转封装类
        CodeQuestionVO codeQuestionVO = CodeQuestionVO.objToVo(codeQuestion);

        // region 可选
        // 1. 关联查询用户信息
        Long userId = codeQuestion.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        codeQuestionVO.setUser(userVO);
        // endregion

        return codeQuestionVO;
    }

    /**
     * 分页获取题目封装
     *
     * @param codeQuestionPage
     * @param request
     * @return
     */
    @Override
    public Page<CodeQuestionVO> getCodeQuestionVOPage(Page<CodeQuestion> codeQuestionPage, HttpServletRequest request) {
        List<CodeQuestion> codeQuestionList = codeQuestionPage.getRecords();
        Page<CodeQuestionVO> codeQuestionVOPage = new Page<>(codeQuestionPage.getCurrent(), codeQuestionPage.getSize(), codeQuestionPage.getTotal());
        if (CollUtil.isEmpty(codeQuestionList)) {
            return codeQuestionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<CodeQuestionVO> codeQuestionVOList = codeQuestionList.stream().map(codeQuestion -> {
            return CodeQuestionVO.objToVo(codeQuestion);
        }).collect(Collectors.toList());

        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = codeQuestionList.stream().map(CodeQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> questionIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> questionIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> codeQuestionIdSet = codeQuestionList.stream().map(CodeQuestion::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
        }
        // 填充信息
        codeQuestionVOList.forEach(codeQuestionVO -> {
            Long userId = codeQuestionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            codeQuestionVO.setUser(userService.getUserVO(user));
        });
        // endregion

        codeQuestionVOPage.setRecords(codeQuestionVOList);
        return codeQuestionVOPage;
    }

}
