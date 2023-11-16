package com.yang.tool.modular.excel.provider;

import cn.hutool.json.JSONObject;
import com.yang.tool.api.CommonExcelApi;
import com.yang.tool.modular.excel.pojo.ExcelHeader2Option;
import com.yang.tool.modular.excel.runner.BatchImportRunner;
import com.yang.tool.modular.excel.runner.SingleImportRunner;
import com.yang.tool.modular.excel.util.Excel2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Slf4j
@Service("CommonExcelApi")
public class CommonExcelApiProvider implements CommonExcelApi {

    @Override
    public JSONObject importData(MultipartFile file, BatchImportRunner<Map<String, Object>> batchImportRunner) {
        return Excel2Util.importData(file, batchImportRunner);
    }

    @Override
    public JSONObject importData(MultipartFile file, SingleImportRunner<Map<String, Object>> singleImportRunner) {
        return Excel2Util.importData(file, singleImportRunner);
    }

    @Override
    public ResponseEntity<byte[]> downloadImportTemplate(List<ExcelHeader2Option> excelHeaderOption) {
        return Excel2Util.createImportTemplate(excelHeaderOption);
    }

    @Override
    public void batchExport(HttpServletResponse response, String fileName, List<ExcelHeader2Option> excelHeaderOption, Iterable<?> data) {
        Excel2Util.exportExcel(response, fileName, excelHeaderOption, data);
    }
}
