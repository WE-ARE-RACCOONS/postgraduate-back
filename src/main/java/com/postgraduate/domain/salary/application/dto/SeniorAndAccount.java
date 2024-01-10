package com.postgraduate.domain.salary.application.dto;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Senior;

public record SeniorAndAccount(Senior senior, Account account) {}
