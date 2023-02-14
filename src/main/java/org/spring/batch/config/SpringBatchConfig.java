package org.spring.batch.config;

import org.spring.batch.item.Payment;
import org.spring.batch.mapper.PaymentFieldSetMapper;
import org.spring.batch.tasklet.FileMoveTasklet;
import org.spring.batch.validator.PaymentValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Value("C:\\projects\\java\\spring-batch\\csv\\data.csv")
    private PathResource inputFileCsv;

    private final static String FILE_MOVE_PATH = "C:\\projects\\java\\spring-batch\\csv\\move\\";

    @Bean
    public FlatFileItemReader<Payment> reader() {
        return new FlatFileItemReaderBuilder<Payment>()
                .name("PaymentReader")
                .resource(inputFileCsv)
                .delimited()
                .names("id", "amount", "date", "ord_number", "user_id")
                .linesToSkip(1)
                .fieldSetMapper(new PaymentFieldSetMapper())
                .build();
    }

    @Bean
    public ItemProcessor<Payment, Payment> processor() {
        ValidatingItemProcessor<Payment> processor =
                new ValidatingItemProcessor<>(new PaymentValidator());
        processor.setFilter(false);
        return processor;
    }

    @Bean
    public JdbcBatchItemWriter<Payment> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Payment>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO payment_tab (id, amount, date, ord_number, user_id) " +
                        "VALUES (:id, :amount, :date, :ordNumber, :userId)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importPaymentCsvJob(JobRepository jobRepository, Step step1, Step step2) {
        return new JobBuilder("importPaymentJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .next(step2)
                .end()
                .build();
    }

    @Bean(name = "step1")
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Payment> writer) {

        return new StepBuilder("step1", jobRepository)
                .<Payment, Payment>chunk(3, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean(name = "step2")
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
//        FileDel
        return new StepBuilder("step2", jobRepository)
                .tasklet(new FileMoveTasklet(inputFileCsv, FILE_MOVE_PATH), transactionManager)
                .build();
    }

}
