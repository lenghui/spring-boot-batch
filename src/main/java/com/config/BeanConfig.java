package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.service.QueryBatchService;

@Configuration
public class BeanConfig {

	@Bean(value="batchServiceForJob1")
	public QueryBatchService batchServiceForJob1() {
		return new QueryBatchService().setJOB_NAME("myJob");
	}
	
	@Bean(value="batchServiceForJob2")
	public QueryBatchService batchServiceForJob2() {
		return new QueryBatchService().setJOB_NAME("myJob2");
	}
	
}
