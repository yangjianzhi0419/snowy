package com.yang.tool.modular.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表头配置
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelHeader2Option {
    private String key;
    private String title;
    private boolean require;
}
