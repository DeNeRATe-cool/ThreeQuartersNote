package org.threeQuarters.ai;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import javafx.application.Platform;
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.util.MessageBox;
import org.threeQuarters.util.ResourceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AIExam {
    private Message systemMessage;
    private String resourcePath;
    private String ask;

    public AIExam() throws IOException {
        resourcePath = getClass().getClassLoader().getResource("").getPath().substring(1);
//        getClass().getResource("/images/configure.png")

        systemMessage = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(ResourceUtils.readFileAsText(resourcePath + "prompt_exam.txt"))
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

    public static List<Question> parseQuestions(String input) {
        List<Question> questions = new ArrayList<>();
        String[] parts = input.split("\n");

        int questionCount = Integer.parseInt(parts[0].split(" ")[0]); // 提取题目数量
        int index = 1;

        try{
            for (int i = 0; i < questionCount; i++) {
                // 题面
                while(parts[index++].equals("\n"));
                String questionText = parts[index++];
                // 选项
                String[] options = new String[4];
                options[0] = parts[index++].substring(3); // A.
                options[1] = parts[index++].substring(3); // B.
                options[2] = parts[index++].substring(3); // C.
                options[3] = parts[index++].substring(3); // D.

                // 正确答案
                String correctAnswer = parts[index++];

                // 解析
                String explanation = parts[index++];

                // 创建题目对象
                Question question = new Question(i + 1, questionText, options, correctAnswer, explanation);
                questions.add(question);
            }
        }catch (StringIndexOutOfBoundsException e)
        {
            Platform.runLater(()->{
                new MessageBox("","","生成过程中出现了错误，请重新生成");
            });
        }


        return questions;
    }

    public static List<Question> getQuestionAccordingEditingFile()
    {
        try {
            if(FileManager.getInstance().getEditingFileTab() == null)return null;
            String content = FileManager.getInstance().getEditingFileTab().getTextContent();
            AIExam aiExam = new AIExam();
            String input = aiExam.ans(content);
            System.out.println("input\n" + input);
            List<Question>questions = parseQuestions(input);

            return questions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String content = "";
        AIExam aiExam = null;
        try {
            aiExam = new AIExam();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
//            System.out.println(aiAssistant.ans(content));
            String input = aiExam.ans(content);
            System.out.println(input);
            List<Question> questions = parseQuestions(input);
            // 输出题目
            System.out.println("一共出题 " + questions.size() + " 道：");
            for (Question q : questions) {
                System.out.println(q);
            }
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        }

//        String input = "10\n" +
//                "\n" +
//                "1. 以下哪个是建立健康关系的重要因素？\n" +
//                "A. 诚实和信任  \n" +
//                "B. 控制和占有  \n" +
//                "C. 忽视对方的感受  \n" +
//                "D. 不沟通  \n" +
//                "A\n" +
//                "诚实和信任是建立任何健康关系的基石。\n" +
//                "\n" +
//                "2. 在寻找女朋友的过程中，以下哪种行为最有可能吸引对方？\n" +
//                "A. 展示真实的自我  \n" +
//                "B. 装作自己不是的人  \n" +
//                "C. 过度炫耀财富  \n" +
//                "D. 频繁改变兴趣爱好以迎合对方  \n" +
//                "A\n" +
//                "展示真实的自我可以让对方更好地了解你，从而建立起真实的关系。\n" +
//                "\n" +
//                "3. 以下哪种方式有助于在社交场合中结识新的朋友？\n" +
//                "A. 独自待在家里  \n" +
//                "B. 参加兴趣小组或俱乐部活动  \n" +
//                "C. 拒绝与人交流  \n" +
//                "D. 总是谈论自己  \n" +
//                "B\n" +
//                "参加兴趣小组或俱乐部活动可以让你有机会遇到志同道合的人。\n" +
//                "\n" +
//                "4. 以下哪种方法可以帮助你在约会时给对方留下好印象？\n" +
//                "A. 倾听对方说话  \n" +
//                "B. 经常打断对方  \n" +
//                "C. 不停地谈论自己的成就  \n" +
//                "D. 忽略对方的意见  \n" +
//                "A\n" +
//                "倾听对方说话并表现出关心会让对方感到被尊重和重视。\n" +
//                "\n" +
//                "5. 以下哪种行为在建立关系初期最为重要？\n" +
//                "A. 经常联系对方  \n" +
//                "B. 尊重对方的个人空间  \n" +
//                "C. 过早地讨论未来计划  \n" +
//                "D. 强迫对方做出承诺  \n" +
//                "B\n" +
//                "尊重对方的个人空间可以让对方感到舒适，有助于关系的健康发展。\n" +
//                "\n" +
//                "6. 以下哪种方式有助于保持长期关系的新鲜感？\n" +
//                "A. 一起尝试新事物  \n" +
//                "B. 固定不变的生活模式  \n" +
//                "C. 从不表达感激之情  \n" +
//                "D. 避免共同活动  \n" +
//                "A\n" +
//                "一起尝试新事物可以增加彼此之间的互动和乐趣，保持关系的新鲜感。\n" +
//                "\n" +
//                "7. 以下哪种行为在处理冲突时最为有效？\n" +
//                "A. 平静地沟通  \n" +
//                "B. 大声争吵  \n" +
//                "C. 冷战  \n" +
//                "D. 批评对方  \n" +
//                "A\n" +
//                "平静地沟通可以帮助双方理解对方的观点，并找到解决问题的方法。\n" +
//                "\n" +
//                "8. 以下哪种方法有助于增强情侣间的亲密感？\n" +
//                "A. 定期进行深入对话  \n" +
//                "B. 从不分享内心感受  \n" +
//                "C. 避免身体接触  \n" +
//                "D. 不关注对方的需求  \n" +
//                "A\n" +
//                "定期进行深入对话可以增进理解和情感上的连接。\n" +
//                "\n" +
//                "9. 以下哪种行为有助于在恋爱关系中保持独立性？\n" +
//                "A. 保留个人兴趣和爱好  \n" +
//                "B. 放弃所有个人时间  \n" +
//                "C. 完全依赖对方  \n" +
//                "D. 不再发展个人社交圈  \n" +
//                "A\n" +
//                "保留个人兴趣和爱好可以让双方都有独立的空间和个人成长的机会。\n" +
//                "\n" +
//                "10. 以下哪种方法有助于在分手后恢复心理健康？\n" +
//                "A. 寻求朋友和家人的支持  \n" +
//                "B. 沉浸在悲伤中  \n" +
//                "C. 避免面对现实  \n" +
//                "D. 不断骚扰前任  \n" +
//                "A\n" +
//                "寻求朋友和家人的支持可以帮助你更快地走出失恋的阴影，恢复心理健康。\n";



    }
}

