package com.postgraduate.domain.user.quit.application.mapper;

import com.postgraduate.domain.auth.application.dto.req.SignOutRequest;
import com.postgraduate.domain.user.quit.domain.entity.Quit;
import com.postgraduate.domain.user.quit.domain.entity.Quit.QuitBuilder;
import com.postgraduate.domain.user.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.postgraduate.domain.user.quit.domain.entity.constant.QuitReason.ETC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuitMapper {
    public static Quit mapToQuit(User user, SignOutRequest request) {
        QuitBuilder builder = getBuilder(user, request);
        if (request.reason().equals(ETC))
            builder.etc(request.etc());
        return builder.build();
    }

    private static QuitBuilder getBuilder(User user, SignOutRequest request) {
        return Quit.builder()
                .role(user.getRole())
                .reason(request.reason().getReason());
    }
}
