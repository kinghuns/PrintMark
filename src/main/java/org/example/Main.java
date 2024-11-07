package org.example;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.Texts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        //System.out.println("Hello world!");
        String pathTemplate = "G:\\Dev\\shippingmark\\template\\";
        String pathMarkOutput = "G:\\Dev\\shippingmark\\output\\";

//        String message = "Hello there!";
//        logger.trace(message);
//        logger.debug(message);
//        logger.info(message);
//        logger.warn(message);
//        logger.error(message);
//        logger.fatal(message);

       //MapTemplate();
//        CreateMarkUtil.CreateShipMarkByProduct(9);
//        CreateMarkUtil.CreateShipMarkByProduct(10);
//        CreateMarkUtil.CreateShipMarkByProduct(17);
//        CreateMarkUtil.CreateShipMarkByProduct(11);
//        CreateMarkUtil.CreateShipMarkByProduct(12);
//        CreateMarkUtil.CreateShipMarkByProduct(13);
//        CreateMarkUtil.CreateShipMarkByProduct(14);
//        CreateMarkUtil.CreateShipMarkByProduct(15);
//        CreateMarkUtil.CreateShipMarkByProduct(16);
    }

    public static void TestXWPFTemplate(){
        String pathMarkOutput = "G:\\Dev\\shippingmark\\output\\";
        XWPFTemplate template = XWPFTemplate.compile("G:\\Dev\\shippingmark\\template\\Mark-Template-02.docx");
        //封装模型数据
        HashMap<String, Object> map = new HashMap<>();

        map.put("itemName","test");
//        map.put("itemCode",markInfo[1]);
//        map.put("boxCode",boxNo);
//        map.put("count",markInfo[7]);
//        map.put("grossWeight",markInfo[10]);
//        map.put("netWeight",markInfo[9]);
//        map.put("size",markInfo[8]);

        //渲染数据
        template.render(map);

//        File file = new File(tmpDir); //以某路径实例化一个File对象
//        if (!file.exists()){ //如果不存在
//            boolean dr = file.mkdirs(); //创建目录
//        }
        String fileName = String.valueOf(System.currentTimeMillis())+".docx";
        //以文件形式输出
        try {
                template.writeToFile(pathMarkOutput +"\\" + fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            LocalDate date = LocalDate.now();
            System.out.println("Document:" + fileName + " had been created. " + date);

        }
    }




//    public static void MergeTest(){
//        //new一个list 模拟要合并的word对象集合
//        List<File> docFileList = new ArrayList<>();
//        //String seqWithStart = String.valueOf(i);
//        for (int i = 79; i<=180; i++ ){
//            String seqWithStart = String.valueOf(i);
//            String pathName = "G:\\Dev\\shippingmark\\data\\H\\H-" + seqWithStart+ ".docx";
//            docFileList.add(new File(pathName));
//        }
////        docFileList.add(new File("G:\\Dev\\shippingmark\\data\\1.docx"));
////        docFileList.add(new File("G:\\Dev\\shippingmark\\data\\2.docx"));
////        docFileList.add(new File("G:\\Dev\\shippingmark\\data\\3.docx"));
////        docFileList.add(new File("G:\\Dev\\shippingmark\\data\\4.docx"));
//
//        //合并之后doc存储路径 此处读的配置文件的存储路径 D:/pdfData/
//        String docPath = "G:\\Dev\\shippingmark\\data\\H\\";
//        //当前日期+UUID作为文件名防止重复
//        String fileName = LocalDate.now() + "-" + UUID.randomUUID().toString().replaceAll("-", "");
//        //合并之后doc存储路径
//        String mergeDocUrl = docPath+fileName+".docx";
//        //转成file对象
//        File mergeDocFile = new File(mergeDocUrl);
//
//        //合并doc
//        try {
//            MergeFiles.mergeDoc(docFileList,mergeDocFile);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("合并word成功");
//
//    }

