package com.practice.utils;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.atomic.AtomicReference;

import static com.practice.utils.LLMUtils.getChatCompletionChunkFlowable;


public class OpenAIStreamingOut {
    public static void main(String[] args) {
        Flowable<ChatCompletionChunk> flowable = getChatCompletionChunkFlowable();

        System.out.println("开始");
        AtomicReference<StringBuilder> fullContent = new AtomicReference<>(new StringBuilder());
        flowable
                .observeOn(Schedulers.io()) // 在 IO 线程处理
                .doOnNext(chunk -> {
                    System.out.println("开始打印" + chunk);
                    if (!chunk.getSource().equals("[DONE]")) {
                        String content = chunk.getChoices().get(0).getMessage().getReasoningContent();
                        if (content == null) {
                            content = chunk.getChoices().get(0).getMessage().getContent();
                        }
                        System.out.println(content);
                        fullContent.get().append(content);
                    }
                })
                .doOnComplete(() -> {
                    System.out.println("\n--- 完整的 Content ---");
                    System.out.println(fullContent.get().toString());
                })
                .subscribe();
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
                .doOnComplete(() -> fullContent.get().toString());
    }



}
