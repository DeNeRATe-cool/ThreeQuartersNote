package org.threeQuarters.projects;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import javafx.application.Platform;
import org.threeQuarters.AiFileAssistant.TextExtractor;
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.ai.AISummary;
import org.threeQuarters.util.MessageBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GenerateMdFromFileWorker implements Runnable{

    private static String path;

    public GenerateMdFromFileWorker(String path)
    {
        this.path = path;
    }

    public void run()
    {
//        String content = PowerPointTextExtractor.extractText(path);
//        String content = PowerPointTextExtractor.getInstance().extractText(path);
        String content = TextExtractor.extract(path);
        String name = new File(path).getName();
//        System.out.println(content);
        AISummary aiSummary = null;
        try{
            System.out.println("start");
            aiSummary = new AISummary();
            String res = aiSummary.generate(name,content);
            System.out.println("generate finished");
            FileManager.getInstance().createNewFile(name+".md",res);
            System.out.println("finished");
            ProjectFileAiToMd.getInstance().set0Face();
        } catch (FileNotFoundException e) {
            Platform.runLater(()->{
                new MessageBox("","","生成失败");
            });
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoApiKeyException e) {
            Platform.runLater(()->{
                new MessageBox("","","生成失败");
            });
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            Platform.runLater(()->{
                new MessageBox("","","生成失败");
            });
            throw new RuntimeException(e);
        }catch(Exception e)
        {
            Platform.runLater(()->{
                new MessageBox("","","生成失败");
            });
        }finally {
            Platform.runLater(()->{
                ProjectFileAiToMd.getInstance().set0Face();
            });
        }
    }
}