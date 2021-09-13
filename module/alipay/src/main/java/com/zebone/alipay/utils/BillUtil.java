package com.zebone.alipay.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.apache.commons.io.FileUtils.copyURLToFile;

public class BillUtil {

    public static List<String> downloadBill(String url) throws Exception {
        String rootPath = System.getProperty("user.dir");
        URL httpurl = new URL(url);
        String fileName = "bill.zip";
        String zipFileName = rootPath + File.separator+"alipayBill"+File.separator + fileName;
        File f = new File(zipFileName);
        copyURLToFile(httpurl, f);
        String descFileName = rootPath+ File.separator+"alipayBill";
        List<String> fileNames = new ArrayList<>();
        String descFileNames = descFileName;
        if (!descFileNames.endsWith(File.separator)) {
            descFileNames = descFileNames + File.separator;
        }
        // 根据ZIP文件创建ZipFile对象
        ZipFile zipFile = new ZipFile(zipFileName,Charset.forName("GBK"));
        ZipEntry entry = null;
        String entryName = null;
        String descFileDir = null;
        byte[] buf = new byte[4096];
        int readByte = 0;
        // 获取ZIP文件里所有的entry
        Enumeration enums = zipFile.entries();
        // 遍历所有entry
        while (enums.hasMoreElements()) {
            entry = (ZipEntry) enums.nextElement();
            // 获得entry的名字
            entryName = entry.getName();
            descFileDir = descFileNames + entryName;
            fileNames.add(descFileDir);
            if (entry.isDirectory()) {
                // 如果entry是一个目录，则创建目录
                new File(descFileDir).mkdirs();
                continue;
            } else {
                // 如果entry是一个文件，则创建父目录
                new File(descFileDir).getParentFile().mkdirs();
            }
            File file = new File(descFileDir);
            // 打开文件输出流
            OutputStream os = new FileOutputStream(file);
            // 从ZipFile对象中打开entry的输入流
            InputStream is = zipFile.getInputStream(entry);
            while ((readByte = is.read(buf)) != -1) {
                os.write(buf, 0, readByte);
            }
            os.close();
            is.close();
        }
        zipFile.close();
        return fileNames;
    }
}
