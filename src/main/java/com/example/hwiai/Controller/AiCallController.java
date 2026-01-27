package com.example.hwiai.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AiCallController {
    
    private final ChatClient chatClient;
    
    public AiCallController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/aiTest/hello")
    public String getHello() {
        return chatClient.prompt()
                .user("안녕하세요! 간단한 인사말을 해주세요.")
                .call()
                .content();
    }
    
    @GetMapping("/aiTest/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}