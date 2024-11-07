package org.example;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OfficeUtils {

//    public static List<String[]> readExcel(String filePath) {
//        List<String[]> dataList = new ArrayList<String[]>();
//        boolean isExcel2003 = true;
//        if (isExcel2007(filePath)) {
//            isExcel2003 = false;
//        }
//        File file = new File(filePath);
//        InputStream is = null;
//        try {
//            is = new FileInputStream(file);
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//        }
//        Workbook wb = null;
//        try {
//            wb = isExcel2003 ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        Sheet sheet = wb.getSheetAt(0);
//        int totalRows = sheet.getPhysicalNumberOfRows();
//        int totalCells = 0;
//        if (totalRows >= 1 && sheet.getRow(0) != null) {
//            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
//        }
//        for (int r = 0; r < totalRows; r++) {
//            Row row = sheet.getRow(r);
//            if (row == null) {
//                continue;
//            }
//            String[] rowList = new String[totalCells];
//            for (int c = 0; c < totalCells; c++) {
//                Cell cell = row.getCell(c);
//                String cellValue = "";
//                if (cell == null) {
//                    rowList[c] = (cellValue);
//                    continue;
//                }
//                cellValue = ConvertCellStr(cell, cellValue);
//                rowList[c] = (cellValue);
//            }
//            dataList.add(rowList);
//        }
//        return dataList;
//    }

//    private static String ConvertCellStr(Cell cell, String cellStr) {
//        switch (cell.getCellType()) {
//            case Cell.CELL_TYPE_STRING:
//                // 读取String
//                cellStr = cell.getStringCellValue().toString().replaceAll("&","&amp;");
//                break;
//            case Cell.CELL_TYPE_BOOLEAN:
//                // 得到Boolean对象的方法
//                cellStr = String.valueOf(cell.getBooleanCellValue());
//                break;
//            case Cell.CELL_TYPE_NUMERIC:
//                // 先看是否是日期格式
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    // 读取日期格式
//                    cellStr = formatTime(cell.getDateCellValue().toString());
//                } else {
//                    // 读取数字
//                    cellStr = String.valueOf(cell.getNumericCellValue());
//                }
//                break;
//            case Cell.CELL_TYPE_FORMULA:
//                // 读取公式
//                cellStr = cell.getCellFormula().toString().replaceAll("&","&amp;");
//                break;
//        }
//        return cellStr;
//    }

    private static boolean isExcel2007(String fileName) {
        return fileName.matches("^.+\\.(?i)(xlsx)$");
    }

    private static String formatTime(String s) {
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sf.parse(s);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = sdf.format(date);
        return result;
    }



}
