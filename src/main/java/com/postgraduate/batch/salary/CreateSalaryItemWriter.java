package com.postgraduate.batch.salary;

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
public class CreateSalaryItemWriter implements ItemWriter<CreateSalary> {
    private final CreateSalaryRepository createSalaryRepository;

    @Override
    public void write(Chunk<? extends CreateSalary> chunk) {
        List<CreateSalary> createSalaries = new ArrayList<>();
        chunk.forEach(createSalaries::add);
        createSalaryRepository.insertAllSalary(createSalaries);
        log.info("salary 자동 생성 ChunkSize : {}", chunk.size());
    }
}
