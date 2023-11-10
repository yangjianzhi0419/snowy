package com.yang.sys.modular.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.sys.modular.user.entity.SysUser;
import com.yang.sys.modular.user.mapper.SysUserMapper;
import com.yang.sys.modular.user.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
