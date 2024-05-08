package com.postgraduate.global.batch.salary;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.global.batch.done.DoneMentoring;
import com.postgraduate.global.batch.done.DoneMentoringRowMapper;
import com.postgraduate.global.slack.SlackSalaryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SalaryJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SlackSalaryMessage slackSalaryMessage;
    private final SalaryGetService salaryGetService;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 10;

    @Bean(name = "salaryJob")
    public Job salaryJob() {
        return new JobBuilder("salaryJob", jobRepository)
                .start(sendSlackStep())
                .next(makeSalaryStep())
    }

    @Bean(name = "sendSlackStep")
    public Step sendSlackStep() {
        return new StepBuilder("sendSlackStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        List<Salary> salaries = salaryGetService.findAllLast();
                        slackSalaryMessage.sendSlackSalary(salaries);
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean(name = "makeSalaryStep")
    public Step makeSalaryStep() throws Exception {
        return new StepBuilder("makeSalaryStep", jobRepository)
                .<SalaryInfo, SalaryInfo>chunk(CHUNK_SIZE, transactionManager)
                .reader(salaryReader())
    }

    @Bean(name = "salaryReader")
    public JdbcPagingItemReader<SalaryInfo> salaryReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<SalaryInfo>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new DoneMentoringRowMapper())
                .queryProvider(doneQueryProvider())
                .name("doneReader")
                .build();
    }

    @Bean(name = "salaryQuery")
    public PagingQueryProvider doneQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select s.senior_id, a.bank, a.account_id, a.account_holder, a.account_number");
        queryProvider.setFromClause("from senior s left join account a on s.senior_id = a.senior_senior_id\n" +
                "join user u on s.user_user_id = u.user_id");
        queryProvider.setWhereClause("where u.is_delete = false");
        queryProvider.setSortKey("senior_id");
        return queryProvider.getObject();
    }
}
