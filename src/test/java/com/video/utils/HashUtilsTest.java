package com.video.utils;

import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HashUtilsTest {

    @Test
    void calculateDHash() throws IOException {
        String imagePath = "E:\\Video\\src\\main\\resources\\static\\image\\274bbf218f5e52f23a9ddb47d9dd7adb\\bug.jpg";
        BufferedImage image = ImageIO.read(new File(imagePath));
        int width = 9; // 图像缩放后的宽度
        int height = 8; // 图像缩放后的高度

        // 缩放图像
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        scaledImage.getGraphics().drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0, null);

        StringBuilder hash = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
                int left = scaledImage.getRGB(x, y);
                int right = scaledImage.getRGB(x + 1, y);
                hash.append((left < right) ? "1" : "0");
            }
        }
        System.out.println(hash.toString());
    }

    @Test
    void calculateAHash() {
    }

    @Test
    void calculatePHash() {
    }

    @Test
    void calculateSimilarity() {
        String hash1 = "1000000010000000100000001000000010000000100100000000000000000001";
        String hash2 = "0000001000000010000010100010101000101010000000100000101000000000";
        String hash3 = "1000000010000000100000001000000010000000100100000000000000000001";
        // 计算汉明距离
        int hammingDistance = calculateHammingDistance(hash1, hash2);

        // 假设哈希码长度相同，这里使用哈希码长度作为位数
        int bitCount = hash1.length() ; // 一个十六进制字符代表4位二进制数

        // 计算相似度
        double similarity = ((double) (bitCount - hammingDistance) / bitCount);

        System.out.println(similarity*100+"%");
    }
    // 计算汉明距离的方法
    private static int calculateHammingDistance(String hash1, String hash2) {
        int distance = 0;

        // 假设哈希码长度相同，比较对应位上的字符差异
        for (int i = 0; i < hash1.length(); i++) {
            char char1 = hash1.charAt(i);
            char char2 = hash2.charAt(i);


            // 利用异或运算符计算不同位的数量
            int xor = Integer.parseInt(String.valueOf(char1), 16) ^ Integer.parseInt(String.valueOf(char2), 16);

            // 计算异或结果中1的位数，即汉明距离
            while (xor != 0) {
                distance += xor & 1;
                xor >>= 1;
            }
        }
        return distance;
    }

    @Test
    void imageHistogramComparison() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String path1 = "E:\\Video\\src\\main\\resources\\static\\image\\274bbf218f5e52f23a9ddb47d9dd7adb\\bug.jpg";
        String path2 = "E:\\Video\\src\\main\\resources\\static\\image\\274bbf218f5e52f23a9ddb47d9dd7adb\\Waiwai.jpg";
        Mat src_1 = Imgcodecs.imread(path1);// 图片 1
        Mat src_2 = Imgcodecs.imread(path2);// 图片 2

        Mat hvs_1 = new Mat();
        Mat hvs_2 = new Mat();
        //图片转HSV
        Imgproc.cvtColor(src_1, hvs_1, Imgproc.COLOR_BGR2HSV);
        Imgproc.cvtColor(src_2, hvs_2, Imgproc.COLOR_BGR2HSV);

        Mat hist_1 = new Mat();
        Mat hist_2 = new Mat();

        //直方图计算
        Imgproc.calcHist(Stream.of(hvs_1).collect(Collectors.toList()), new MatOfInt(0), new Mat(), hist_1, new MatOfInt(255), new MatOfFloat(0, 256));
        Imgproc.calcHist(Stream.of(hvs_2).collect(Collectors.toList()), new MatOfInt(0), new Mat(), hist_2, new MatOfInt(255), new MatOfFloat(0, 256));

        //图片归一化
        Core.normalize(hist_2, hist_2, 1, hist_2.rows(), Core.NORM_MINMAX, -1, new Mat());

        //直方图比较
        double b = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);

        System.out.println(b*100+"%");
    }
}