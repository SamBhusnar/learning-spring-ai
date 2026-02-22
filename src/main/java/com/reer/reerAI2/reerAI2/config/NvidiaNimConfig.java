package com.reer.reerAI2.reerAI2.config;

import com.reer.reerAI2.reerAI2.advisor.JsonOnlyAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration

public class NvidiaNimConfig {

    private final ChatMemory chatMemory;

    public NvidiaNimConfig(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    @Bean("nvidiaClient")
    ChatClient nvidiaClient(
            @Qualifier("openAiChatModel") ChatModel model
    ) {
        return ChatClient.builder(model)

                .defaultAdvisors(
                        List.of(
                                MessageChatMemoryAdvisor.builder(
                                                chatMemory
                                        )

                                        .build(),
                                new JsonOnlyAdvisor(),
                                new SimpleLoggerAdvisor(),
                                new SafeGuardAdvisor(List.of("illegal work")))
                )

                .defaultOptions(ChatOptions.builder()
                        .model("meta/llama3-8b-instruct")
                        .temperature(0.5)
                        .maxTokens(700)
                        .build())
                .build();
    }
}
