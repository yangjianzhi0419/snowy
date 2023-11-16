package com.yang.sys.modular.backup.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yang.common.pojo.CommonResult;
import com.yang.sys.modular.backup.entity.BackupRecord;
import com.yang.sys.modular.backup.param.BackupConfigParam;
import com.yang.sys.modular.backup.param.BackupListParam;
import com.yang.sys.modular.backup.param.BackupPageParam;
import com.yang.sys.modular.backup.service.BackupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 依赖于config模块
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Api(tags = "数据库自动备份")
@RestController
@Validated
public class BackupController {

    @Resource
    private BackupService backupService;

    /**
     * 备份配置
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("备份配置")
    @PostMapping("/dev/backup/config")
    public CommonResult<Object> config(@RequestBody BackupConfigParam backupConfigParam) {
        backupService.config(backupConfigParam);
        return CommonResult.ok();
    }

    /**
     * 获取备份分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取配置分页")
    @GetMapping("/dev/backup/page")
    public CommonResult<Page<BackupRecord>> page(BackupPageParam backupPageParam) {
        return CommonResult.data(backupService.page(backupPageParam));
    }

    /**
     * 获取备份列表
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("获取备份列表")
    @GetMapping("/dev/backup/list")
    public CommonResult<List<BackupRecord>> list(BackupListParam backupListParam) {
        return CommonResult.data(backupService.list(backupListParam));
    }

    /**
     * 恢复数据库备份
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("恢复数据库备份")
    @GetMapping("/dev/backup/restore")
    public CommonResult<Object> restore(@RequestParam("backupId") String backupId) {
        backupService.restore(backupId);
        return CommonResult.ok();
    }

    /**
     * 手动备份
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("手动备份数据库")
    @GetMapping("/dev/backup/performManual")
    public CommonResult<Object> performManual() {
        backupService.performManual();
        return CommonResult.ok();
    }
}
