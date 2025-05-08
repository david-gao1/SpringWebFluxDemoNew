package com.practice.utils;



import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import ru.yandex.clickhouse.util.apache.StringUtils;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;



public class ChatCompletionUtils {


    static final Duration timeout = Duration.ofSeconds(600);


    public static Flowable<ChatCompletionChunk> getStreamChatCompletionRequestByEntity(ChatCompletionEntity entity) {

        // 初始化OpenAI服务客户端
        OpenAiService service = getOpenAiService(entity);
        ChatCompletionRequest chatCompletionRequest = getChatCompletionRequestByEntity(entity);
        // 发起聊天完成请求，获取结果
        return service.streamChatCompletion(chatCompletionRequest);
    }

    public static AssistantMessage getChatCompletionMessageByEntity(ChatCompletionEntity entity) {

        // 初始化OpenAI服务客户端
        OpenAiService service = getOpenAiService(entity);
        ChatCompletionRequest chatCompletionRequest = getChatCompletionRequestByEntity(entity);
        // 发起聊天完成请求，获取结果
        ChatCompletionResult chatCompletion = service.createChatCompletion(chatCompletionRequest);
        // 从结果中提取并返回第一个建议的消息内容
        return chatCompletion.getChoices().get(0).getMessage();
    }


    /**
     * 初始化OpenAI服务客户端
     */
    static OpenAiService getOpenAiService(ChatCompletionEntity entity) {
        String apiKey = entity.getApiKey();
        String modelUrl = entity.getModelUrl();
        // 初始化OpenAI服务客户端
        return new OpenAiService(apiKey, timeout, modelUrl);
    }

    static ChatCompletionRequest getChatCompletionRequestByEntity(ChatCompletionEntity entity) {
        String model = "";
        String modelName = entity.getModelName();
        if (!StringUtils.isBlank(modelName)) {
            model = modelName;
        }
        List<ChatMessage> messages = entity.getMessages();
        if (messages == null) {
            ChatMessage userMessage = getUserMessage(entity.getMessage());
            messages = new ArrayList<>();
            messages.add(userMessage);
        }
        ChatResponseFormatEnum responseFormat = entity.getResponseFormat();
        if (responseFormat != null) {
            if (responseFormat.equals(ChatResponseFormatEnum.JSON_OBJECT)) {
                ChatMessage systemMessage = getSystemMessage("请使用json格式回答");
                messages.add(0, systemMessage);
            }
        }
        // 构建聊天完成请求，包括模型、消息、生成结果数量和最大令牌数等参数
        return ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .n(1) // 指定只返回一个建议
                .maxTokens(entity.getMaxTokens())
                .temperature(entity.getTemperature())
                .topP(entity.getTopP())
                .build();
    }


    public static ChatMessage getSystemMessage(String message) {
        return new SystemMessage(message);
    }

    public static ChatMessage getUserMessage(String message) {
        return new UserMessage(message);
    }

    public static ChatMessage getAssistantMessage(String message) {
        return new AssistantMessage(message);
    }

}
