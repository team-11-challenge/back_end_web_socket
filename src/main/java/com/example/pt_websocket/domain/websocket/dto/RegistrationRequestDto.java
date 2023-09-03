package com.example.pt_websocket.domain.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationRequestDto {
    private long studentId;
    private long courseId;
    private String studentNum;
}
