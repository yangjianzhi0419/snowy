package com.yang.common.util;

import com.yang.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 命令行指令工具
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Slf4j
public class CommonCommandUtil {

    /**
     * 还原数据库
     */
    public static void restoreDb(String host, String port, String dbName, String username, String password, String backupPath) throws IOException, InterruptedException {
        // 构造 mysql 命令
        String command = "mysql -h " + host + " -P " + port
                + " -u " + username + " -p" + password + " " + dbName + " < " + backupPath;

        // 执行还原命令
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            log.info("数据库还原成功");
        } else {
            log.error("数据库还原失败，exit code : " + exitCode);
            throw new CommonException("");
        }
    }

    /**
     * 备份数据库
     *
     * @return
     */
    public static void backupDb(String host, String port, String username, String password, String dbName, String backupPath) throws IOException, InterruptedException {
        String command = "mysqldump -h " + host + " -P " + port
                + " -u " + username + " -p" + password
                + " " + dbName + " > " + backupPath;
        // 执行备份命令
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            log.info("数据库备份完成成功");
        } else {
            log.error("数据库备份失败，exit code : " + exitCode);
            throw new CommonException("");
        }
    }
}
