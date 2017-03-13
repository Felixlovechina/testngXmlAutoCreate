package org.testng.util;

import com.intellij.openapi.vfs.VirtualFile;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by felix on 16/12/23.
 */
public class AutoCreatXML_VirtualFIle {
    public static void main(String[] args) {

String methodsname ="c";
        Pattern pattern = Pattern.compile("^[A-Za-z_$]+[A-Za-z_$\\d]+$");
        Matcher matcher = pattern.matcher(methodsname);

        if (matcher.find()&&matcher.group().toString().equals(methodsname)){

            // 有test注解的方法

            System.out.println(methodsname);
            System.out.println("matcher.group() = " + matcher.group());


        }
        else {
            System.out.println("matcher.group() = " + matcher.group());
            System.out.println("methodsname = " + methodsname);

        }

    }

    public static AutoCreatXML_VirtualFIle instance;


    public synchronized static AutoCreatXML_VirtualFIle getInstance() {
        if (instance == null) {
            instance = new AutoCreatXML_VirtualFIle();
        }
        return instance;
    }

    //
    public String readVirtualFile(VirtualFile[] files, String testngxmlpath) {
        StringBuffer result = new StringBuffer();
        Map<String, List<Map<String, List<String>>>> path_list = new HashMap<String, List<Map<String, List<String>>>>();

        //从flag往后，是java类的import路径

        Document document = DocumentHelper.createDocument();
        document.addDocType("suite", null, "http://testng.org/testng-1.0.dtd");
        Element root = document.addElement("suite").addAttribute("name", "All Test Suite");
        String xmlFileName = "testng.xml";
        String outtpath = testngxmlpath + "/" + xmlFileName;
        for (int i = 0; i < files.length; i++) {
            String path = files[i].getPath();


            //获取test名称
//            String testname = path.replaceAll("/", "_").trim();
            String testname = path;

            //获取文件保存位置
            //创建根节点suite，并设置name属性为xmlsuitename

            //创建节点test，并设置name、verbose属性
            Element test = root.addElement("test")

                    //记录日志信息的详细程度，有0-10个级别，0是没有，10是最详细，对输出的测试报告无影响
                    .addAttribute("verbose", "2")

                    //控制@Test标识的测试用例执行顺序，默认是false，在节点下面的所有方法的执行顺序是无序的
                    //把它设为true以后就能保证在节点下的方法是按照顺序执行的。
                    .addAttribute("preserve-order", "true")
                    .addAttribute("name", testname);

            //创建节点classes，无属性
            Element classes = test.addElement("classes");
            List<File> fiList = UtilFile.getFilesWithOutDir_spring(path);
            if (fiList == null) {
                System.out.println(" no files");
                return null;
            }
            for (File f : fiList
                    ) {
                System.out.println(f.getName());
                if (f.getName().endsWith(".java")) {
                    String class_name = f.getName().substring(0, f.getName().indexOf(".java"));
                    String fileContain = UtilFile.readTxtFileReturn_without_comments(f, "").trim();
                    List<String> methodsList = new ArrayList<String>();
                    // FIXME: felix 17/2/17 这里的第二个括号，可能是description，需要考虑塞到xml中
                    Pattern p = Pattern.compile("(@Test)(.+?)(\\{)");
                    Matcher m = p.matcher(fileContain);
                    while (m.find()) {
                        String t1 = m.group(2);
                        String t2 = t1.substring(0, t1.lastIndexOf("(")).trim();
                        String methodsname = t2.substring(t2.lastIndexOf(" ") + 1);

//                        ^[a-zA-Z_]+[a-zA-Z0-9_]*
//                        ^[A-Za-z_$]+[A-Za-z_$\d]+$
                        Pattern pattern = Pattern.compile("^[A-Za-z_$]+[A-Za-z_$\\d]+$");
                        Matcher matcher = pattern.matcher(methodsname);

                        if (matcher.find()&&matcher.group().toString().equals(methodsname)){

                                // 有test注解的方法
                                methodsList.add(methodsname);
                                System.out.println(methodsname);


                        }
                        else {
                            result.append("\nwarning file :").append(f.getPath()).append(" at ").append(methodsname);
                        }


                    }
                    Pattern p_2 = Pattern.compile("(package )(.+?)(;)");
                    Matcher m_2 = p_2.matcher(fileContain);
                    if (m_2.find()){
                        String packageName = m_2.group(2).trim();

                        if (methodsList.size() > 0) {
                            //创建节点classs，并设置name属性
                            Element classs = classes.addElement("class").addAttribute("name", packageName + "." + class_name);
                            //创建节点methods，无属性
                            Element methods = classs.addElement("methods");
                            //创建节点classs，并设置name属性
                            for (String testMothod : methodsList
                                    ) {
                                Element include = methods.addElement("include").addAttribute("name", testMothod);
                            }
                        }

                    }else{
                        result.append("\nwarning file :").append(f.getPath());
                    }


                }

            }

            System.out.println("#####\n#####\txml文件内容：\n\n" + document.asXML() + "\n\n#####\n#####");


//            String outtpath = Thread.currentThread().getContextClassLoader().getResource(".").getPath() + "/" + xmlFileName;

            System.out.println("\n\n#####\n#####\txml文件在：" + outtpath);
            UtilFile.writeStrToFile(document.asXML(), outtpath, false);
            result.append("\n\nTestNG XML at :\n").append(outtpath);
        }
        return  result.toString() ;

    }


}
