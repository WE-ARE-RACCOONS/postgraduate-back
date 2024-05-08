package com.postgraduate.global.batch.done;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DoneJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final DoneMentoringProcessor doneMentoringProcessor;
    private final DoneMentoringWriter doneMentoringWriter;

    private static final int CHUNK_SIZE = 10;

    @Bean(name = "doneJob")
    public Job doneJob() throws Exception {
        return new JobBuilder("doneJob", jobRepository)
                .start(doneStep())
                .build();
    }

    @Bean(name = "doneStep")
    public Step doneStep() throws Exception {
        return new StepBuilder("doneStep", jobRepository)
                .<DoneMentoring, DoneMentoring>chunk(CHUNK_SIZE, transactionManager)
                .reader(doneReader())
                .processor(doneMentoringProcessor)
                .writer(doneMentoringWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    @Bean(name = "doneReader")
    public JdbcPagingItemReader<DoneMentoring> doneReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<DoneMentoring>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new DoneMentoringRowMapper())
                .queryProvider(doneQueryProvider())
                .name("doneReader")
                .build();
    }

    @Bean(name = "doneQuery")
    public PagingQueryProvider doneQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select m.mentoring_id, m.senior_senior_id, m.salary_salary_id, m.date, p.pay");
        queryProvider.setFromClause("from mentoring m join payment p on m.payment_payment_id = p.payment_id");
        queryProvider.setWhereClause("where m.status = 'EXPECTED'");
        queryProvider.setSortKey("mentoring_id");
        return queryProvider.getObject();
    }
}
