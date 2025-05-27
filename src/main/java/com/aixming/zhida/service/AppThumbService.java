package com.aixming.zhida.service;

import com.aixming.zhida.model.entity.AppThumb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author AixMing
 * @description 针对表【app_thumb(应用点赞表)】的数据库操作Service
 * @createDate 2025-05-27 20:46:44
 */
public interface AppThumbService extends IService<AppThumb> {

    void doThumb(long appId, long userId);
}
