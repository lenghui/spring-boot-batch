package com.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.QueryBatchService;

@RestController
public class JobLaunchController {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private QueryBatchService queryBatchService;
	
	//调用跑批
	@RequestMapping("/invokejob")
	public String handle() {
		// 跑批前校验上次跑批是否成功，如果失败则拒绝跑批
		if (!queryBatchService.checkLastBatchStatus()) {
			return "上次跑批失败，请先完成续批！";
		}
		
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.toJobParameters();
		Job job = (Job) context.getBean("userJob");
		JobExecution jobExecution = null;
		try {
			// 在启动批处理时，当数据格式不对，异常已经在jar包中捕捉并不会抛出，所以我们得根据结果来判断执行状态
			jobExecution = jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException e) {
			e.printStackTrace();
			return e.toString();
		} catch (JobRestartException e) {
			e.printStackTrace();
			return e.toString();
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
			return e.toString();
		} catch (JobParametersInvalidException e) {
			e.printStackTrace();
			return e.toString();
		}
		return "Batch job has been invoked"+"。 jonName: "+job.getName()+", jobParameters:"+jobParameters
				+",status:"+(jobExecution == null?null:jobExecution.getStatus());
	}
	
	// 续批
	@RequestMapping("/reinvokejob")
	public String exceptionHandle() {
		// 获取上次跑批时的入参
		if (queryBatchService.getParamters() == null) {
			return "获取入参失败！";
		}
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", queryBatchService.getParamters())
				.toJobParameters();
		Job job = (Job) context.getBean("userJob");
		JobExecution jobExecution = null;
		try {
			jobExecution = jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException e) {
			e.printStackTrace();
			return e.toString();
		} catch (JobRestartException e) {
			e.printStackTrace();
			return e.toString();
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
			return e.toString();
		} catch (JobParametersInvalidException e) {
			e.printStackTrace();
			return e.toString();
		}
		
		return "Batch job has been invoked"+"。 jonName: "+job.getName()+", jobParameters:"+jobParameters
				+", status="+(jobExecution==null?null:jobExecution.getStatus());
	}
	

	
}
