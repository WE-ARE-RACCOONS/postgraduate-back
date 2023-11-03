package com.postgraduate.domain.senior.application.dto.res;

import com.postgraduate.domain.senior.domain.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SeniorProfileResponse {
    private Profile profile;
}
