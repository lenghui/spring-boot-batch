package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class QueryBatchService {

	@Autowired
	private JdbcTemplate jdbcTemplage;
	
	private String JOB_NAME;
	
	public String getJOB_NAME() {
		return JOB_NAME;
	}

	public QueryBatchService setJOB_NAME(String jOB_NAME) {
		JOB_NAME = jOB_NAME;
		return this;
	}

	// 获取JobInstance表中上一次运行实例的ID
	String GET_JOB_INSTANCE_ID = "SELECT JOB_INSTANCE_ID FROM BATCH_JOB_INSTANCE WHERE JOB_NAME = ? ORDER BY JOB_INSTANCE_ID DESC LIMIT 1";
	// 获取JobExecution 表中JobExecution_ID
	final String GET_JOB_EXECUTION_ID = "SELECT JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID = ("+GET_JOB_INSTANCE_ID+") ORDER BY CREATE_TIME DESC LIMIT 1"; 
	// 获取StepExecution 表中的运行状态
	final String GET_STEP_EXCUTION_STATUS = "SELECT STATUS FROM BATCH_STEP_EXECUTION WHERE JOB_EXECUTION_ID = ("+GET_JOB_EXECUTION_ID+") ORDER BY START_TIME DESC LIMIT 1";
	// 获取batch_job_execution_params表中对应JobExecution_ID的入参
	final String GET_PARAMTERSSTRING = "SELECT LONG_VAL FROM BATCH_JOB_EXECUTION_PARAMS WHERE JOB_EXECUTION_ID = ("+GET_JOB_EXECUTION_ID+") ORDER BY LONG_VAL DESC LIMIT 1";
	// 判断表中是否有数据（没有则表示是第一次运行）
	final String CHECK_INSTANCE_ISNULL = "SELECT COUNT(1) FROM BATCH_JOB_INSTANCE WHERE JOB_NAME = ?";
	
	// 获取上一次跑批状态
	public boolean checkLastBatchStatus() {
		// 第一次运行时，数据库中是没有数据的，所以要先排除第一次跑批的场景
		if (this.checkFirstRun()) {
			System.out.println("first run!");
			return true;
		}
		boolean flag = false;
		String status = jdbcTemplage.queryForObject(GET_STEP_EXCUTION_STATUS,new String[] {JOB_NAME} ,String.class);
		if ("COMPLETED".equals(status)) {
			flag = true;
		}
		return flag;
	}
	
	// 获取上一次跑批的入参params
	public Long getParamters() {
		Long paramters = jdbcTemplage.queryForObject(GET_PARAMTERSSTRING,new String[] {JOB_NAME}, Long.class);
		return paramters;
	}
	// 查看是否时第一次跑批
	public boolean checkFirstRun() {
		Integer count = jdbcTemplage.queryForObject(CHECK_INSTANCE_ISNULL,new String[] {JOB_NAME}, Integer.class);
		return count == 0 ? true:false;
	}
}
