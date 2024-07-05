package com.postgraduate.batch.salary;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.global.slack.SlackSalaryMessage;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydslitemreader.core.pagingitemreader.QueryDslZeroPagingItemReader;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.account.domain.entity.QAccount.account;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CreateSalaryJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SlackSalaryMessage slackSalaryMessage;
    private final SalaryGetService salaryGetService;
    private final CreateSalaryItemWriter createSalaryItemWriter;
    private final CreateSalarySkipListener createSalarySkipListener;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 50;

    @Bean(name = "salaryJob")
    public Job salaryJob() {
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
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean(name = "createSalaryStep")
    public Step createSalaryStep() {
        return new StepBuilder("createSalaryStep", jobRepository)
                .<CreateSalary, CreateSalary>chunk(CHUNK_SIZE, transactionManager)
                .reader(salaryReader())
                .writer(createSalaryItemWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .listener(createSalarySkipListener)
                .listener(new CreateSalaryStepListener())
                .build();
    }

    @Bean
    public QueryDslZeroPagingItemReader<CreateSalary> salaryReader() {
        LocalDate date = getSalaryDate().plusDays(7);
        log.info("not in date : {}", date);
        return new QueryDslZeroPagingItemReader<>(entityManagerFactory, CHUNK_SIZE, queryFactory ->
                queryFactory.select(Projections.constructor(CreateSalary.class,
                                senior.seniorId,
                                account.accountId,
                                account.bank,
                                account.accountNumber,
                                account.accountHolder))
                        .distinct()
                        .from(senior)
                        .join(user)
                        .on(senior.user.eq(user))
                        .leftJoin(account)
                        .on(account.senior.eq(senior))
                        .where(senior.seniorId.notIn(
                                        JPAExpressions
                                                .select(salary.senior.seniorId)
                                                .from(salary)
                                                .where(salary.salaryDate.eq(date))
                                ))
                        .orderBy(senior.seniorId.desc())
        );
    }
}
