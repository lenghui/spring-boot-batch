package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueryBatchService {

	@Autowired
	private JdbcTemplate jdbcTemplage;
	// 获取JobInstance表中上一次运行实例的ID
	final String GET_JOB_INSTANCE_ID = "SELECT JOB_INSTANCE_ID FROM BATCH_JOB_INSTANCE ORDER BY JOB_INSTANCE_ID DESC LIMIT 1";
	// 获取JobExecution 表中JobExecution_ID
	final String GET_JOB_EXECUTION_ID = "SELECT JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID = ("+GET_JOB_INSTANCE_ID+") ORDER BY CREATE_TIME DESC LIMIT 1"; 
	// 获取StepExecution 表中的运行状态
	final String GET_STEP_EXCUTION_STATUS = "SELECT STATUS FROM BATCH_STEP_EXECUTION WHERE JOB_EXECUTION_ID = ("+GET_JOB_EXECUTION_ID+") ORDER BY START_TIME DESC LIMIT 1";
	// 获取batch_job_execution_params表中对应JobExecution_ID的入参
	final String GET_PARAMTERSSTRING = "SELECT LONG_VAL FROM BATCH_JOB_EXECUTION_PARAMS WHERE JOB_EXECUTION_ID = ("+GET_JOB_EXECUTION_ID+") ORDER BY LONG_VAL DESC LIMIT 1";
	
	
	// 获取上一次跑批状态
	public boolean checkLastBatchStatus() {
		boolean flag = false;
		String status = jdbcTemplage.queryForObject(GET_STEP_EXCUTION_STATUS, String.class);
		if ("COMPLETED".equals(status)) {
			flag = true;
		}
		return flag;
	}
	
	// 获取上一次跑批的入参params
	public Long getParamters() {
		Long paramters = jdbcTemplage.queryForObject(GET_PARAMTERSSTRING, Long.class);
		return paramters;
	}
	
}
