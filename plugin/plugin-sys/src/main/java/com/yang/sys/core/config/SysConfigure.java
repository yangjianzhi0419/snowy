package com.yang.sys.core.config;

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
public class SysConfigure {

    @Resource
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * api文档分组配置
     */
    @Bean("sysDocApi")
    public Docket sysDocApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("系统功能SYS")
                        .description("系统功能SYS")
                        .version("1.0.0")
                        .build())
                .globalResponses(HttpMethod.GET, CommonResult.responseList())
                .globalResponses(HttpMethod.POST, CommonResult.responseList())
                .groupName("系统功能SYS")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("com.yang.sys"))
                .paths(PathSelectors.any())
                .build().extensions(openApiExtensionResolver.buildExtensions("系统功能SYS"));
    }
}
