package com.yang.tool.modular.excel.util;

import com.yang.tool.modular.excel.runner.BatchImportRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：利用并行流快速分批次插入数据
 *
 * @author: yangjianzhi
 * @version1.0
 */
public class BatchImportUtil {

    /**
     * 每个长 SQL 插入的行数，可以根据数据库性能调整
     */
    private final static int SIZE = 1000;

    /**
     * 如果需要调整并发数目，修改下面方法的第二个参数即可
     */
    static {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "4");
    }

    /**
     * 插入方法
     */
    public static <T> Integer insertData(List<T> list, BatchImportRunner<T> batchImportRunner) {
        if (list == null || list.size() < 1) {
            return 0;
        }

        List<List<T>> streamList = new ArrayList<>();

        for (int i = 0; i < list.size(); i += SIZE) {
            int j = Math.min(i + SIZE, list.size());
            List<T> subList = list.subList(i, j);
            streamList.add(subList);
        }

        // 并行流使用的并发数是CPU核心数，不能局部更改。全局更改影响较大，斟酌
        return streamList.parallelStream().mapToInt(batchImportRunner::action).sum();
    }
}
