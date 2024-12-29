package org.threeQuarters.AiFileAssistant;

import org.threeQuarters.util.Utils;

public class TextExtractor {

    public static boolean isSupportFileType(String path)
    {
        return Utils.isPDFFile(path) || Utils.isPowerPointFile(path) || Utils.isWordFile(path);
    }

    public static String extract(String path){
        if(Utils.isPowerPointFile(path))
        {
            return PowerPointTextExtractor.getInstance().extractText(path);
        }
        else if(Utils.isWordFile(path))
        {
            return WordTextExtracter.getInstance().extractText(path);
        }
        else if(Utils.isPDFFile(path))
        {
            return PDFTextExtractor.getInstance().extractText(path);
        }
        return "";
    }

}
