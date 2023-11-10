package com.yang.gen.modular.basic.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Getter
@Setter
public class GenBasicPreviewResult {

    /** SQL代码结果集 */
    @ApiModelProperty(value = "SQL代码结果集", position = 1)
    private List<GenBasicCodeResult> genBasicCodeSqlResultList;

    /** 前端代码结果集 */
    @ApiModelProperty(value = "前端代码结果集", position = 2)
    private List<GenBasicCodeResult> genBasicCodeFrontendResultList;

    /** 后端代码结果集 */
    @ApiModelProperty(value = "后端代码结果集", position = 3)
    private List<GenBasicCodeResult> genBasicCodeBackendResultList;

    @Getter
    @Setter
    public static class GenBasicCodeResult {

        /** 代码文件名称 */
        @ApiModelProperty(value = "代码文件名称", position = 1)
        private String codeFileName;

        /** 代码文件带路径名称 */
        @ApiModelProperty(value = "代码文件带路径名称", position = 2)
        private String codeFileWithPathName;

        /** 代码文件内容 */
        @ApiModelProperty(value = "代码文件内容", position = 2)
        private String codeFileContent;
    }
}
