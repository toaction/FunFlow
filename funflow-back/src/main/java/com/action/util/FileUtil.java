package com.action.util;

public class FileUtil {


    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }

        // 获取最后一个 '.' 之后的部分
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == filename.length() - 1) {
            return null; // 没有点，或点在最后（如 "file."）
        }

        String extension = filename.substring(lastDotIndex + 1);
        if (extension.isEmpty()) {
            return null;
        }

        // 转为小写，便于后续校验
        return extension.toLowerCase();
    }
}
