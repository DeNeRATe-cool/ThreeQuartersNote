package org.threeQuarters.AiFileAssistant;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class WordTextExtracter implements TextExtractAddon{
    private static WordTextExtracter instance;

    private WordTextExtracter wordTextExtracter;

    public static WordTextExtracter getInstance()
    {
        if(instance == null)instance = new WordTextExtracter();
        return instance;
    }

    /**
     * 提取 Word 文档中的文字内容。
     *
     * @param path Word 文件路径
     * @return 文档中的文字内容
     */

    @Override
    public String extractText(String path) {
        // 判断文件类型
        if (path.endsWith(".doc")) {
            // 处理 .doc 格式
            try (FileInputStream fis = new FileInputStream(path);
                 HWPFDocument document = new HWPFDocument(fis)) {
                WordExtractor extractor = new WordExtractor(document);
                return extractor.getText();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        } else if (path.endsWith(".docx")) {
            // 处理 .docx 格式
            try (FileInputStream fis = new FileInputStream(path);
                 XWPFDocument document = new XWPFDocument(fis)) {
                StringBuilder text = new StringBuilder();
                List<XWPFParagraph> paragraphs = document.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    text.append(paragraph.getText()).append(System.lineSeparator());
                }
                return text.toString();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("不支持的文件格式: " + path);
        }
        return "";
    }

    public static void main(String[] args) {
        String path = "D:\\like_working\\2024_autumn\\2024_autumn\\Lisan\\Key\\7.doc";
        System.out.println(WordTextExtracter.getInstance().extractText(path));
    }
    
}
