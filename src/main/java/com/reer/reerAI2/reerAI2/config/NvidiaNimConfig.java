package com.reer.reerAI2.reerAI2.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NvidiaNimConfig {

    @Bean("nvidiaClient")
    ChatClient nvidiaClient(
            @Qualifier("openAiChatModel") ChatModel model
    ) {
        return ChatClient.builder(model).build();
    }
}
