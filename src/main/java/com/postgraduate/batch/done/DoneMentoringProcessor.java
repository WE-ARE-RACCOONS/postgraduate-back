package com.postgraduate.batch.done;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public class DoneMentoringProcessor implements ItemProcessor<DoneMentoring, DoneMentoring> {
    @Override
    public DoneMentoring process(DoneMentoring doneMentoring) {
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime doneDate = parse(doneMentoring.date(), formatter);
        if (now().minusDays(3)
                .isAfter(doneDate))
            return doneMentoring;
        return null;
    }
}
