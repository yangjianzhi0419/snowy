package com.yang.dev.modular.backup.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据库备份记录
 *
 * @author LS
 */
@Data
@TableName("dev_backup")
public class BackupRecord {
    /** id */
    @ApiModelProperty(value = "id", position = 1)
    private String id;

    /** 备份名称 */
    @ApiModelProperty(value = "备份名称", position = 2)
    private String backupName;

    /** 备份类型 */
    @ApiModelProperty(value = "备份类型", position = 2)
    private String backupType;

    /** 备份时间 */
    @ApiModelProperty(value = "备份时间", position = 2)
    private DateTime backupTime;

    /** 数据大小 */
    @ApiModelProperty(value = "数据大小", position = 2)
    private String dataSize;
}
