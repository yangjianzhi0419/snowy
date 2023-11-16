package com.yang.common.util;

import java.text.DecimalFormat;

/**
 * 通用文件工具
 *
 * @author: yangjianzhi
 * @version1.0
 */
public class CommonFileUtil {

    /**
     * 将文件大小转换为合适的单位
     * size: byte
     */
    public static String formatFileSize(long size) {
        if (size <= 0) {
            return "0 B";
        }

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
