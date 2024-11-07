package org.example;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 参数1:需要合并的word的文件对象list
 * 参数2:合并之后word存储的全路径file对象
 */
public class MergeFiles {

    public static void mergeDoc(List<File> fileList, File newFile) throws Exception {

        OutputStream dest = new FileOutputStream(newFile);
        ArrayList<XWPFDocument> documentList = new ArrayList<XWPFDocument>();
        XWPFDocument doc = null;
        for (int i = 0; i < fileList.size(); i++) {
            FileInputStream in = new FileInputStream(fileList.get(i).getPath());
            OPCPackage open = OPCPackage.open(in);
            XWPFDocument document = new XWPFDocument(open);
            documentList.add(document);
        }

        for (int i = 0; i < documentList.size(); i++) {
            doc = documentList.get(0);
            if (i != 0) {
                //documentList.get(i).createParagraph().setPageBreak(true);//实现了分页效果。//但是会出现在首行为空的情况
//                documentList.get(i).createParagraph().createRun().addBreak(BreakType.PAGE);//现了分页效果。使用这种方式不会出现留白的情况
                appendBody(doc, documentList.get(i));
            }
        }
        doc.write(dest);//输出合并之后的文件
    }




    public static void appendBody(XWPFDocument src, XWPFDocument append) throws Exception {
        CTBody src1Body = src.getDocument().getBody();
        CTBody src2Body = append.getDocument().getBody();

        List<XWPFPictureData> allPictures = append.getAllPictures();
        // 记录图片合并前及合并后的ID
        Map<String, String> map = new HashMap();
        for (XWPFPictureData picture : allPictures) {
            String before = append.getRelationId(picture);
            //将原文档中的图片加入到目标文档中
            String after = src.addPictureData(picture.getData(), Document.PICTURE_TYPE_PNG);
            map.put(before, after);
        }

        appendBody(src1Body, src2Body, map);

    }

    private static void appendBody(CTBody src, CTBody append, Map<String, String> map) throws Exception {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);

        //去掉追加word内容中的 w:sectPr 标签，确保合成的word中只有一个 w:sectPr 标签对
        //避免合成的word文档打开之后会提示有些内容读不出来,导致文件损坏
        String rgex = "<[\\s]*?w:sectPr[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?w:sectPr[\\s]*?>";
        appendString = appendString.replaceAll(rgex, "");

        String srcString = src.xmlText();
        String regex = regex(srcString, "w:sectPr");
        System.out.println(regex);

        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String sufix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));

        if (map != null && !map.isEmpty()) {
            //对xml字符串中图片ID进行替换
            for (Map.Entry<String, String> set : map.entrySet()) {
                addPart = addPart.replace(set.getKey(), set.getValue());
            }
        }
        //将两个文档的xml内容进行拼接
        CTBody makeBody = CTBody.Factory.parse(prefix + mainPart + addPart + sufix);

        src.set(makeBody);
    }

    /**
     * 获取指定标签中的内容
     *
     * @param xml
     * @param label
     * @return
     */
    public static String regex(String xml, String label) {
        String context = "";
        // 正则表达式
        String rgex = "<" + label + "[^>]*>((?:(?!<\\/" + label + ">)[\\s\\S])*)<\\/" + label + ">";
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(xml);
        // 匹配的有多个
        List<String> list = new ArrayList<String>();
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        if (list.size() > 0) {
            // 输出内容自己定义
            context = String.valueOf(list.size());
        }
        return context;
    }

}
