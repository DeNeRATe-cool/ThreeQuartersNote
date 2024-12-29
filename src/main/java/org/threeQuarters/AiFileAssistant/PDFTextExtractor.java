package org.threeQuarters.AiFileAssistant;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFTextExtractor implements TextExtractAddon{

    private static PDFTextExtractor instance;

    private PDFTextExtractor()
    {

    }

    public static PDFTextExtractor getInstance(){
        if(instance == null)instance = new PDFTextExtractor();
        return instance;
    }


    @Override
    public String extractText(String path) {
        try (PDDocument document = PDDocument.load(new File(path))) {
            // 检查文档是否加密
            if (document.isEncrypted()) {
                throw new IOException("无法处理加密的 PDF 文档: " + path);
            }

            // 使用 PDFTextStripper 提取文字
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(document);
        }catch(IOException e){
            e.printStackTrace();
        }
        return "";
    }
}
