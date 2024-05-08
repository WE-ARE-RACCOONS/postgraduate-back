package com.postgraduate.global.batch.salary;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
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
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CreateSalaryJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SlackSalaryMessage slackSalaryMessage;
    private final SalaryGetService salaryGetService;
    private final CreateSalaryItemWriter createSalaryItemWriter;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 10;

    @Bean(name = "salaryJob")
    public Job salaryJob() throws Exception {
        return new JobBuilder("salaryJob", jobRepository)
                .start(sendSlackStep())
                .next(createSalaryStep())
                .build();
    }

    @Bean(name = "sendSlackStep")
    public Step sendSlackStep() {
        return new StepBuilder("sendSlackStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    List<Salary> salaries = salaryGetService.findAllLast();
                    slackSalaryMessage.sendSlackSalary(salaries);
                    log.info("salarySize : {}", salaries.size()); // 임시
                    salaries.forEach(salary -> log.info("salary : {}", salary.getTotalAmount())); // 임시
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean(name = "createSalaryStep")
    public Step createSalaryStep() throws Exception {
        return new StepBuilder("createSalaryStep", jobRepository)
                .<CreateSalary, CreateSalary>chunk(CHUNK_SIZE, transactionManager)
                .reader(salaryReader())
                .writer(createSalaryItemWriter)
                .build();
    }

    @Bean(name = "salaryReader")
    public JdbcPagingItemReader<CreateSalary> salaryReader() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("salaryDate", getSalaryDate().plusDays(7));
        return new JdbcPagingItemReaderBuilder<CreateSalary>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new CreateSalaryRowMapper())
                .queryProvider(salaryQueryProvider())
                .parameterValues(parameters)
                .name("salaryReader")
                .build();
    }

    @Bean(name = "salaryQuery")
    public PagingQueryProvider salaryQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT s.senior_id, a.bank, a.account_id, a.account_holder, a.account_number");
        queryProvider.setFromClause("FROM senior s\n" +
                "JOIN user u ON s.user_user_id = u.user_id\n" +
                "LEFT JOIN account a ON s.senior_id = a.senior_senior_id");
        queryProvider.setWhereClause("WHERE u.is_delete = false\n" +
                "AND s.senior_id NOT IN (\n" +
                "SELECT senior_senior_id\n" +
                "FROM salary\n" +
                "WHERE salary_date = :salaryDate\n" +
                ")");
        queryProvider.setSortKey("senior_id");
        return queryProvider.getObject();
    }
}
