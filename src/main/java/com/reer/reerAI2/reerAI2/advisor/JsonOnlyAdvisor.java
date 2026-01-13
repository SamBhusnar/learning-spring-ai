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

        Prompt originalPrompt = request.prompt();
        ChatOptions options = request.prompt().getOptions();
        System.out.println("------------------------");
        System.out.println(request.prompt().getOptions());
        System.out.println("------------------------");
        List<Message> messages = new ArrayList<>(originalPrompt.getUserMessages());
        SystemMessage systemMessage = originalPrompt.getSystemMessage();
        if (!systemMessage.getText().isEmpty() || !systemMessage.getText().isBlank()) {
            messages.addFirst(systemMessage);
        }


        List<Message> instructions = originalPrompt.getInstructions();
        if (!instructions.isEmpty()) {
            instructions.forEach(messages::addFirst);
        }
        System.out.println("context :------------------------------------------------------" + request.context());

        System.out.println(messages);
        Prompt modifiedPrompt = new Prompt(messages, options);
        ChatClientRequest modifiedRequest =
                ChatClientRequest.builder()
                        .context(request.context())

                        .prompt(modifiedPrompt)


                        .build();


        var response = chain.nextCall(modifiedRequest);
        return response;
    }
}
