package org.threeQuarters.ai;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import io.reactivex.Flowable;
import lombok.Setter;
import org.threeQuarters.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

interface UpdateTextMethod {
    void update(String content);
}

public class AIService {
    private static AIService instance;

    private String resourcePath;
    private Message systemMessage;
    private List<Message> messages = new ArrayList<>();
    @Setter
    private static String content;

    private AIService() throws FileNotFoundException, IOException {
        resourcePath = getClass().getClassLoader().getResource("").getPath().substring(1);
        systemMessage = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(ResourceUtils.readFileAsText(resourcePath + "prompt_chat.txt")+(content == null ? "" : content))
                .build();
        messages.add(systemMessage);
    }

    private static AIService getInstance() throws FileNotFoundException, IOException {
        if (instance == null) {
            instance = new AIService();
        }
        return instance;
    }

    public static void updateAiServiceInstance() throws IOException {
        instance = new AIService();
    }

    private void clear_messages() {
        messages.clear();
        messages.add(systemMessage);
    }

    public static void clear() {
        try {
            getInstance().clear_messages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleGenerationResult(
            GenerationResult message, StringBuilder fullContent, UpdateTextMethod updateMethod) {
        String content = message.getOutput().getChoices().get(0).getMessage().getContent();
        fullContent.append(content);
        updateMethod.update(fullContent.toString());
    }

    public static void chat(String content, UpdateTextMethod updateMethod) {
        try {
            List<Message> messages = new ArrayList<>(getInstance().messages);
            Generation generation = new Generation();
            Message userMessage = Message.builder()
                    .role(Role.USER.getValue())
                    .content(content)
                    .build();
            messages.add(userMessage);
            GenerationParam param = GenerationParam.builder()
                    .apiKey("sk-625740cda11745a39e90cd975d7b7220")
                    .model("qwen-max")
                    .messages(messages)
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .incrementalOutput(true)
                    .build();
            Flowable<GenerationResult> result = generation.streamCall(param);
            StringBuilder fullContent = new StringBuilder();
            result.blockingForEach(message -> handleGenerationResult(message, fullContent, updateMethod));
            Message assistantMessage = Message.builder()
                .role(Role.ASSISTANT.getValue())
                .content(fullContent.toString())
                .build();
            getInstance().messages.add(userMessage);
            getInstance().messages.add(assistantMessage);
        } catch (Exception e) {
            e.printStackTrace();
            updateMethod.update("抱歉，发生了错误。");
        }
    }
}