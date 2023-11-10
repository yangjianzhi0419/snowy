package com.yang.dev.core.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.yang.common.pojo.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Profile({"dev", "test"})
@Configuration
public class DevConfigure {

    @Resource
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * api文档分组配置
     */
    @Bean("sysDocApi")
    public Docket sysDocApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("开发工具DEV")
                        .description("开发工具DEV")
                        .version("1.0.0")
                        .build())
                .globalResponses(HttpMethod.GET, CommonResult.responseList())
                .globalResponses(HttpMethod.POST, CommonResult.responseList())
                .groupName("开发工具DEV")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("com.yang.dev"))
                .paths(PathSelectors.any())
                .build().extensions(openApiExtensionResolver.buildExtensions("开发工具DEV"));
    }
}
