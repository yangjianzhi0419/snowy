package com.yang.toolexcel.provider;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yang.toolexcel.runner.BatchImportRunner;
import com.yang.toolexcel.runner.SingleImportRunner;
import com.yang.toolexcel.util.Excel2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * bean必需有注解 ExcelFieldOption
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Slf4j
@Service
public class BeanExcelProvider {
    /**
     * 批量导入 (分批次插入数据) (使用easyExcel读取大量数据)
     */
    public static <T> JSONObject importData(MultipartFile file, Class<T> clazz, BatchImportRunner<T> batchImportRunner) {
        // 将读取出来的 Map<String, Object> 转为对象后再执行录入操作
        BatchImportRunner<Map<String, Object>> importRunner = maps -> {
            try {
                List<T> ts = Excel2Util.readDataFromExcel(maps, clazz);
                return batchImportRunner.action(ts);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return 0;
        };

        return Excel2Util.importData(file, importRunner);
    }

    /**
     * 批量导入(单个导入)
     */
    public static <T> JSONObject importData(MultipartFile file, Class<T> clazz, SingleImportRunner<T> singleImportRunner) {
        // 将读取出来的 Map<String, Object> 转为对象后再执行录入操作
        SingleImportRunner<Map<String, Object>> importRunner = (data, i) -> {
            List<Map<String, Object>> maps = new ArrayList<>();
            maps.add(data);
            try {
                List<T> ts = Excel2Util.readDataFromExcel(maps, clazz);
                if (ts.size() > 0) {
                    return singleImportRunner.action(ts.get(0), i);
                }
            } catch (IOException ignored) {
            }

            return JSONUtil.createObj().set("success", false);
        };

        return Excel2Util.importData(file, importRunner);
    }

    /**
     * 下载批量导入模板
     */

}
