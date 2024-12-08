package org.threeQuarters.util;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static ImageView createAvatarImageView(byte[] avatarBytes) {
        if (avatarBytes != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(avatarBytes);
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);

            // 设置头像显示大小
            imageView.setFitWidth(100); // 宽度
            imageView.setFitHeight(100); // 高度
            imageView.setPreserveRatio(true); // 保持宽高比
            return imageView;
        } else {
            System.out.println("Avatar bytes are null!");
            return null;
        }
    }

    public static byte[] compressToTargetSize(byte[] inputBytes, int maxSizeKB) {
        final int targetSizeBytes = maxSizeKB * 1024; // 转换为字节
        float quality = 0.8f; // 初始压缩质量
        int width = 0, height = 0;
        BufferedImage image = null;

        // 将字节数组转换为 BufferedImage
        try (ByteArrayInputStream bais = new ByteArrayInputStream(inputBytes)) {
            image = ImageIO.read(bais);
            if (image == null) {
                System.err.println("Failed to decode image.");
                return null;  // 无法解码图像，返回null
            }
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            System.err.println("Error reading image from byte array: " + e.getMessage());
            return null;  // 读取图像时出错，返回null
        }

        // 开始循环压缩
        while (true) {
            // 压缩图片质量
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality); // 设置压缩质量

                try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                    writer.setOutput(ios);
                    writer.write(null, new IIOImage(image, null, null), param);
                }
                writer.dispose();

                byte[] compressedBytes = baos.toByteArray();

                // 如果大小小于目标，返回结果
                if (compressedBytes.length <= targetSizeBytes) {
                    return compressedBytes;
                }

                // 如果质量已经降到最低，则尝试调整分辨率
                if (quality <= 0.1f) {
                    // 每次缩小分辨率 90%
                    width = (int) (width * 0.9);
                    height = (int) (height * 0.9);
                    image = resizeImage(image, width, height);

                    // 重置质量
                    quality = 0.8f;
                } else {
                    // 否则降低质量
                    quality -= 0.1f;
                }
            } catch (IOException e) {
                System.err.println("Error during image compression: " + e.getMessage());
                return null;  // 压缩过程中出现错误，返回null
            }
        }
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }


}
