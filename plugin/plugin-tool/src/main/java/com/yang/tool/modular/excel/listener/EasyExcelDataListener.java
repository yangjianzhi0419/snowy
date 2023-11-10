package com.yang.tool.modular.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 使用easyExcel监听器读取大量数据
 *
 * @author: yangjianzhi
 * @version1.0
 */
public class EasyExcelDataListener extends AnalysisEventListener<Map<Integer, String>> {
    private static final int BATCH_COUNT = 10000;
    private Map<Integer, String> headerOptions = null;
    private List<Map<Integer, String>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private Function<List<Map<String, Object>>, Integer> handleDataFunction;
    private int totalCount = 0;
    private int successCount = 0;

    /**
     * 每隔10000条存储数据库，然后清理list ，方便内存回收
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext analysisContext) {
        int rowIndex = analysisContext.readRowHolder().getRowIndex();
        if (rowIndex == 1) {
            headerOptions = data;
            return;
        }

        totalCount++;
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            successCount += saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 处理最后一批数据
        successCount += saveData();
        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    }

    /**
     * 保存数据
     */
    private int saveData() {
        if (cachedDataList.size() == 0) {
            return 0;
        }

        List<Map<String, Object>> maps = new ArrayList<>();
        for (Map<Integer, String> data : cachedDataList) {
            Map<String, Object> map = new HashMap<>();
            headerOptions.forEach((index, key) -> {
                String value = data.getOrDefault(index, "");
                map.put(key, value);
            });
            maps.add(map);
        }
        return handleDataFunction.apply(maps);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setHandleDataFunction(Function<List<Map<String, Object>>, Integer> handleDataFunction) {
        this.handleDataFunction = handleDataFunction;
    }
}