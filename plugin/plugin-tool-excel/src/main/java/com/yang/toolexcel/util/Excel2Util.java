package com.yang.toolexcel.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
import com.yang.common.enums.CommonErrorCodeEnum;
import com.yang.common.exception.CommonException;
import com.yang.toolexcel.core.annotation.ExcelFieldOption;
import com.yang.toolexcel.core.pojo.ExcelHeader2Option;
import com.yang.toolexcel.core.runner.BatchImportRunner;
import com.yang.toolexcel.core.runner.SingleImportRunner;
import com.yang.toolexcel.listener.EasyExcelDataListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Slf4j
public class Excel2Util {

    /**
     * 批量导入 (分批次插入数据) (使用easyExcel读取大量数据)
     */
    public static JSONObject importData(MultipartFile file, BatchImportRunner<Map<String, Object>> batchImportRunner) {
        try {
            JSONArray errorDetail = JSONUtil.createArray();

            EasyExcelDataListener listener = new EasyExcelDataListener();
            listener.setHandleDataFunction(maps -> BatchImportUtil.insertData(maps, batchImportRunner));
            EasyExcel.read(file.getInputStream(), listener).sheet().doRead();

            int totalCount = listener.getTotalCount();
            int successCount = listener.getSuccessCount();
            int errorCount = totalCount - successCount;

            return JSONUtil.createObj()
                    .set("totalCount", totalCount)
                    .set("successCount", successCount)
                    .set("errorCount", errorCount)
                    .set("errorDetail", errorDetail);
        } catch (Exception e) {
            log.error(">>> 用户导入失败：", e);
            throw new CommonException(CommonErrorCodeEnum.SYSTEM_ERROR, "用户导入失败");
        }
    }

    /**
     * 批量导入(单个导入)
     */
    public static JSONObject importData(MultipartFile file, SingleImportRunner<Map<String, Object>> singleImportRunner) {
        try {
            int successCount = 0;
            int errorCount = 0;
            JSONArray errorDetail = JSONUtil.createArray();

            List<Map<String, Object>> maps = ExcelUtil.getReader(file.getInputStream()).read(1, 2, 2147483647);
            for (int i = 0; i < maps.size(); i++) {
                JSONObject resultObj = singleImportRunner.action(maps.get(i), i);
                if (resultObj.getBool("success")) {
                    successCount++;
                } else {
                    errorCount++;
                    errorDetail.add(resultObj);
                }
            }

            return JSONUtil.createObj()
                    .set("totalCount", maps.size())
                    .set("successCount", successCount)
                    .set("errorCount", errorCount)
                    .set("errorDetail", errorDetail);
        } catch (Exception e) {
            log.error(">>> 用户导入失败：", e);
            throw new CommonException(CommonErrorCodeEnum.SYSTEM_ERROR, "用户导入失败");
        }
    }

    /**
     * 导出 xlsx文件
     * <p>
     * 注意 ExcelUtil.getWriter()默认创建xls格式的Excel，因此写出到客户端也需要自定义文件名为XXX.xls，
     * 否则会出现文件损坏的提示。 若想生成xlsx格式，请使用ExcelUtil.getWriter(true)创建
     */
    public static void exportExcel(HttpServletResponse response, String fileName, List<ExcelHeader2Option> headerOptions, Iterable<?> data) {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setOnlyAlias(true);
        for (ExcelHeader2Option option : headerOptions) {
            String key = option.getKey();
            String title = option.getTitle();
            writer.addHeaderAlias(key, title);
        }
        writer.write(data);

        //设置标题
        try {
            //设置content—type header
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();

            writer.flush(outputStream, true);
            writer.close();
            IoUtil.close(outputStream);
        } catch (IOException e) {
            throw new CommonException();
        }
    }

    /**
     * 获取表头配置
     */
    public static List<ExcelHeader2Option> getExcelHeaderOption(Class<?> clazz) {
        List<ExcelHeader2Option> headerOptions = new ArrayList<>();

        // 获取类的所有声明的字段
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            ExcelFieldOption annotation = field.getAnnotation(ExcelFieldOption.class);

            // 如果字段有 ExcelProperty 注解
            if (annotation != null) {
                String key = field.getName();
                String title = annotation.value();
                boolean require = annotation.require();

                // 检查 title 是否有效
                if (StrUtil.isNotBlank(title)) {
                    headerOptions.add(new ExcelHeader2Option(key, title, require));
                }
            }
        }

