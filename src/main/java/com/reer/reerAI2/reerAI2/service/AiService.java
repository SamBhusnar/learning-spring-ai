package com.reer.reerAI2.reerAI2.service;

import com.reer.reerAI2.reerAI2.records.UserProfile;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
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
        StructuredOutputConverter<List<UserProfile>> converter=
        new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<UserProfile>>()
                {});
// descriptive talking with llm
        PromptTemplate promptTemplate=new PromptTemplate(
                """
     Create top 10 Indian %s profiles from 2021 !
     
 """.formatted("cricketer"));
//        SystemPromptTemplate systemPromptTemplate = SystemPromptTemplate.builder()
//                // strict message to llm
//                .template("""
//You MUST return valid JSON.
//Return ONLY JSON.
//Do NOT add explanations.
//""")
//                .build();
//        Message systemMessage = systemPromptTemplate.createMessage();
//       Message systemMessage= new SystemMessage(
//                """
//                       give information in only text  not in json
//                        """
//        );
        Message userMessage = promptTemplate.createMessage();
        Message systemMessage=new SystemMessage("""
        Strictly must obey these instructions :
                 %s
        """.formatted(converter.getFormat()));
        Prompt prompt = new Prompt(List.of( systemMessage,  userMessage));
//        List<UserProfile> entity = nvidiaClient.prompt(prompt).call().entity(converter);
//        System.out.println(entity);
//        return entity.toString();
        ChatClient.ChatClientRequestSpec prompt1 = nvidiaClient.prompt(prompt);
//
        ChatClient.CallResponseSpec call = prompt1.call();

        ChatResponse chatClientResponse = call.chatResponse();
        System.out.println("after call : "+chatClientResponse.getMetadata().getModel());;
        System.out.println("after call metadata  : "+chatClientResponse.getResult().getOutput().getMetadata());
        System.out.println(  " after call  chatClientResponse.getMetadata().getPromptMetadata() ");
//        while (chatClientResponse.getMetadata().getPromptMetadata().iterator().hasNext()){
//            System.out.println(chatClientResponse.getMetadata().getPromptMetadata().iterator().next());
//        }
//        System.out.println(chatClientResponse.getMetadata().getUsage());;
        return  null;

//        System.out.println("________________________________________________________________________________________________");
//        System.out.println(chatClientResponse);
//        System.out.println("________________________________________________________________________________________________");
//        String text = chatClientResponse.getResult().getOutput().getText();
//return text;
//        List<UserProfile> entity = call.entity(converter);
//ś
//        System.out.println(entity);
//        return    entity.toString();
//
        // using fluent api
//     var entity=   nvidiaClient.prompt()
//                .system(system->system.text("Only give response in json format !"))
//                .user(user->user.text("""
//    Create top 10 Indian %s profiles from 2021.
//    Each profile must contain:
//    - name
//    - age
//    - skills (cricket-related)
//""".formatted("cricketer")))
//                .call()
//                .entity(converter);
//        System.out.println(entity);
//        return entity.toString();
    }
}
