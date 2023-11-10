package com.yang.toolexcel.api;

import cn.hutool.json.JSONObject;
import com.yang.toolexcel.core.runner.BatchImportRunner;
import com.yang.toolexcel.core.runner.SingleImportRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * excel工具api
 *
 * T类必需存在ExcelFieldOption(字段配置注解)
 *
 * @author: yangjianzhi
 * @version1.0
 */
public interface ExcelApi {

    /**
     * 批量导入 (分批次插入数据) (使用easyExcel读取大量数据)
     */
    <T> JSONObject importData(MultipartFile file, Class<T> clazz, BatchImportRunner<T> batchImportRunner);

    /**
     * 批量导入(单个导入)
     */
    <T> JSONObject importData(MultipartFile file, Class<T> clazz, SingleImportRunner<T> singleImportRunner);

    /**
     * 下载导入模板
     */
    ResponseEntity<byte[]> downloadImportTemplate(Class<?> clazz);

    /**
     * 批量导出
     */
    <T> void batchExport(HttpServletResponse response, String fileName, List<T> dataList);
}
