package com.practice.utils;

import com.theokanning.openai.completion.chat.AssistantMessage;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import static com.practice.utils.LLMUtils.getChatCompletionEntity;
import static com.practice.utils.PromptUtils.PromptNL2sql;


public class OpenAIBatchOut {
    public static void main(String[] args) {

        ChatCompletionEntity entity = getChatCompletionEntity();
        List<ChatMessage> chatMessages = setChatMessages("2020年之前入职的员工有多少人", PromptNL2sql);
        entity.setMessages(chatMessages);
        AssistantMessage assistantMessage = ChatCompletionUtils.getChatCompletionMessageByEntity(entity);
        String content = assistantMessage.getContent();
        System.out.println(content);

    }




    public static List<ChatMessage> setChatMessages(String query, String prompt) {
        ChatMessage systemMessage = ChatCompletionUtils.getSystemMessage(prompt);
        ChatMessage userMessage = ChatCompletionUtils.getUserMessage(query);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);
        return messages;
    }
}
