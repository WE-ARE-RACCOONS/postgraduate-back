package com.postgraduate.global.logging.service;

import com.postgraduate.global.logging.dto.LogRequest;
import com.postgraduate.global.mq.producer.MessageProducer;
import com.postgraduate.global.slack.SlackMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {
    private final MessageProducer messageProducer;
    private final SlackMessage slackMessage;

    public void save(LogRequest logRequest) throws IOException {
        try {
            log.info("log save");
            messageProducer.sendMessage(logRequest);
        } catch (Exception ex) {
            log.error("로그 서버 연결 실패");
            slackMessage.sendSlackLog(ex);
        }
    }
}
