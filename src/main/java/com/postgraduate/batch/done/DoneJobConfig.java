package com.postgraduate.batch.done;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.querydslitemreader.core.pagingitemreader.expression.Expression;
import com.querydslitemreader.core.pagingitemreader.options.QueryDslNoOffsetNumberOptions;
import com.querydslitemreader.core.pagingitemreader.QueryDslNoOffsetPagingItemReader;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DoneJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DoneMentoringProcessor doneMentoringProcessor;
    private final DoneMentoringWriter doneMentoringWriter;
    private final DoneMentoringSkipListener doneMentoringSkipListener;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 50;

    @Bean(name = "doneJob")
    public Job doneJob() {
        return new JobBuilder("doneJob", jobRepository)
                .start(doneStep())
                .build();
    }

    @Bean(name = "doneStep")
    public Step doneStep() {
        return new StepBuilder("doneStep", jobRepository)
                .<Mentoring, DoneMentoring>chunk(CHUNK_SIZE, transactionManager)
                .reader(doneReader())
                .processor(doneMentoringProcessor)
                .writer(doneMentoringWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .listener(doneMentoringSkipListener)
                .build();
    }

    @Bean(name = "doneReader")
    public QueryDslNoOffsetPagingItemReader<Mentoring> doneReader() {
        QueryDslNoOffsetNumberOptions<Mentoring, Long> options =
                new QueryDslNoOffsetNumberOptions(mentoring.mentoringId, Expression.DESC);

        return new QueryDslNoOffsetPagingItemReader<>(entityManagerFactory, CHUNK_SIZE, options, queryFactory ->
                queryFactory.selectFrom(mentoring)
                        .distinct()
                        .join(payment)
                        .on(mentoring.payment.eq(payment))
                        .fetchJoin()
                        .join(senior)
                        .on(mentoring.senior.eq(senior))
                        .fetchJoin()
                        .join(salary)
                        .on(mentoring.salary.eq(salary))
                        .fetchJoin()
                        .where(mentoring.status.eq(Status.EXPECTED))
        );
    }
}
