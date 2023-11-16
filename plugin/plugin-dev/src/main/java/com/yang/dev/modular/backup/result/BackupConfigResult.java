package com.yang.dev.modular.backup.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Data
public class BackupConfigResult {
    private String backupPath;
    private String backupFrequency;
    private String dbName;
    private String dbHost;
    private String dbPort;

    public String getBackupName() {
        String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return dbName + dateTimeStr + ".sql";
    }
}