        return headerOptions;
    }

    /**
     * 下载导入模板
     */
    public static ResponseEntity<byte[]> createImportTemplate(List<ExcelHeader2Option> headerOptions) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("批量导入模板");

        // 创建样式
        CellStyle titleCellStyle = createTitleCellStyle(workbook);
        CellStyle redHeaderCellStyle = createHeaderCellStyle(workbook, IndexedColors.ORANGE);
        CellStyle blueHeaderCellStyle = createHeaderCellStyle(workbook, IndexedColors.SEA_GREEN);

        // 写入标题行
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(95);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("填表说明：\n" +
                "1.严禁对表格的列顺序、表头等进行修改，否则会导入失败\n" +
                "2.红色为必填字段，请按照格式填写\n" +
                "3.绿色为非必填字段，系统根据填写内容直接导入到数据库");
        titleCell.setCellStyle(titleCellStyle);

        AtomicInteger cellIndex = new AtomicInteger();
        // 写入表头行
        Row headerRow = sheet.createRow(1);
        Cell headerCell = headerRow.createCell(cellIndex.get());
        sheet.setColumnWidth(cellIndex.get(), 15 * 256);
        headerCell.setCellValue("序号");
        headerCell.setCellStyle(blueHeaderCellStyle);
        cellIndex.getAndIncrement();

        // 写入数据列头
        for (ExcelHeader2Option option : headerOptions) {
            String title = option.getTitle();
            boolean require = option.isRequire();

            Cell nextCell = headerRow.createCell(cellIndex.get());
            sheet.setColumnWidth(cellIndex.get(), 25 * 256);
            nextCell.setCellValue(title);
            nextCell.setCellStyle(require ? redHeaderCellStyle : blueHeaderCellStyle);
            cellIndex.getAndIncrement();
        }

        // 合并标题行的单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellIndex.get() - 1));

        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // 设置响应头信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "template.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    // 样式
    public static CellStyle createTitleCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        // 字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("等线");
        font.setColor(IndexedColors.RED.getIndex());
        cellStyle.setFont(font);

        // 设置垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置水平居左对齐
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        // 设置自动换行
        cellStyle.setWrapText(true);

        return cellStyle;
    }

    // 样式
    public static CellStyle createHeaderCellStyle(Workbook workbook, IndexedColors indexedColors) {
        CellStyle cellStyle = workbook.createCellStyle();
        // 字体
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");
        cellStyle.setFont(font);

        // 设置垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置水平居左对齐
        cellStyle.setAlignment(HorizontalAlignment.LEFT);

        // 设置填充颜色为红色
        // 使用预定义的颜色索引
        cellStyle.setFillForegroundColor(indexedColors.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 设置边框样式
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        // 设置边框颜色为黑色
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        return cellStyle;
    }

    /**
     * 读取数据
     */
    public static <T> List<T> mapExcelDataToList(List<Map<String, Object>> maps, Class<T> clazz) throws IOException {
        Field[] fields = clazz.getDeclaredFields();

        return maps.stream()
                .map(strObj -> {
                    T t = createInstance(clazz);
                    if (t != null) {
                        for (Field field : fields) {
                            ExcelFieldOption annotation = field.getAnnotation(ExcelFieldOption.class);
                            if (annotation != null) {
                                String title = annotation.value();
                                if (strObj.containsKey(title)) {
                                    Object o = strObj.get(title);
                                    if (o != null) {
                                        String value = o.toString();
                                        if (StrUtil.isNotBlank(value)) {
                                            field.setAccessible(true);
                                            try {
                                                // 这里可以添加更多的类型转换逻辑
                                                if (field.getType() == Integer.class) {
                                                    field.set(t, Integer.parseInt(value));
                                                } else if (field.getType() == Double.class) {
                                                    field.set(t, Double.parseDouble(value));
                                                } else {
                                                    field.set(t, value);
                                                }
                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return t;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T> T createInstance(Class<T> clazz) {
        // 获取类的无参构造函数
        Constructor<T> constructor;
        try {
            constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
