package com.practice.serivice;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.schedulers.Schedulers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.practice.utils.LLMUtils.getChatCompletionChunkFlowable;

/**
 * 打字机效果验证
 */
@Service
public class ChunkStreamService {

    public Flux<String> stream() {
        Flowable<ChatCompletionChunk> flowable = getChatCompletionChunkFlowable(); // 模拟数据流
        StringBuilder fullContent = new StringBuilder();
        UnicastProcessor<String> processor = UnicastProcessor.create();

        flowable
                .observeOn(Schedulers.io())
                .doOnNext(chunk ->
                        System.out.println(" Received chunk: " + chunk))
                .takeUntil((Predicate<? super ChatCompletionChunk>) chunk -> "stop".equals(chunk.getChoices().get(0).getFinishReason()))
                .map(chunk -> {
                    String content = chunk.getChoices().get(0).getMessage().getReasoningContent();
                    if (content == null) {
                        content = chunk.getChoices().get(0).getMessage().getContent();
                    }
                    return content == null ? "" : content;
                })
                .filter(Objects::nonNull)
                .buffer(30, TimeUnit.MILLISECONDS)
                .filter(batch -> !batch.isEmpty())
                .doOnNext(batch -> {
                    String merged = String.join("", batch);
                    fullContent.append(merged);
                    processor.onNext(merged);
                })
                .doOnComplete(() -> {
                    processor.onComplete();
                    System.out.println("--- 全部内容已积累 ---");
                    System.out.println(fullContent.toString());
                })
                .doOnError(processor::onError)
                .subscribe();


        return Flux.from(processor);
    }


}
