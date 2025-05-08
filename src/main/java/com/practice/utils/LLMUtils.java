
package com.practice.utils;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatMessage;
import io.reactivex.Flowable;

import java.util.ArrayList;
import java.util.List;

import static com.practice.utils.PromptUtils.PromptNL2sql;

/**
 * @Author: gaoliang
 * @CreateTime: 2025-05-08
 * @Description: description
 */
public class LLMUtils {


    public static void main(String[] args) {
        // 获取名为"JAVA_HOME"的环境变量的值
        String javaHome = System.getenv("apiKey");
        if (javaHome != null) {
            System.out.println("JAVA_HOME的值为: " + javaHome);
        } else {
            System.out.println("未找到名为JAVA_HOME的环境变量");
        }
    }


    public static Flowable<ChatCompletionChunk> getChatCompletionChunkFlowable() {
        ChatCompletionEntity chatCompletionEntity = getChatCompletionEntity();

//        List<ChatMessage> chatMessages = setChatMessages("产生一个有聚合功能的sql", "你是一个sql专家");
        List<ChatMessage> chatMessages = setChatMessages("2020年之前入职的员工有多少人", PromptNL2sql);
        chatCompletionEntity.setMessages(chatMessages);
        Flowable<ChatCompletionChunk> flowable =
                ChatCompletionUtils.getStreamChatCompletionRequestByEntity(chatCompletionEntity);
        return flowable;
    }

    public static List<ChatMessage> setChatMessages(String query, String prompt) {
        ChatMessage systemMessage = ChatCompletionUtils.getSystemMessage(prompt);
        ChatMessage userMessage = ChatCompletionUtils.getUserMessage(query);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);
        return messages;
    }

    public static ChatCompletionEntity getChatCompletionEntity() {
        ChatCompletionEntity entity = new ChatCompletionEntity();
        entity.setApiKey(System.getenv("apiKey"));
        entity.setModelUrl(System.getenv("modelUrl"));
        entity.setModelName(System.getenv("modelName"));
        return entity;
    }
}

