package com.postgraduate.global.batch.cancel;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import jakarta.persistence.EntityManagerFactory;
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

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CancelJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CancelWriter cancelWriter;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 10;

    @Bean(name = "cancelJob")
    public Job cancelJob() throws Exception {
        return new JobBuilder("cancelJob", jobRepository)
                .start(cancelStep())
                .build();
    }

    @Bean(name = "cancelStep")
    public Step cancelStep() throws Exception {
        return new StepBuilder("cancelStep", jobRepository)
                .<Mentoring, Mentoring>chunk(CHUNK_SIZE, transactionManager)
                .reader(itemReader())
                .writer(cancelWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Mentoring> itemReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<Mentoring>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new MentoringRowMapper())
                .queryProvider(queryProvider())
                .name("reader")
                .build();
    }

    @Bean
    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select mentoring_id");
        queryProvider.setFromClause("from mentoring");
        queryProvider.setWhereClause("where mentoring.status = 'WAITING'");
        queryProvider.setSortKey("mentoring_id");
        return queryProvider.getObject();
    }
}
