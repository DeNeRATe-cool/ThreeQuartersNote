package org.threeQuarters.FileMaster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class myFileUtil {


    public static File createFileWithContent(String filePath, String content) throws IOException {
        // 检查文件是否以 ".md" 结尾，如果没有则添加
        if (!filePath.endsWith(".md")) {
            filePath += ".md";
        }

        // 创建 File 对象
        File file = new File(filePath);

        // 检查文件是否已经存在，如果不存在则创建
        if (!file.exists()) {
            // 创建父目录（如果不存在）
            file.getParentFile().mkdirs();
            file.createNewFile();  // 创建文件
        }

        // 写入内容到文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
        file.setWritable(true);
        System.out.println("文件已创建并写入内容：" + file.getAbsolutePath());
        return file;
    }


}
