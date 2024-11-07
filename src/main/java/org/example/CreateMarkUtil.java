package org.example;

import com.deepoove.poi.XWPFTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class CreateMarkUtil {

    public static String[] GetProductInfo(String model) {
        return null;

    }

    public static List<String[]> GetAllProductInfo(String filePath, String fileName) {
        List<String[]> productInfo = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath,fileName))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line;
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);
                // 打印行
                System.out.println(String.join(", ", columns) );
                productInfo.add(columns);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return productInfo;
    }

    public static Boolean CreateShipMarkByProduct(int productId){
        //return null;
        //Create Path
        /*
           1. Create Path;
           2. Get the Mark infomation;
           3. build a loop to create a serials single marks of current product;
           4. Merge all marks into a single file.
         */
        String pathTemplate = "G:\\Dev\\shippingmark\\template\\";
        String pathMarkOutput = "G:\\Dev\\shippingmark\\output\\";
        List<String[]> markInfos = GetAllProductInfo(pathTemplate,"ShippingMark-01.csv");

        System.out.println(markInfos.size());

        String[] selectedMarkInfo = null;
        if (markInfos.size() > productId) {
            selectedMarkInfo = markInfos.get(productId);
            GenSingleFileMarksByProductId(selectedMarkInfo);
            //Merge files
            int start = Integer.parseInt(selectedMarkInfo[5]);
            int end = Integer.parseInt(selectedMarkInfo[6]);


            String prefix = selectedMarkInfo[3];
//            String amount = selectedMarkInfo[4];
            String temDir = "G:\\Dev\\shippingmark\\data\\" + prefix + "-" + start+ "-" + end;
            System.out.println(temDir + " " +pathMarkOutput);
            MergeShipMark2File(temDir, pathMarkOutput);
            return true;
        }
        else
        {
            return false;
        }

        //to do:将MarkInfo和ProductInfo抽象，以其集合的方式进行操作，以替代目前的字符串数组
    }

    public static void MergeShipMark2File(String markDir, String saveDir){
        //new一个list 模拟要合并的word对象集合
        List<File> docFileList = new ArrayList<>();

        File file = new File(markDir);		//获取其file对象
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中

        for(File f:fs){					//遍历File[]数组
            if(!f.isDirectory())		//若非目录(即文件)，则打印
                docFileList.add(f);
                System.out.println(f);
        }

//        //String seqWithStart = String.valueOf(i);
//        for (int i = 79; i<=180; i++ ){
//            String seqWithStart = String.valueOf(i);
//            String pathName = "G:\\Dev\\shippingmark\\data\\H\\H-" + seqWithStart+ ".docx";
//            docFileList.add(new File(pathName));
//        }

        //合并之后doc存储路径 此处读的配置文件的存储路径 D:/pdfData/
        String docPath = saveDir;
        //当前日期+UUID作为文件名防止重复
        String fileName = LocalDate.now() + "-" + UUID.randomUUID().toString().replaceAll("-", "");
        //合并之后doc存储路径
        String mergeDocUrl = docPath+fileName+".docx";
        //转成file对象
        File mergeDocFile = new File(mergeDocUrl);

        //合并doc
        try {
            MergeFiles.mergeDoc(docFileList,mergeDocFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("合并word成功");

    }
    public static void GenSingleFileMarksByProductId(String[] productInfo ){
        String[] columns = productInfo;
        System.out.println(String.join(", ", columns));
        int start = Integer.parseInt(columns[5]);
        int end = Integer.parseInt(columns[6]);


        String prefix = columns[3];
        String amount = columns[4];
        String temDir = prefix + "-" + start+ "-" + end;

        System.out.println(start + " to " + end);
        //String.format(“%02d”, start)
        for (int i= start; i<=end; i++) {
            String seqWithStart = String.valueOf(i);
            String seq = String.format("%02d",i-start+1);
            String temFileName = prefix + "-" + String.format("%03d",i);
//            String seq = String.valueOf(i-start+1);
            CreateMark(columns, CreateBoxNo(prefix,amount,seq,seqWithStart),temFileName,temDir);
        }

    }

    static String CreateBoxNo(String prefix, String amount, String seq, String seqWithStart ) {
        return prefix + "-" + seqWithStart + ", " + seq + "/" + amount;
    }

    public static void CreateMark(String[] markInfo, String boxNo, String tmpFileName, String tmpDirectory){
        String str = System.currentTimeMillis()+"";
        String fileName = tmpFileName + ".docx";
        String tmpDir = "G:\\Dev\\shippingmark\\data\\" + tmpDirectory ;

        XWPFTemplate template = XWPFTemplate.compile("G:\\Dev\\shippingmark\\template\\Mark-Template-01.docx");
//        XWPFTemplate template = XWPFTemplate.compile("G:\\Dev\\shippingmark\\template\\Mark-Template-small.docx");
        //封装模型数据
        HashMap<String, Object> map = new HashMap<>();

        map.put("itemName",markInfo[2]);
        map.put("itemCode",markInfo[1]);
        map.put("boxCode",boxNo);
        map.put("count",markInfo[7]);
        map.put("grossWeight",markInfo[10]);
        map.put("netWeight",markInfo[9]);
        map.put("size",markInfo[8]);

        //渲染数据
        template.render(map);

        File file = new File(tmpDir); //以某路径实例化一个File对象
        if (!file.exists()){ //如果不存在
            boolean dr = file.mkdirs(); //创建目录
        }

        //以文件形式输出
        try {
            template.writeToFile(tmpDir +"\\" + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LocalDate date = LocalDate.now();
        System.out.println("Document:" + fileName + " had been created. " + date);

    }

}
