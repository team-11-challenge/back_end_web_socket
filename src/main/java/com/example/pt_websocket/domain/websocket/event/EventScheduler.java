package com.example.pt_websocket.domain.websocket.event;

import com.example.pt_websocket.domain.websocket.service.QueueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {
    private final QueueService queueService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Scheduled(fixedDelay = 1000)
    public void getOrder() throws JsonProcessingException {
        log.info("do work");
        simpMessageSendingOperations.convertAndSend("/sub/hey", "sup!");

//        queueService.getOrder(Event.REGISTRATION);
    }

    @Scheduled(fixedDelay = 100)
    public void getResult() throws JsonProcessingException {
        queueService.getResult(Event.REGISTRATION_RESULT);
    }
}