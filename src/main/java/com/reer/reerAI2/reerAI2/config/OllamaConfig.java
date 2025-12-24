package com.reer.reerAI2.reerAI2.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OllamaConfig {

    @Bean("phi3Client")
    ChatClient phi3Client(
            @Qualifier("ollamaChatModel") ChatModel model
    ) {
        return ChatClient.builder(model)
                .defaultOptions(
                        OllamaChatOptions.builder()
                                .model("phi3:mini")
                                .temperature(0.2)
                                .build()
                )
                .build();
    }

    @Bean("llama3Client")
    ChatClient llama3Client(
            @Qualifier("ollamaChatModel") ChatModel model
    ) {
        return ChatClient.builder(model)
                .defaultOptions(
                        OllamaChatOptions.builder()
                                .model("llama3-safe:latest")
                                .temperature(0.4)
                                .build()
                )
                .build();
    }
}
