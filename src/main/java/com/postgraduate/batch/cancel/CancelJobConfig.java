package com.postgraduate.batch.cancel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CancelJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CancelMentoringWriter cancelMentoringWriter;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 50;

    @Bean(name = "cancelJob")
    public Job cancelJob() throws Exception {
        return new JobBuilder("cancelJob", jobRepository)
                .start(cancelStep())
                .build();
    }

    @Bean(name = "cancelStep")
    public Step cancelStep() throws Exception {
        return new StepBuilder("cancelStep", jobRepository)
                .<CancelMentoring, CancelMentoring>chunk(CHUNK_SIZE, transactionManager)
                .reader(itemReader())
                .writer(cancelMentoringWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    @Bean(name = "cancelReader")
    public JdbcPagingItemReader<CancelMentoring> itemReader() throws Exception {
        Map<String, Object> parameter = new HashMap<>();
        LocalDateTime now = now()
                .toLocalDate()
                .atStartOfDay();
        parameter.put("date", Timestamp.valueOf(now));

        return new JdbcPagingItemReaderBuilder<CancelMentoring>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new CancelMentoringRowMapper())
                .queryProvider(cancelQueryProvider())
                .parameterValues(parameter)
                .name("cancelReader")
                .build();
    }

    @Bean(name = "cancelQuery")
    public PagingQueryProvider cancelQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select mentoring_id, user_user_id, senior_senior_id, payment_payment_id");
        queryProvider.setFromClause("from mentoring");
        queryProvider.setWhereClause("where status = 'WAITING' and created_at < :date");
        queryProvider.setSortKey("mentoring_id");
        return queryProvider.getObject();
    }
}

