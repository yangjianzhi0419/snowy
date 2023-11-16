package com.yang.gen.modular.config.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yang.common.pojo.CommonResult;
import com.yang.common.pojo.CommonValidList;
import com.yang.gen.modular.config.entity.GenConfig;
import com.yang.gen.modular.config.param.GenConfigEditParam;
import com.yang.gen.modular.config.param.GenConfigIdParam;
import com.yang.gen.modular.config.param.GenConfigListParam;
import com.yang.gen.modular.config.service.GenConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Api(tags = "代码生成详细配置控制器")
@RestController
@Validated
public class GenConfigController {

    @Resource
    private GenConfigService genConfigService;

    /**
     * 获取代码生成详细配置分页
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取代码生成详细配置分页")
    @GetMapping("/gen/config/list")
    public CommonResult<List<GenConfig>> list(GenConfigListParam genConfigListParam) {
        return CommonResult.data(genConfigService.list(genConfigListParam));
    }

    /**
     * 编辑代码生成详细配置
     *
     * @author yubaoshan
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("编辑代码生成详细配置")
    @PostMapping("/gen/config/edit")
    public CommonResult<String> edit(@RequestBody @Valid GenConfigEditParam genConfigEditParam) {
        genConfigService.edit(genConfigEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除代码生成详细配置
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("删除代码生成详细配置")
    @PostMapping("/gen/config/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                               CommonValidList<GenConfigIdParam> genConfigIdParamList) {
        genConfigService.delete(genConfigIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取代码生成详细配置详情
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("获取代码生成详细配置详情")
    @GetMapping("/gen/config/detail")
    public CommonResult<GenConfig> detail(@Valid GenConfigIdParam genConfigIdParam) {
        return CommonResult.data(genConfigService.detail(genConfigIdParam));
    }

    /**
     * 批量编辑代码生成详细配置
     *
     * @author yubaoshan
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("批量编辑代码生成详细配置")
    @PostMapping("/gen/config/editBatch")
    public CommonResult<String> editBatch(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                  CommonValidList<GenConfigEditParam> genConfigEditParamList) {
        genConfigService.editBatch(genConfigEditParamList);
        return CommonResult.ok();
    }
}