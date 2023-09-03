package com.example.pt_websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PtWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(PtWebsocketApplication.class, args);
    }

}
