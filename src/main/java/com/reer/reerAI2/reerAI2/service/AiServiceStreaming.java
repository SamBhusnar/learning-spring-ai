package com.reer.reerAI2.reerAI2.service;

import com.reer.reerAI2.reerAI2.records.UserProfile;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
@Service

public class AiServiceStreaming {

    private final ChatClient nvidiaClient;  // NVIDIA NIM
    public AiServiceStreaming( @Qualifier("nvidiaClient") ChatClient nvidiaClient){
        this.nvidiaClient = nvidiaClient;
    }
    public Flux<String> enterprise(String q) {

        PromptTemplate promptTemplate=new PromptTemplate(
                """
     tell me about lion and tiger overall big %s   
 """.formatted("cats"));
        Message userMessage = promptTemplate.createMessage();
        Message systemMessage=new SystemMessage("""
       response return in text not in json.
       response should be in polite language.
            """);
        Prompt prompt = new Prompt(List.of( systemMessage,  userMessage));

        return nvidiaClient.prompt(prompt).stream().content();
    }
}
