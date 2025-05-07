package com.aixming.zhida.judge.codesandbox;

import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.exception.BusinessException;

/**
 * 代码沙箱工厂
 *
 * @author AixMing
 * @since 2025-03-19 19:41:29
 */
public class CodeSandboxFactory {

    /**
     * 创建代码沙箱实例
     *
     * @param type 沙箱类型
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        try {
            Class<? extends CodeSandbox> clazz = (Class<? extends CodeSandbox>) Class.forName(type);
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建代码沙箱实例失败");
        }
    }

}
