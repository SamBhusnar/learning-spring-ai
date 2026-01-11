package com.reer.reerAI2.reerAI2.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.ArrayList;
import java.util.List;

public class JsonOnlyAdvisor implements CallAdvisor {

    @Override
    public String getName() {
        return "json-only-advisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientResponse adviseCall(
            ChatClientRequest request,
            CallAdvisorChain chain) {

        // 🔹 BEFORE LLM CALL
        Prompt originalPrompt = request.prompt();
        List<Message> messages = new ArrayList<>(originalPrompt.getUserMessages());
        SystemMessage systemMessage = originalPrompt.getSystemMessage();
        if(!systemMessage.getText().isEmpty()||!systemMessage.getText().isBlank()){
            messages.addFirst(systemMessage);
            System.out.println("_____________________________________________________________________________________");
            System.out.println(" originalPrompt.getSystemMessage  : " +originalPrompt.getSystemMessage() );
            System.out.println( "systemMessage.getText() : "+  systemMessage.getText());
            System.out.println("_____________________________________________________________________________________");
        }


        List<Message> instructions = originalPrompt.getInstructions();
        if(  !instructions.isEmpty()){
            instructions.forEach(messages::addFirst);
        }
//        messages.addFirst( new SystemMessage("""
//            only give information in json not in text.
//            don't give any explanation.
//            don't give any extra information.
//            complete the json .
//            don't terminate the json with any extra information.
//            don't terminate the json without completing it.
//                Each profile must contain:
//                        - name
//                        - age
//                        - skills (cricket-related)
//        """));
        System.out.println(messages);
        Prompt modifiedPrompt = new Prompt(messages);


        ChatClientRequest modifiedRequest =
                ChatClientRequest.builder()

                        .prompt(modifiedPrompt)

                        .build();

        // 🔹 CALL NEXT ADVISOR / MODEL

        ChatClientResponse response = chain.nextCall(modifiedRequest);

        // 🔹 AFTER LLM CALL (optional)
        return response;
    }
}
