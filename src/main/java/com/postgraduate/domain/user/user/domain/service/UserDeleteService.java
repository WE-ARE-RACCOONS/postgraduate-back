package com.postgraduate.domain.user.user.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.repository.UserRepository;
import com.postgraduate.domain.user.wish.domain.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeleteService {
    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final MentoringRepository mentoringRepository;
    private final PaymentRepository paymentRepository;

    public void deleteUser(User user) {
        mentoringRepository.findAllByUser(user)
                .stream()
                .forEach(Mentoring::updateUserDelete);
        // payment 에서 User null로 변경
        paymentRepository.findAllByUser(user)
                .stream()
                .forEach(Payment::updateUserDelete);
        log.info("wish 삭제");
        wishRepository.deleteByUser(user);
        log.info("user 삭제");
        userRepository.delete(user);
        // mentoring 에서 user null로 변경
    }
}
