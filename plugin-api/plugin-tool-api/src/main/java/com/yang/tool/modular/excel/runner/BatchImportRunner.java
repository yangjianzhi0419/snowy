package com.yang.tool.modular.excel.runner;

import java.util.List;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@FunctionalInterface
public interface BatchImportRunner<T> {

    /**
     * 执行批量导入
     *
     * @param data
     * @return
     */
    int action(List<T> data);
}
