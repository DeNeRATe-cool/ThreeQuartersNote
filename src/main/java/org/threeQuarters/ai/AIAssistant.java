package org.threeQuarters.ai;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.threeQuarters.util.ResourceUtils;

import java.io.IOException;
import java.util.Arrays;

public class AIAssistant {
    private Message systemMessage;
    private String resourcePath;
    private String ask;

    public AIAssistant() throws IOException {
        resourcePath = getClass().getClassLoader().getResource("").getPath().substring(1);

        systemMessage = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(ResourceUtils.readFileAsText(resourcePath + "assprompt.txt"))
                .build();
    }

    public String ans(String ask) throws NoApiKeyException, InputRequiredException {
        Generation generation = new Generation();
        StringBuilder sb = new StringBuilder();
        sb.append(ask);
        System.out.println(sb);
        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(sb.toString())
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey("sk-625740cda11745a39e90cd975d7b7220")
                .model("qwen-max")
                .messages(Arrays.asList(systemMessage, userMessage))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        GenerationResult result = generation.call(param);
        String content = result.getOutput().getChoices().get(0).getMessage().getContent();
        return content;
    }

    public static void main(String[] args) {
        String content = "我怎么才能做到自律呢";
        AIAssistant aiAssistant = null;
        try {
            aiAssistant = new AIAssistant();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println(aiAssistant.ans(content));
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }

}
