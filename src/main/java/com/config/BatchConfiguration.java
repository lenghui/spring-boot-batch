 package com.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.listern.JobCompletionNotificationListener;
import com.model.People;
import com.process.PeopleProcess;
import com.write.WriteForJob1;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public FlatFileItemReader<People> reader() throws Exception{
		return new FlatFileItemReaderBuilder()
				.name("peopleItemReader")
				.resource(new ClassPathResource("sample-data.csv"))
				.delimited()
				.names(new String[] {"firstName", "lastName"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<People>() {{
					setTargetType(People.class);
				}})
				.build();
	}
	
	@Bean
	public PeopleProcess processor() {
		return new PeopleProcess();
	}
	
//	@Bean
//	public JdbcBatchItemWriter<People> writer(DataSource dataSource){
//		return new JdbcBatchItemWriterBuilder<People>()
//				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//				.sql("INSERT INTO PEOPLE (FIRST_NAME, LAST_NAME) VALUES (:firstName, :lastName)")
//				.dataSource(dataSource)
//				.build();
//	}
	
	@Bean
	public WriteForJob1 writerForJob1() {
		return new WriteForJob1();
	}
	
	@Bean
	public Step step01() throws Exception {
		return stepBuilderFactory.get("step01")
				.<People, People> chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writerForJob1())
				.build();
	}
	
	@Bean(value="userJob")
	public Job importUserJob(JobCompletionNotificationListener listener, Step step01) {
		return jobBuilderFactory.get("myJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step01)
				.end()
				.build();
	}
	
	
}
