package com.postgraduate.domain.salary.application.dto;

import com.postgraduate.domain.member.senior.domain.entity.Account;
import com.postgraduate.domain.member.senior.domain.entity.Senior;

public record SeniorAndAccount(Senior senior, Account account) {}
