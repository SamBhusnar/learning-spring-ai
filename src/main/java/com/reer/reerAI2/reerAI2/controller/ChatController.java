package com.reer.reerAI2.reerAI2.controller;

import com.reer.reerAI2.reerAI2.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final AiService aiService;

    public ChatController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam String q,
            @RequestParam String askFor) {

        return switch (askFor.toLowerCase()) {
            case "fast" -> ResponseEntity.ok(aiService.fast(q));
            case "smart" -> ResponseEntity.ok(aiService.smart(q));
            case "enterprise" -> ResponseEntity.ok(aiService.enterprise(q));
            default -> ResponseEntity.badRequest()
                    .body("askFor must be fast | smart | enterprise");
        };
    }

}



