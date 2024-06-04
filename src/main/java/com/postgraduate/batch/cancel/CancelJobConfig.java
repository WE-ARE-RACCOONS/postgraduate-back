package com.postgraduate.batch.cancel;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.querydslitemreader.core.pagingitemreader.expression.Expression;
import com.querydslitemreader.core.pagingitemreader.options.QueryDslNoOffsetNumberOptions;
import com.querydslitemreader.core.pagingitemreader.options.QueryDslNoOffsetPagingItemReader;
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

import java.time.LocalDateTime;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static java.time.LocalDateTime.now;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CancelJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CancelMentoringProcessor cancelMentoringProcessor;
    private final CancelMentoringWriter cancelMentoringWriter;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 50;

    @Bean(name = "cancelJob")
    public Job cancelJob() {
        return new JobBuilder("cancelJob", jobRepository)
                .start(cancelStep())
                .build();
    }

    @Bean(name = "cancelStep")
    public Step cancelStep() {
        return new StepBuilder("cancelStep", jobRepository)
                .<Mentoring, CancelMentoring>chunk(CHUNK_SIZE, transactionManager)
                .reader(itemReader())
                .processor(cancelMentoringProcessor)
                .writer(cancelMentoringWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    @Bean(name = "cancelReader")
    public QueryDslNoOffsetPagingItemReader<Mentoring> itemReader() {
        QueryDslNoOffsetNumberOptions<Mentoring, Long> options = new QueryDslNoOffsetNumberOptions<>(mentoring.mentoringId, Expression.DESC);

        LocalDateTime now = now()
                .toLocalDate()
                .atStartOfDay();

        return new QueryDslNoOffsetPagingItemReader<>(entityManagerFactory, CHUNK_SIZE, options, queryFactory ->
            queryFactory.selectFrom(mentoring)
                    .join(payment)
                    .on(mentoring.payment.eq(payment))
                    .fetchJoin()
                    .join(user)
                    .on(mentoring.user.eq(user))
                    .fetchJoin()
                    .join(senior)
                    .on(mentoring.senior.eq(senior))
                    .fetchJoin()
                    .where(mentoring.status.eq(WAITING).and(mentoring.createdAt.before(now)))
        );
    }
}

