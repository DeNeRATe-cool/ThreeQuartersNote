package org.threeQuarters.AiFileAssistant;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.FileInputStream;
import java.io.IOException;

public class PowerPointTextExtractor implements TextExtractAddon{

    private static PowerPointTextExtractor powerPointTextExtractor;

    private PowerPointTextExtractor(){

    }
    public static PowerPointTextExtractor getInstance(){
        if(powerPointTextExtractor == null)powerPointTextExtractor = new PowerPointTextExtractor();
        return powerPointTextExtractor;
    }

    public String extractText(String filePath)
    {
        String content = "";

        try (FileInputStream fis = new FileInputStream(filePath);
             XMLSlideShow ppt = new XMLSlideShow(fis)) {


            // 遍历每张幻灯片
            for (XSLFSlide slide : ppt.getSlides()) {
//                System.out.println("Slide " + (slide.getSlideNumber()) + ":");
                content += "Slide " + (slide.getSlideNumber()) + ":\n";
                // 遍历幻灯片中的所有形状
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape textShape = (XSLFTextShape) shape;
//                        System.out.println(textShape.getText());
                        content += textShape.getText() + "\n";
                    }
                }
//                System.out.println("-------------------------");
                content += "------------------------------\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

//    public static String extractText(String filePath) {
//        String content = "";
//
//        try (FileInputStream fis = new FileInputStream(filePath);
//             XMLSlideShow ppt = new XMLSlideShow(fis)) {
//
//
//            // 遍历每张幻灯片
//            for (XSLFSlide slide : ppt.getSlides()) {
////                System.out.println("Slide " + (slide.getSlideNumber()) + ":");
//                content += "Slide " + (slide.getSlideNumber()) + ":\n";
//                // 遍历幻灯片中的所有形状
//                for (XSLFShape shape : slide.getShapes()) {
//                    if (shape instanceof XSLFTextShape) {
//                        XSLFTextShape textShape = (XSLFTextShape) shape;
////                        System.out.println(textShape.getText());
//                        content += textShape.getText() + "\n";
//                    }
//                }
////                System.out.println("-------------------------");
//                content += "------------------------------\n";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return content;
//    }

    public static void main(String[] args) {
        String pptxPath = "D:\\like_working\\2024_autumn\\javaBigHomeWork\\ThreeQuartersNote\\cache\\PPT\\01 绪论_计算机硬件基础（2024秋）.pptx"; // 替换为你的PPTX文件路径

    }
}
