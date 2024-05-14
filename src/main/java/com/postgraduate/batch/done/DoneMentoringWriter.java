package com.postgraduate.batch.done;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DoneMentoringWriter implements ItemWriter<DoneMentoring> {
    private final DoneMentoringRepository doneMentoringRepository;

    @Override
    public void write(Chunk<? extends DoneMentoring> chunk) {
        List<DoneMentoring> doneMentorings = new ArrayList<>();
        chunk.forEach(doneMentorings::add);
        doneMentoringRepository.updateAllMentoring(doneMentorings);
        doneMentoringRepository.updateAllSalary(doneMentorings);
        log.info("멘토링 자동 완료 size : {}", chunk.size());
    }
}
