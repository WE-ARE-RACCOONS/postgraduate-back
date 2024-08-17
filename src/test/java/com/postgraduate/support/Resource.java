package com.postgraduate.support;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;

import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.EXPECTED;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.payment.domain.entity.constant.Status.DONE;
import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
import static com.postgraduate.domain.user.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.user.user.domain.entity.constant.Role.USER;
import static java.time.LocalDateTime.now;

public class Resource {
    private User user = new User(-1L, -1L, "mail", "후배", "011", "profile", 0, USER, true, now(), now(), false);
    private Wish wish = new Wish(-1L, "major", "field", true, user, com.postgraduate.domain.wish.domain.entity.constant.Status.WAITING);
    private User otherUser = new User(-3L, -3L, "mail", "다른후배", "011", "profile", 0, USER, true, now(), now(), false);
    private User userOfSenior = new User(-2L, -2L, "mail", "선배", "012", "profile", 0, SENIOR, true, now(), now(), false);
    private Info info = new Info("major", "서울대학교", "교수님", "키워드1,키워드2", "랩실", "인공지능", false, false, "인공지능,키워드1,키워드2", "chatLink", 30);
    private Profile profile = new Profile("저는요", "한줄소개", "대상");
    private Senior senior = new Senior(-1L, userOfSenior, "certification", com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING, 0, 0, info, profile, now(), now());
    private Senior otherSenior = new Senior(-3L, otherUser, "certification", com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING, 0, 0, info, null, now(), now());
    private SalaryAccount salaryAccount = new SalaryAccount("bank", "1234", "holder");
    private Salary salary = new Salary(-1L, false, senior, 20000, getSalaryDate(), LocalDateTime.now(), salaryAccount);
    private Payment payment = new Payment(-1L, user, senior, 20000, "-1", "123", "123", LocalDateTime.now(), LocalDateTime.now(), DONE);
    private Mentoring waitingMentoring = new Mentoring(-1L, user, senior, payment, salary, "topic", "question", "date1,date2,date3", 30, WAITING, now(), now());
    private Mentoring expectedMentoring = new Mentoring(-2L, user, senior, payment, salary, "topic", "question", "date", 30, EXPECTED, now(), now());
    private Mentoring doneMentoring = new Mentoring(-3L, user, senior, payment, salary, "topic", "question", "2024-02-03-18-12", 30, Status.DONE, now(), now());
    private Account account = new Account(-1L, "010", "신한", "김씨", senior);
    private List<Available> availables = List.of(
            new Available(-1L, "월", "17:00", "23:00", senior),
            new Available(-2L, "금", "10:00", "20:00", senior),
            new Available(-3L, "토", "10:00", "20:00", senior));
    public User getUser(){return user;}
    public User getOtherUser(){return otherUser;}
    public User getSeniorUser(){return userOfSenior;}
    public Wish getWish(){return wish;}
    public Senior getSenior() {return senior;}
    public Senior getOtherSenior() {return otherSenior;}
    public Account getAccount(){return account;}
    public Salary getSalary() {return salary;}
    public Payment getPayment() {return payment;}
    public Mentoring getWaitingMentoring() {return waitingMentoring;}
    public Mentoring getExpectedMentoring() {return expectedMentoring;}
    public Mentoring getDoneMentoring() {return doneMentoring;}
    public List<Available> getAvailables() {return availables;}
}
