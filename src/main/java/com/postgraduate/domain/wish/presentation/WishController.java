package com.postgraduate.domain.wish.presentation;

import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.wish.application.dto.request.WishCreateRequest;
import com.postgraduate.domain.wish.application.usecase.WishManageUseCase;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.wish.presentation.constant.WishResponseCode.WISH_CREATE;
import static com.postgraduate.domain.wish.presentation.constant.WishResponseMessage.APPLY_WISH;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wish")
@Tag(name = "Wish Controller")
public class WishController {
    private final WishManageUseCase wishManageUseCase;

    @PostMapping("/apply")
    public ResponseEntity<ResponseDto<Void>> applyWish(@AuthenticationPrincipal User user, @RequestBody WishCreateRequest request) {
        wishManageUseCase.applyWish(request, user);
        return ResponseEntity.ok(ResponseDto.create(WISH_CREATE.getCode(), APPLY_WISH.getMessage()));
    }
}
