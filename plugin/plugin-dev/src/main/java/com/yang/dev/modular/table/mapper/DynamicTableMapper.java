package com.yang.dev.modular.table.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author: yangjianzhi
 * @version1.0
 */
public interface DynamicTableMapper {
    /**
     * 表操作
     */
    int exist(@Param("tableName") String tableName, @Param("tableSchema") String tableSchema);

    void createTable(DynamicTableBuildParam options);

    void addColumn(DytFieldUpdateParam updateParam);

    void dropColumn(DytFieldUpdateParam updateParam);

}
