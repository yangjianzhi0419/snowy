package com.yang.sys.modular.user.provider;

import cn.hutool.json.JSONObject;
import com.yang.sys.api.SysUserApi;
import org.springframework.stereotype.Service;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Service
public class SysUserApiProvider implements SysUserApi {

    @Override
    public JSONObject getUserByIdWithoutException(String userId) {
        return null;
    }
}
