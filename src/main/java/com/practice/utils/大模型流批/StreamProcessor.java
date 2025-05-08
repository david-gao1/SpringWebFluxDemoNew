package com.practice.utils.大模型流批;

import com.practice.utils.ChatCompletionEntity;
import com.practice.utils.ChatCompletionUtils;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatMessage;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.practice.utils.LLMUtils.getChatCompletionEntity;
import static com.practice.utils.LLMUtils.setChatMessages;


public class StreamProcessor {


    public static void main(String[] args) {

        ChatCompletionEntity entity = getChatCompletionEntity();

        List<ChatMessage> chatMessages = setChatMessages("产生一个有聚合功能的sql，简要分析一种情况即可，不要分析太多", "你是一个sql专家");
        entity.setMessages(chatMessages);
        Flowable<ChatCompletionChunk> flowable =
                ChatCompletionUtils.getStreamChatCompletionRequestByEntity(entity);

        Flux<String> stringFlux = RxJava2Adapter.flowableToFlux(processFlowable(flowable));
        stringFlux.subscribe(content -> {
            // 调用者处理返回的内容
            System.out.println("处理内容: " + content);
        });
    }


    public static Flowable<String> processFlowable(Flowable<ChatCompletionChunk> flowable) {
        AtomicReference<StringBuilder> fullContent = new AtomicReference<>(new StringBuilder());

        return flowable
                .observeOn(Schedulers.io()) // 在 IO 线程处理
                .filter(chunk -> !chunk.getSource().equals("[DONE]"))
                .map(chunk -> {
                    String content = chunk.getChoices().get(0).getMessage().getReasoningContent();
                    if (content == null) {
                        content = chunk.getChoices().get(0).getMessage().getContent();
                    }
                    fullContent.get().append(content);
                    return content;
                })
                .concatWith(Flowable.fromCallable(() -> fullContent.get().toString()));

    }

}



