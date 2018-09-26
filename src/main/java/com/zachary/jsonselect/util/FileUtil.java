package com.zachary.jsonselect.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * file util
 *
 * @author zachary
 */
public class FileUtil {
    private final static String LINE_SEPARATOR = "\n";

    /**
     * 读取文件，按行输出到list中返回
     *
     * @param pathname
     * @return
     * @throws IOException
     */
    public static List<String> getRows(String pathname) throws IOException {
        File file = new File(pathname);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        List<String> fileRows = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            fileRows.add(line);
        }
        br.close();
        return fileRows;
    }

    /**
     * 读取文件内容，行分割符\n拼接，返回结果
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getContent(String filePath) throws IOException {
        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        return getContent(fr);
    }

    public static String getContent(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);
        return getContent(reader);
    }

    public static String getContent(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line + LINE_SEPARATOR);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 重命名文件,renameTo函数失效
     *
     * @param file1
     * @param file2
     */
    public static void reNameFile(File file1, File file2) {
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(file1);
            fos = new FileOutputStream(file2);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fis.close();
            if (file1.isFile() && file1.exists()) {
                file1.getAbsoluteFile().delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写字符串到文件中
     *
     * @param filepath
     * @param content
     * @param append
     */
    public static void writeFile(String filepath, String content, boolean append) {
        File file = new File(filepath);
        writeFile(file, content, append);
    }

    /**
     * @param file
     * @param content
     * @param append
     */
    public static void writeFile(File file, String content, boolean append) {
        if (file == null) {
            return;
        }
        try {
            if (!file.exists() || !file.isFile()) {
                File dir = new File(file.getParent());
                // 路径不存在，则创建路径
                if (!dir.exists() || !dir.isDirectory()) {
                    dir.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream out = null;
            // 如果追加方式用true
            if (append) {
                out = new FileOutputStream(file, true);
            } else {
                out = new FileOutputStream(file, false);
            }
            StringBuffer sb = new StringBuffer();
            sb.append(content + LINE_SEPARATOR);
            // 注意需要转换对应的字符集
            out.write(sb.toString().getBytes("utf-8"));
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
