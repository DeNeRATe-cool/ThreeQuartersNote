package org.threeQuarters.ai;


import lombok.Getter;
import lombok.Setter;

public class Question {
    @Getter
    @Setter
    private int questionNumber;
    @Getter
    @Setter
    private String questionText;
    @Getter
    @Setter
    private String[] options = new String[4];
    @Getter
    @Setter
    private String correctAnswer;
    @Getter
    @Setter
    private String explanation;

    @Getter
    @Setter
    private int questionNumberCut;
    @Getter
    @Setter
    private String questionTextCut;
    @Getter
    @Setter
    private String[] optionsCut = new String[4];
    @Getter
    @Setter
    private String correctAnswerCut;
    @Getter
    @Setter
    private String explanationCut;

    public Question(int questionNumber, String questionText, String[] options, String correctAnswer, String explanation) {
        this.questionNumber = questionNumber;
        this.questionNumberCut = questionNumber;
        this.questionTextCut = splitLongSentence(questionText,20);
        this.questionText = questionText;
        this.options = options;
        for(int i = 0;i < options.length;i++)options[i] = splitLongSentence(options[i],20);
        this.optionsCut = options;
        this.correctAnswerCut = correctAnswer;
        this.correctAnswer = correctAnswer;
        this.explanationCut = splitLongSentence(explanation, 20);
        this.explanation = explanation;
    }

    public static String splitLongSentence(String ori,int maxv)
    {
        StringBuilder sb = new StringBuilder(ori);
        for(int i = maxv;i < sb.length();i+=maxv)
        {
            sb.insert(i,"\n");
        }
        return new String(sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("# 题目 ").append(questionNumber).append(": ").append(questionText).append("\n");
        sb.append("A. ").append(options[0]).append("\n");
        sb.append("B. ").append(options[1]).append("\n");
        sb.append("C. ").append(options[2]).append("\n");
        sb.append("D. ").append(options[3]).append("\n");
        sb.append("正确答案：").append(correctAnswer).append("\n");
        sb.append("解析：").append(explanation).append("\n");
        return sb.toString();
    }



}
