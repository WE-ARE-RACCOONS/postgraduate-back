package com.postgraduate.domain.senior.salary.application.dto;

import com.postgraduate.domain.senior.account.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Senior;

public record SeniorAndAccount(Senior senior, Account account) {}
