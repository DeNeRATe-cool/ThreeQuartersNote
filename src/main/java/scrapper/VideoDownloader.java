package scrapper;

import java.io.*;
import java.net.URL;

public class VideoDownloader {
    private static final String path = "./cache/video/";

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