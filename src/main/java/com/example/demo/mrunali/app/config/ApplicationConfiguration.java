package com.example.demo.mrunali.app.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.example.demo.mrunali.app.model.ResourceMaster;
import com.example.demo.mrunali.app.processor.ResourceItemProcessor;

@Configuration
@EnableBatchProcessing
public class ApplicationConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Value("${report.dir}")
	private String reportDest;

	@Value("${report.file.name}")
	private String reportFile;
	
	@Value("${application.report.query}")
	private String reportQuery;
	
	@Value("${application.report.keys}")
	private String reportKeys;

	/* Job declaration */
	@Bean
	public Job readDataFromTable() {
		return jobBuilderFactory.get("readDataFromTable")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(writeToCSVStep())
				.next(printStatus())
				.end()
				.build();
	}
	
	@Bean
	public Step writeToCSVStep() {
		return stepBuilderFactory.get("writeToCSVStep")
				.<ResourceMaster, ResourceMaster> chunk(100)
				.reader(reader())
				.processor(processor())
				.writer(reportWriter())
				.build();
	}
	
	@Bean
	public Step printStatus() {
		return stepBuilderFactory.get("printStatus")
				.tasklet(new Tasklet() {
					
					private final Logger logger = LoggerFactory.getLogger(this.getClass());
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						logger.info("Report prepared at " + reportDest + reportFile);
						return RepeatStatus.FINISHED;
					}
				}).build();
	}
	
	/* Job Resources */
	@Bean
	public FlatFileItemWriter<ResourceMaster> reportWriter() {
		FlatFileItemWriter<ResourceMaster> writer = new FlatFileItemWriter<ResourceMaster>();
		writer.setShouldDeleteIfExists(true);
		writer.setEncoding("UTF-8");
		String reportFilePath = new StringBuilder(reportDest).append(reportFile).toString();
		writer.setResource(new FileSystemResource(reportFilePath));
		
		BeanWrapperFieldExtractor<ResourceMaster> extractor = new BeanWrapperFieldExtractor<ResourceMaster>();
		extractor.setNames(reportKeys.split(","));
		DelimitedLineAggregator<ResourceMaster> aggregator = new DelimitedLineAggregator<ResourceMaster>();
		aggregator.setDelimiter(",");
		aggregator.setFieldExtractor(extractor);
		writer.setLineAggregator(aggregator);
		
		return writer;
	}

	@Bean
	public ResourceItemProcessor processor() {
		return new ResourceItemProcessor();
	}

	@Bean
	public JdbcCursorItemReader<ResourceMaster> reader() {
		JdbcCursorItemReader<ResourceMaster> reader = new JdbcCursorItemReader<ResourceMaster>();
		reader.setDataSource(this.dataSource);
		reader.setSql(reportQuery);
		reader.setRowMapper(new ResourceMaster().new ResourceMasterRowMapper());
		return reader;
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobExecutionListenerSupport();
	}

}
