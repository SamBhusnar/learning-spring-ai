package com.reer.reerAI2.reerAI2.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PrimaryChatModelConfig {

    @Bean
    @Primary
    ChatModel primaryChatModel(
            @Qualifier("ollamaChatModel") ChatModel ollamaChatModel
    ) {
        return ollamaChatModel;
    }
}
