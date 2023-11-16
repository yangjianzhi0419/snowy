package com.yang.tool.api;

import cn.hutool.json.JSONObject;
import com.yang.tool.modular.excel.pojo.ExcelHeader2Option;
import com.yang.tool.modular.excel.runner.BatchImportRunner;
import com.yang.tool.modular.excel.runner.SingleImportRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 通用excel批量导入导出功能
 *
 * @author: yangjianzhi
 * @version1.0
 */
public interface CommonExcelApi {

    /**
     * 批量导入 (分批次插入数据) (使用easyExcel读取大量数据)
     */
    JSONObject importData(MultipartFile file, BatchImportRunner<Map<String, Object>> batchImportRunner);

    /**
     * 批量导入(单个导入)
     */
    JSONObject importData(MultipartFile file, SingleImportRunner<Map<String, Object>> singleImportRunner);

    /**
     * 下载导入模板
     */
    ResponseEntity<byte[]> downloadImportTemplate(List<ExcelHeader2Option> excelHeaderOption);

    /**
     * 批量导出
     */
    void batchExport(HttpServletResponse response, String fileName, List<ExcelHeader2Option> excelHeaderOption, Iterable<?> data);
}
