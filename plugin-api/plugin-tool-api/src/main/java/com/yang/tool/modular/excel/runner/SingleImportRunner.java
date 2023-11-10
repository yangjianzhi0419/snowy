package com.yang.tool.modular.excel.runner;

import cn.hutool.json.JSONObject;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@FunctionalInterface
public interface SingleImportRunner<T> {

    /**
     * 执行导入动作
     *
     * @param data
     * @param i
     * @return
     */
    JSONObject action(T data, int i);
}
