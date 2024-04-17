package com.postgraduate.support;

import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.EXPECTED;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.payment.domain.entity.constant.Status.DONE;
import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.time.LocalDateTime.now;

public class Resource {
    private User user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, USER, true, now(), now(), false);
    private User userOfSenior = new User(0L, 2L, "mail", "선배", "012", "profile", 0, SENIOR, true, now(), now(), false);
    private Info info = new Info("major", "서울대학교", "교수님", "키워드1,키워드2", "랩실", "인공지능", false, false, "인공지능,키워드1,키워드2");
    private Profile profile = new Profile("저는요", "한줄소개", "대상", "chatLink", 40);
    private Senior senior = new Senior(0L, userOfSenior, "certification", com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING, 0, info, profile, now(), now());
    private SalaryAccount salaryAccount = new SalaryAccount("bank", "1234", "holder");
    private Salary salary = new Salary(0L, false, senior, 20000, getSalaryDate(), LocalDateTime.now(), salaryAccount);
    private Payment payment = new Payment(0L, user, senior, 20000, "1", "123", "123", LocalDateTime.now(), LocalDateTime.now(), DONE);
    private Mentoring waitingMentoring = new Mentoring(0L, user, senior, payment, salary, "topic", "question", "date1,date2,date3", 30, WAITING, now(), now());
    private Mentoring expectedMentoring = new Mentoring(0L, user, senior, payment, salary, "topic", "question", "date", 30, EXPECTED, now(), now());
    private Mentoring doneMentoring = new Mentoring(0L, user, senior, payment, salary, "topic", "question", "2024-02-03-18-12", 30, Status.DONE, now(), now());
    private List<Available> availables = List.of(
            new Available(0L, "월", "17:00", "23:00", senior),
            new Available(0L, "금", "10:00", "20:00", senior),
            new Available(0L, "토", "10:00", "20:00", senior));
    public User getUser(){return user;}
    public User getSeniorUser(){return userOfSenior;}
    public Senior getSenior() {return senior;}
    public SalaryAccount getSalaryAccount() {return salaryAccount;}
    public Salary getSalary() {return salary;}
    public Payment getPayment() {return payment;}
    public Mentoring getWaitingMentoring() {return waitingMentoring;}
    public Mentoring getExpectedMentoring() {return expectedMentoring;}
    public Mentoring getDoneMentoring() {return doneMentoring;}
    public List<Available> getAvailables() {return availables;}
}
