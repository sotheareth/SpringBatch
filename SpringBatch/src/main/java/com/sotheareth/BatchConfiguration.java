package com.sotheareth;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.sotheareth.domain.UserRegistration;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:/application.properties")
public class BatchConfiguration {
	private static final String INSERT_REGISTRATION_QUERY =
	"insert into USER_REGISTRATION(FIRST_NAME, LAST_NAME, COMPANY, ADDRESS,CITY,STATE,ZIP,COUNTY,URL,PHONE_NUMBER,FAX)" +
	" values " +
	"(:firstName,:lastName,:company,:address,:city,:state,:zip,:county,:url,:phoneNumber,:fax)";
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Value("file:D:/backup_old/Projects/OpenShiftJBoss/SpringBatch/src/main/resources/registrations.csv")
	private Resource input;
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() {
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setUrl(env.getRequiredProperty("spring.datasource.url"));
//		dataSource.setDriverClassName(env.getRequiredProperty("spring.datasource.driver"));
//		dataSource.setUsername(env.getProperty("spring.datasource.username"));
//		dataSource.setPassword(env.getProperty("spring.datasource.password"));
//		return dataSource;
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/spring_batch?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();   
	}

	@Bean
	ItemWriter<UserRegistration> jdbcItemWriter() {
		JdbcBatchItemWriter<UserRegistration> itemWriter = new JdbcBatchItemWriter<UserRegistration>();
		itemWriter.setDataSource(dataSource());
		itemWriter.setSql(INSERT_REGISTRATION_QUERY);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<UserRegistration>());
		return itemWriter;
	}

	@Bean
	LineMapper<UserRegistration> lineMapper() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setNames(new String[] { "firstName", "lastName", "company",
				"address", "city", "state", "zip", "county", "url",
				"phoneNumber", "fax" });
		BeanWrapperFieldSetMapper<UserRegistration> fieldSetMapper = new BeanWrapperFieldSetMapper<UserRegistration>();
		fieldSetMapper.setTargetType(UserRegistration.class);
		DefaultLineMapper<UserRegistration> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}

	@Bean
	ItemReader<UserRegistration> csvFileReader() throws UnexpectedInputException, ParseException, Exception {
		Resource resource = new ClassPathResource("registrations.csv");
		FlatFileItemReader<UserRegistration> itemReader = new FlatFileItemReader<>();
		itemReader.setLineMapper(lineMapper());
		itemReader.setResource(resource);
		itemReader.open(new ExecutionContext());
		UserRegistration r = itemReader.read();
		System.out.println("file:" + r.getAddress());
		return itemReader;
	}

	@Bean
	protected Step step1() throws UnexpectedInputException, ParseException, Exception {
		return stepBuilderFactory.get("step1")
				.<UserRegistration, UserRegistration> chunk(2)
				.reader(csvFileReader()).writer(jdbcItemWriter()).build();
	}

	@Bean
	public Job insertIntoDbFromCsvJob() throws UnexpectedInputException, ParseException, Exception {
		return jobBuilderFactory.get("insertIntoDbFromCsvJob").start(step1())
				.build();
	}

}
