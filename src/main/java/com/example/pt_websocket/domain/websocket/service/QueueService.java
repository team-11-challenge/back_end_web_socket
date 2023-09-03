package com.example.pt_websocket.domain.websocket.service;

import com.example.pt_websocket.domain.websocket.dto.RegistrationRequestDto;
import com.example.pt_websocket.domain.websocket.event.Event;
import com.example.pt_websocket.global.enums.SuccessCode;
import com.example.pt_websocket.global.exception.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

import static com.example.pt_websocket.global.enums.SuccessCode.REGISTRATION_SUCCESS;


@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private static final long FIRST_ELEMENT = 0;
    private static final long LAST_ELEMENT = -1;


    public void getOrder(Event event) throws JsonProcessingException {
        final long start = FIRST_ELEMENT;
        final long end = LAST_ELEMENT;

        Set<String> queue = redisTemplate.opsForZSet().range(event.toString(), start, end);
        Objects.requireNonNull(queue).parallelStream().forEach(member -> {
            Long rank = redisTemplate.opsForZSet().rank(event.toString(), member);
            if (rank != null) {
                RegistrationRequestDto requestDto;
                try {
                    requestDto = objectMapper.readValue(member, RegistrationRequestDto.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                log.info("'{}'님의 현재 대기열은 {}명 남았습니다.", requestDto.getStudentNum(), rank);
                simpMessageSendingOperations.convertAndSend("/sub/order/" + requestDto.getStudentNum(), rank);
            }
        });
    }

    public void getResult(Event event) {
        final long start = FIRST_ELEMENT;
        final long end = LAST_ELEMENT;

        Set<String> queue = redisTemplate.opsForZSet().range(event.toString(), start, end);
        Objects.requireNonNull(queue).parallelStream().forEach(value -> {
            String[] split = value.split("--pt--");
            String studentNum = split[0];
            String result = split[1];
            if(result.equals(REGISTRATION_SUCCESS.getDetail())) {
                log.info("'{}'님의 registration 요청이 성공적으로 수행되었습니다.", studentNum);
            } else {
                log.error(result);
            }
            redisTemplate.opsForZSet().remove(event.toString(), value);

            simpMessageSendingOperations.convertAndSend("/sub/result/" + studentNum, result);
        });
    }
}