package com.postgraduate.admin.application.dto.res;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.user.wish.domain.entity.Wish;

import java.util.Optional;

public record SeniorInfoQuery(Salary salary, Optional<Wish> wish){}
