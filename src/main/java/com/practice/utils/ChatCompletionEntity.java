package com.practice.utils;

import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.Data;

import java.util.List;


@Data
public class ChatCompletionEntity {
    //messages、message二选一必填，其他选填
    List<ChatMessage> messages;
    private String message;

    GptModelType modelType = GptModelType.PRO;
    private Integer maxTokens;//最大输出长度
    private Double temperature;//温度
    private Double topP;

    //自定义模型配置
    String modelUrl;//模型地址，示例 http://api.openai.com/v1/
    String modelName;//模型名称
    String apiKey;//模型 api key

    ChatResponseFormatEnum responseFormat;

    //是否开启优化，NL2SQL等功能可以选择关闭
    boolean optimizeLLMFlag = true;
//    Double presencePenalty;
//    Double frequencyPenalty;
//    Integer seed;
//    Integer n;
}
