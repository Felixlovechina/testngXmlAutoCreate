package org.testng.util;

/**
 * Created by lixiaoli on 2014/12/4.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilFile {

    public static void main(String argv[]) {

        getFilesWithOutDir("/Users/felix/Downloads/test");
    }

    public static List<File> getFilesWithOutDir_spring(String path) {


        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<File> files_re = new ArrayList<File>();
        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                if (file1.isFile()) {
                    files_re.add(file1);
                }
                else
                    if (file1.isDirectory()) {
                        files_re.addAll(getFilesWithOutDir_spring(file1.getAbsolutePath()));
                    }
            }
            Collections.sort(files_re, new FilePathComparator());
        }else{
            files_re.add(file);
        }

        return files_re;
    }

    public static List<File> getFilesWithOutDir(String path) {


        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<File> files_re = new ArrayList<File>();

        for (File file1 : file.listFiles()) {
            if (file1.isFile()) {
                files_re.add(file1);
            }
        }
        Collections.sort(files_re, new FilePathComparator());
        return files_re;
    }

    public static List<File> getDirsWithOutFiles(String path) {

        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<File> filesList = java.util.Arrays.asList(file.listFiles());
        List<File> dirList = new ArrayList<File>();
        for (File file1 : filesList) {
            if (file1.isDirectory()) {
                dirList.add(file1);
            }
        }
        return dirList;
    }

    public static List getFiles(String path) {

        File file = new File(path);
        List<File> filesList = java.util.Arrays.asList(file.listFiles());
        return filesList;
    }

    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }


    public static String readTxtFile(String filePath) {
        System.out.println("filePath = " + filePath);
        return readTxtFileReturn(filePath, "");
    }

    public static String readTxtFileReturn(String filePath, String returnn) {
        if (filePath != null) {

//        System.out.println("文件位置:" + filePath);
            try {

                File file = new File(filePath);
                return readTxtFileReturn(file, returnn);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" read false " + filePath);
                return "";
            }
        }
        else {
            return "";
        }

    }

    public static String readTxtFileReturn(File filePath, String returnn) {
        StringBuffer returnStr = new StringBuffer();
        try {
            String encoding = "utf-8";
            File file = filePath;
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = "";
                long l1 = System.currentTimeMillis();
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    returnStr.append(lineTxt).append(returnn);
                }

                long l2 = System.currentTimeMillis();
//                System.out.println("读文件花费的时间：" + (l2 - l1));
                read.close();
            }
            else {
                System.err.println("not find file :" + file.getAbsolutePath());
//                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("read fail ");
            e.printStackTrace();
            System.exit(1);
        }
        return returnStr.toString();
    }


    public static String readTxtFileReturn_without_comments(File filePath, String returnn) {
        StringBuffer returnStr = new StringBuffer();
        try {
            String encoding = "utf-8";
            File file = filePath;

            String lineTxt = "";
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                long l1 = System.currentTimeMillis();
//				while ((lineTxt = bufferedReader.readLine()) != null) {
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    lineTxt = lineTxt.trim();
                    if (!(lineTxt.startsWith("//") || lineTxt.startsWith("/*") || lineTxt.startsWith("*"))) {
                        returnStr.append(lineTxt).append(returnn);
                    }
                }

                long l2 = System.currentTimeMillis();
//                System.out.println("读文件花费的时间：" + (l2 - l1));
                read.close();
            }
            else {
                System.err.println("not find " + file.getAbsolutePath());
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("read fail ");
            e.printStackTrace();
            System.exit(1);
        }
        return returnStr.toString();
    }

    // 获得后缀
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        else {
            return "";
        }
    }

    public static double getDirSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            }
            else {// 如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        }
        else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }


    public static void writeStrToFile(String s, String fileName, boolean append) {
        FileWriter fw = null;
        File f = new File(fileName);
//        System.out.println("start write" + f.getAbsolutePath());
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f, append);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(s, 0, s.length());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("end   write" + f.getAbsolutePath());
    }

    public static void fileExist(String fileName) {
        File f = new File(fileName);

        if (!f.getParentFile().exists()) {
            System.out.println(f.getParentFile());
            f.getParentFile().mkdirs();

//					f.createNewFile();
        }

    }

    // 自定义比较器：按路径排序
    static class FilePathComparator implements Comparator {
        public int compare(Object object1, Object object2) {// 实现接口中的方法
            File p1 = (File) object1; // 强制转换
            File p2 = (File) object2;
            return new String(p1.getAbsolutePath()).compareTo(new String(p2.getAbsolutePath()));
        }
    }
}
