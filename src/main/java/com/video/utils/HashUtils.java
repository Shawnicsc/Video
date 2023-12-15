package com.video.utils;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author shawni
 * @Description Hash算法比较
 * @Date 2023/11/24 10:32
 * @Version 1.0
 */
public class HashUtils {
    static {
        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    //D-Hash
    public static String calculateDHash(String imagePath) throws Exception {
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
        return hash.toString();
    }
    // A-hash
    public static String calculateAHash(String imagePath) throws Exception {
        BufferedImage image = ImageIO.read(new File(imagePath));
        int width = 8;
        int height = 8;

        // 缩放图像
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        scaledImage.getGraphics().drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0, null);

        int totalValue = 0;
        StringBuilder hash = new StringBuilder();

        // 计算像素平均值
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                totalValue += scaledImage.getRGB(x, y) & 0xFF;
            }
        }
        int avgValue = totalValue / (width * height);

        // 生成哈希码
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                hash.append((scaledImage.getRGB(x, y) & 0xFF) >= avgValue ? "1" : "0");
            }
        }
        return hash.toString();
    }
    // P-Hash
    public static String calculatePHash(String imagePath) {
        Mat image = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_GRAYSCALE);
        Mat resized = new Mat();
        Size sz = new Size(32, 32);

        // 图像缩放为32x32
        Imgproc.resize(image, resized, sz);

        // 进行DCT变换
        Mat dct = new Mat();
        Core.dct(new Mat(resized, new org.opencv.core.Rect(0, 0, 32, 32)), dct);

        // 取低频区域的像素值
        int total = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                total += dct.get(y, x)[0];
            }
        }
        int avg = total / 64;

        // 计算哈希码
        StringBuilder hash = new StringBuilder();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                hash.append(dct.get(y, x)[0] > avg ? "1" : "0");
            }
        }
        return hash.toString();
    }
    // 计算结构相似性指数（SSIM）
    public static double calculateSSIM(Mat image1, Mat image2) {
        Mat image1Gray = new Mat();
        Mat image2Gray = new Mat();
        Imgproc.cvtColor(image1, image1Gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(image2, image2Gray, Imgproc.COLOR_BGR2GRAY);
        MatOfFloat ssimMat = new MatOfFloat();
        Imgproc.matchTemplate(image1Gray, image2Gray, ssimMat, Imgproc.CV_COMP_CORREL);
        Scalar ssimScalar = Core.mean(ssimMat);
        return ssimScalar.val[0];
    }

    public static double calculateHistogram(Mat image1, Mat image2) {
        // 计算直方图
        Mat hist1 = calculateHistogram(image1);
        Mat hist2 = calculateHistogram(image2);

        // 计算相似度
        final double similarity = Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CORREL);
        return similarity;
    }

    // 计算均方差（MSE）
    public static double calculateMSE(Mat image1, Mat image2) {
        Mat diff = new Mat();
        Core.absdiff(image1, image2, diff);
        Mat squaredDiff = new Mat();
        Core.multiply(diff, diff, squaredDiff);
        Scalar mseScalar = Core.mean(squaredDiff);
        return mseScalar.val[0];
    }

    // 计算峰值信噪比（PSNR）
    public static double calculatePSNR(Mat image1, Mat image2) {
        Mat diff = new Mat();
        Core.absdiff(image1, image2, diff);
        Mat squaredDiff = new Mat();
        Core.multiply(diff, diff, squaredDiff);
        Scalar mseScalar = Core.mean(squaredDiff);
        double mse = mseScalar.val[0];
        double psnr = 10.0 * Math.log10(255.0 * 255.0 / mse);
        return psnr;
    }


    private static Mat calculateHistogram(Mat image) {
        Mat hist = new Mat();

        // 设置直方图参数
        MatOfInt histSize = new MatOfInt(256);
        MatOfFloat ranges = new MatOfFloat(0, 256);
        MatOfInt channels = new MatOfInt(0);
        List<Mat> images = new ArrayList<>();
        images.add(image);

        // 计算直方图
        Imgproc.calcHist(images, channels, new Mat(), hist, histSize, ranges);

        return hist;
    }

    // 计算相似度的方法
    public static double calculateSimilarity(String hash1, String hash2) {
        // 计算汉明距离
        int hammingDistance = calculateHammingDistance(hash1, hash2);

        // 假设哈希码长度相同，这里使用哈希码长度作为位数
        int bitCount = hash1.length() ; // 一个十六进制字符代表4位二进制数

        // 计算相似度
        return ((double) (bitCount - hammingDistance) / bitCount);
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
}
