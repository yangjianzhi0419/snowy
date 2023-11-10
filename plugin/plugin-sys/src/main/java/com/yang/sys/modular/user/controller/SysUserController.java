package com.yang.sys.modular.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yang.common.pojo.CommonResult;
import com.yang.sys.modular.user.entity.SysUser;
import com.yang.sys.modular.user.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Api(tags = "用户管理器")
@RestController
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @ApiOperation("测试")
    @GetMapping("/sys/user/get")
    public CommonResult<String> page() {
        return CommonResult.success("sdfdsfs");
    }
}
