package PPTExtracter;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.FileInputStream;
import java.io.IOException;

public class PowerPointTextExtractor {

    public static void extractTextFromPPTX(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             XMLSlideShow ppt = new XMLSlideShow(fis)) {

            // 遍历每张幻灯片
            for (XSLFSlide slide : ppt.getSlides()) {
                System.out.println("Slide " + (slide.getSlideNumber()) + ":");

                // 遍历幻灯片中的所有形状
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape textShape = (XSLFTextShape) shape;
                        System.out.println(textShape.getText());
                    }
                }
                System.out.println("-------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String pptxPath = "D://like_working//2024_autumn//computer_hardware//slides//05-2 MIPS处理器设计(2)-2024秋.pptx"; // 替换为你的PPTX文件路径
        extractTextFromPPTX(pptxPath);
    }
}
