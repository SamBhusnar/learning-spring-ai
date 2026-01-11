package com.reer.reerAI2.reerAI2.controller;

import com.reer.reerAI2.reerAI2.service.AiService;
import com.reer.reerAI2.reerAI2.service.AiServiceStreaming;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final AiServiceStreaming aiServiceStreaming;
    private  final AiService aiService;

    public ChatController(AiServiceStreaming aiServiceStreaming,AiService aiService) {
        this.aiServiceStreaming = aiServiceStreaming;
        this.aiService=aiService;
    }

    @GetMapping("/chat" )
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
    @GetMapping(value = "/chat/streaming"  ,produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStreaming(
            @RequestParam String q,
            @RequestParam String askFor) {

         if (askFor.equalsIgnoreCase( "enterprise" )) {

            return  aiServiceStreaming.enterprise(q);
        } else{

               throw  new RuntimeException("only askFor=enterprise allowed not others ");
        }
    }

}



