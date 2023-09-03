package com.example.pt_websocket.domain.websocket.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Event {
    REGISTRATION("수강신청"), REGISTRATION_RESULT("신청결과");
    private String name;
}
