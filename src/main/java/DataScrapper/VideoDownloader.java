package DataScrapper;

import java.io.*;
import java.net.URL;

public class VideoDownloader {
    private static final String path = "./cache/video/";

    /*
      build ./cache/video path
     */
    static {
        File directory = new File(path);
        if (!directory.exists()) {
            if(directory.mkdirs())
                System.out.println("Directory created!");
            else {
                System.out.println("Directory failed to create...");
            }
        }
    }

    public static void downloadFile(String fileUrl, String fileName) throws IOException {
        fileName = path + fileName;
        URL url = new URL(fileUrl);
        try (InputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }
}
