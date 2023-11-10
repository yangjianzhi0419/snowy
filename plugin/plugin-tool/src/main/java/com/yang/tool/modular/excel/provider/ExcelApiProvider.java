package com.yang.tool.modular.excel.provider;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yang.common.exception.CommonException;
import com.yang.tool.api.ExcelApi;
import com.yang.tool.modular.excel.pojo.ExcelHeader2Option;
import com.yang.tool.modular.excel.runner.BatchImportRunner;
import com.yang.tool.modular.excel.runner.SingleImportRunner;
import com.yang.tool.modular.excel.util.Excel2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
@Service("excelApi")
public class ExcelApiProvider implements ExcelApi {

    /**
     * 批量导入 (分批次插入数据) (使用easyExcel读取大量数据)
     */
    @Override
    public <T> JSONObject importData(MultipartFile file, Class<T> clazz, BatchImportRunner<T> batchImportRunner) {
        // 将读取出来的 Map<String, Object> 转为对象后再执行录入操作
        BatchImportRunner<Map<String, Object>> importRunner = maps -> {
            try {
                List<T> ts = Excel2Util.mapExcelDataToList(maps, clazz);
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
    @Override
    public <T> JSONObject importData(MultipartFile file, Class<T> clazz, SingleImportRunner<T> singleImportRunner) {
        // 将读取出来的 Map<String, Object> 转为对象后再执行录入操作
        SingleImportRunner<Map<String, Object>> importRunner = (data, i) -> {
            List<Map<String, Object>> maps = new ArrayList<>();
            maps.add(data);
            try {
                List<T> ts = Excel2Util.mapExcelDataToList(maps, clazz);
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
     * 下载导入模板
     */
    @Override
    public ResponseEntity<byte[]> downloadImportTemplate(Class<?> clazz) {
        List<ExcelHeader2Option> excelHeaderOption = Excel2Util.getExcelHeaderOption(clazz);
        return Excel2Util.createImportTemplate(excelHeaderOption);
    }

    /**
     * 批量导出
     */
    @Override
    public <T> void batchExport(HttpServletResponse response, String fileName, List<T> dataList) {
        Class<?> clazz = dataList.stream()
                .findFirst()
                .map(Object::getClass)
                .orElse(null);
        if (clazz == null) {
            throw new CommonException();
        }
        List<ExcelHeader2Option> excelHeaderOption = Excel2Util.getExcelHeaderOption(clazz);
        Excel2Util.exportExcel(response, fileName, excelHeaderOption, dataList);
    }
}
