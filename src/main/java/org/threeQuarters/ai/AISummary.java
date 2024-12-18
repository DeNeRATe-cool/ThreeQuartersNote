package org.threeQuarters.ai;

import java.util.Arrays;

import org.threeQuarters.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.System;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

/**
 * Generate notes from text.
 */
public class AISummary {
    private Message systemMessage;
    private String resourcePath;

    public AISummary() throws FileNotFoundException, IOException {
        resourcePath = getClass().getClassLoader().getResource("").getPath().substring(1);
        systemMessage = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(ResourceUtils.readFileAsText(resourcePath + "prompt.txt"))
                .build();
    }

    /**
     * Generate notes from text.
     * Example: generate("计算机硬件基础", "test.txt", "note.md")
     * 
     * @param courseName Course name (used in the title of the generated note)
     * @param source     The source text file (absolute or relative to resource)
     * @param target     The target markdown file (absolute or relative to resource)
     * @return Note text generated.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public String generate(String courseName, String source, String target)
            throws FileNotFoundException, IOException, NoApiKeyException, InputRequiredException {
        Generation generation = new Generation();
        StringBuilder stringBuilder = new StringBuilder();
        if (courseName != null) {
            stringBuilder.append("以下文本内容来自《");
            stringBuilder.append(courseName);
            stringBuilder.append("》课程视频：");
        }
        File textFile = new File(source);
        if (!textFile.isAbsolute()) {
            source = resourcePath + source;
            textFile = new File(source);
        }
        System.out.println(source);
        if (!textFile.isFile()) {
            throw new FileNotFoundException();
        }
        File targetFile = new File(target);
        if (!targetFile.isAbsolute()) {
            target = resourcePath + target;
            targetFile = new File(target);
        }
        if (!targetFile.isAbsolute()) {
            throw new FileNotFoundException();
        }
        stringBuilder.append(ResourceUtils.readFileAsText(source));
        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(stringBuilder.toString())
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey("sk-625740cda11745a39e90cd975d7b7220")
                .model("qwen-max")
                .messages(Arrays.asList(systemMessage, userMessage))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        GenerationResult result = generation.call(param);
        String content = result.getOutput().getChoices().get(0).getMessage().getContent();
//        String content = result.getOutput().getChoices().getFirst().getMessage().getContent();
        try (FileWriter writer = new FileWriter(target)) {
            writer.write(content);
        }
        return content;
    }

    public String generate(String courseName,String source) throws NoApiKeyException, InputRequiredException {
        Generation generation = new Generation();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(source);
        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(stringBuilder.toString())
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
        try {
            AISummary aiSummary = new AISummary();
            System.out.println(aiSummary.generate("计算机硬件基础", "test.txt", "note.md"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
