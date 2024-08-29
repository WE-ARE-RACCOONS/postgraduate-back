package com.postgraduate.global.auth.quit.application.mapper;

import com.postgraduate.global.auth.login.application.dto.req.SignOutRequest;
import com.postgraduate.global.auth.quit.domain.entity.Quit;
import com.postgraduate.global.auth.quit.domain.entity.constant.QuitReason;
import com.postgraduate.global.auth.quit.domain.entity.Quit.QuitBuilder;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuitMapper {
    public static Quit mapToQuit(User user, SignOutRequest request) {
        QuitBuilder builder = getBuilder(user, request);
        if (request.reason().equals(QuitReason.ETC))
            builder.etc(request.etc());
        return builder.build();
    }

    private static QuitBuilder getBuilder(User user, SignOutRequest request) {
        return Quit.builder()
                .role(user.getRole())
                .reason(request.reason().getReason());
    }
}
