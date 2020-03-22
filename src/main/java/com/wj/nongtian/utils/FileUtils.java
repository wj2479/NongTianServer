package com.wj.nongtian.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    /**
     * 获取文件的MD5加密数据
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getMd5FromFile(File file) throws IOException {
        String md5 = "";
        if (file == null)
            return md5;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            md5 = DigestUtils.md5Hex(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return md5;
    }

    /**
     * 获取文件路径名，如果重复 就重命名
     *
     * @param filePath
     * @return
     */
    public static String getFilePath(String rootPath, String filePath) {
        // 保存文件
        File file = new File(rootPath + "/" + filePath);
        if (file.exists()) {
            int index = filePath.lastIndexOf(".");
            String start = filePath.substring(0, index);
            String end = filePath.substring(index);

            String path = start + "_" + System.currentTimeMillis() + end;
            return getFilePath(rootPath, path);
        }
        return filePath;
    }
}
