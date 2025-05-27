package com.aixming.zhida.service.impl;

import com.aixming.zhida.mapper.AppMapper;
import com.aixming.zhida.mapper.AppThumbMapper;
import com.aixming.zhida.model.entity.App;
import com.aixming.zhida.model.entity.AppThumb;
import com.aixming.zhida.service.AppThumbService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author AixMing
 * @description 针对表【app_thumb(应用点赞表)】的数据库操作Service实现
 * @createDate 2025-05-27 20:46:44
 */
@Service
public class AppThumbServiceImpl extends ServiceImpl<AppThumbMapper, AppThumb>
        implements AppThumbService {

    @Resource
    private AppMapper appMapper;

    @Override
    @Transactional
    public void doThumb(long appId, long userId) {
        AppThumb appThumb = new AppThumb();
        appThumb.setAppId(appId);
        appThumb.setUserId(userId);
        save(appThumb);
        LambdaQueryWrapper<AppThumb> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppThumb::getAppId, appId);
        long count = count(queryWrapper);
        App app = new App();
        app.setId(appId);
        app.setThumbNum((int) count);
        appMapper.updateById(app);
    }
}




