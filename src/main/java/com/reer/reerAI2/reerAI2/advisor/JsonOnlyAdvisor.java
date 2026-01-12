package com.reer.reerAI2.reerAI2.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
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
        ChatOptions options = request.prompt().getOptions();
        System.out.println("------------------------");
        System.out.println(request.prompt().getOptions());
        System.out.println("------------------------");
        List<Message> messages = new ArrayList<>(originalPrompt.getUserMessages());
        SystemMessage systemMessage = originalPrompt.getSystemMessage();
        if (!systemMessage.getText().isEmpty() || !systemMessage.getText().isBlank()) {
            messages.addFirst(systemMessage);
//            System.out.println("_____________________________________________________________________________________");
//            System.out.println(" originalPrompt.getSystemMessage  : " +originalPrompt.getSystemMessage() );
//            System.out.println( "systemMessage.getText() : "+  systemMessage.getText());
//            System.out.println("_____________________________________________________________________________________");
        }


        List<Message> instructions = originalPrompt.getInstructions();
        if (!instructions.isEmpty()) {
            instructions.forEach(messages::addFirst);
        }

        System.out.println(messages);
        Prompt modifiedPrompt = new Prompt(messages, options);

//        System.out.println("req. :  model : "+request.prompt().getOptions().getModel());;
//        System.out.println("req. :  max tokens : "+request.prompt().getOptions().getMaxTokens());;
//        System.out.println(" req : temp : "+request.prompt().getOptions().getTemperature());
//        System.out.println(" req : context : "+request.context());
        ChatClientRequest modifiedRequest =
                ChatClientRequest.builder()


                        .prompt(modifiedPrompt)

                        .build();


        // following commented lines gives null pointer exceptions

//        System.out.println(" modifiedRequest.prompt().getOptions().getModel() "+modifiedRequest.prompt().getOptions().getModel());
//        System.out.println(" modifiedRequest.prompt().getOptions().getTemperature() "+modifiedRequest.prompt().getOptions().getTemperature());
//        System.out.println(" modifiedRequest.prompt().getOptions().getMaxTokens() "+modifiedRequest.prompt().getOptions().getMaxTokens());

        // 🔹 AFTER LLM CALL (optional)
        return chain.nextCall(modifiedRequest);
    }
}
