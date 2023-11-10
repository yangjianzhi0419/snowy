package com.yang.sys.api;

import cn.hutool.json.JSONObject;

/**
 * 用户Api
 *
 * @author: yangjianzhi
 * @version1.0
 */
public interface SysUserApi {

    /**
     * 根据用户id获取用户对象，没有则返回null
     */
    JSONObject getUserByIdWithoutException(String userId);
}
