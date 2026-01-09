package com.reer.reerAI2.reerAI2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final ChatClient llama3Client;  // Ollama
    private final ChatClient phi3Client;    // Ollama
    private final ChatClient nvidiaClient;  // NVIDIA NIM

    public AiService(
            @Qualifier("llama3Client") ChatClient llama3Client,
            @Qualifier("phi3Client") ChatClient phi3Client,
            @Qualifier("nvidiaClient") ChatClient nvidiaClient
    ) {
        this.llama3Client = llama3Client;
        this.phi3Client = phi3Client;
        this.nvidiaClient = nvidiaClient;
    }

    public String fast(String q) {
        return phi3Client.prompt(q).call().content();
    }

    public String smart(String q) {
        return llama3Client.prompt(q).call().content();
    }

    public String enterprise(String q) {


        PromptTemplate promptTemplate=new PromptTemplate(
                """
                tell me about  %s
                """.formatted("java")
        );
        SystemPromptTemplate systemPromptTemplate = SystemPromptTemplate.builder()
                .template("You are a helpful {name} assistant.")
                .build();
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "codding"));
        Message userMessage = promptTemplate.createMessage();
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        String content = nvidiaClient.prompt(prompt).call().content();
        System.out.println(content);
        return    content;
    }
}
