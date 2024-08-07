package com.postgraduate.domain.wish.domain.service;

import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.repository.WishRepository;
import com.postgraduate.domain.wish.exception.WishNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class WishGetServiceTest {
    @Mock
    private WishRepository wishRepository;
    @InjectMocks
    private WishGetService wishGetService;

    @Test
    @DisplayName("Wish 조회 예외 테스트")
    void byWishIdFail() {
        long wishId = 1L;
        given(wishRepository.findByWishIdAndMatchingReceiveIsTrueAndUser_IsDeleteIsFalse(wishId))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> wishGetService.byWishId(wishId))
                .isInstanceOf(WishNotFoundException.class);
    }

    @Test
    @DisplayName("Wish 조회 테스트")
    void byWishId() {
        long wishId = 1L;
        Wish wish = mock(Wish.class);
        given(wishRepository.findByWishIdAndMatchingReceiveIsTrueAndUser_IsDeleteIsFalse(wishId))
                .willReturn(Optional.of(wish));

        assertThat(wishGetService.byWishId(wishId))
                .isEqualTo(wish);
    }
}