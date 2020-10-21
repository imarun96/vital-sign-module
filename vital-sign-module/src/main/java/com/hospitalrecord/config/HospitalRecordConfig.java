package com.hospitalrecord.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import

static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class HospitalRecordConfig {
	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Patient Record History").apiInfo(apiInfo()).select()
				.paths(regex("/vital.*")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Patient Record History Service").description("SWAGGER Implementation")
				.build();
	}
}