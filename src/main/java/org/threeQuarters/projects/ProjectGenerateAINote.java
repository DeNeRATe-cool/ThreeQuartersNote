package org.threeQuarters.projects;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.threeQuarters.AiFileAssistant.PowerPointTextExtractor;
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.ai.AISummary;
import org.threeQuarters.ai.ASRModelType;
import org.threeQuarters.ai.SpeechRecognition;

import java.io.IOException;
import java.nio.file.Paths;

public class ProjectGenerateAINote {

    private static String videoRootPath = System.getProperty("user.dir");

    private static String pptRootPath = System.getProperty("user.dir");

    public ProjectGenerateAINote(){
        videoRootPath = Paths.get(videoRootPath,"cache","video").toString();
        pptRootPath = Paths.get(pptRootPath,"cache","PPT").toString();
    }



    public void executeVideo2Text(String filename)
    {
        SpeechRecognition speechRecognition = new SpeechRecognition();
        try{
            int exitCode = speechRecognition.transcribe(ASRModelType.TINY, Paths.get(videoRootPath, filename).toString(), Paths.get(videoRootPath, "result.txt").toString());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void executeAiSummary(String CourseName,String filename)
    {
        AISummary aiSummary = null;
        try {
            aiSummary = new AISummary();
            String content = aiSummary.generate(CourseName,Paths.get(videoRootPath.toString(),"result.txt").toString(),Paths.get(videoRootPath,"note.md").toString());
            FileManager.getInstance().createNewFile(filename+".md",content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeVideoAInote(String CourseName, String filename)
    {
        executeVideo2Text(filename);
        executeAiSummary(CourseName,filename);
    }

    public void executePPTAInote(String CourseName,String filename)
    {
//        String source = PowerPointTextExtractor.extractText(Paths.get(pptRootPath, filename).toString());
        String source = PowerPointTextExtractor.getInstance().extractText(Paths.get(pptRootPath,filename).toString());
        AISummary aiSummary = null;
        try {
            aiSummary = new AISummary();
            String content = aiSummary.generate(CourseName,source);
            FileManager.getInstance().createNewFile(filename+".md",content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ProjectGenerateAINote projectGenerateAINote = new ProjectGenerateAINote();
        projectGenerateAINote.executeVideoAInote("离散数学","离散数学（2）_张梦豪_第11周星期4第8,9节.mp4");
//        projectGenerateAINote.executePPTAInote("计算机硬件基础","01 绪论_计算机硬件基础（2024秋）.pptx");
    }

}
