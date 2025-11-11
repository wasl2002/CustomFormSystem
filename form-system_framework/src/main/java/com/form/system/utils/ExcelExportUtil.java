package com.form.system.utils;

import com.alibaba.fastjson.JSONObject;
import com.form.system.entity.FormField;
import com.form.system.formatter.FieldFormatterManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Excel导出工具类
 */
@Component
public class ExcelExportUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);
    
    // 注入字段格式化器管理器
    private static FieldFormatterManager fieldFormatterManager;
    
    @Autowired
    public void setFieldFormatterManager(FieldFormatterManager fieldFormatterManager) {
        ExcelExportUtil.fieldFormatterManager = fieldFormatterManager;
    }
    
    /**
     * 导出数据到Excel文件
     * 
     * @param data 数据列表
     * @param headers 表头映射（字段名 -> 显示名称）
     * @param formName 表单名称
     * @param fileName 文件名
     * @param response HTTP响应
     */
    public static void exportToExcel(List<JSONObject> data, 
                                   Map<String, FormField> headers,
                                   String formName,
                                   String fileName, 
                                   HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            
            // 处理文件名编码
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName + ".xlsx");
            
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("数据");
            
            // 创建表单名称样式
            CellStyle titleStyle = createTitleStyle(workbook);
            
            // 创建下载时间样式
            CellStyle timeStyle = createTimeStyle(workbook);
            
            // 创建表头样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // 创建表单名称行（第一行）
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(formName);
            titleCell.setCellStyle(titleStyle);
            
            // 创建下载时间行（第二行）
            Row timeRow = sheet.createRow(1);
            Cell timeCell = timeRow.createCell(0);
            String downloadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            timeCell.setCellValue("下载时间: " + downloadTime);
            timeCell.setCellStyle(timeStyle);
            
            // 创建表头行（第三行）
            Row headerRow = sheet.createRow(2);
            int cellIndex = 0;
            List<FormField> collect = headers.values().stream().sorted(Comparator.comparing(e -> e.getSortOrder())).collect(Collectors.toList());
            for (FormField entry : collect) {
                Cell cell = headerRow.createCell(cellIndex++);
                cell.setCellValue(entry.getFieldName());
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据（从第四行开始）
            int rowIndex = 3;
            for (JSONObject rowData : data) {
                Row row = sheet.createRow(rowIndex++);
                cellIndex = 0;
                for (FormField entry : collect) {
                    Cell cell = row.createCell(cellIndex++);
                    Object value = rowData.get(entry.getFieldKey());
                    // 使用字段格式化方法格式化值
                    Object formattedValue = fieldFormatterManager.formatFieldValue(value, entry);
                    setCellValue(cell, formattedValue);
                }
            }
            
            // 合并表单名称单元格（跨所有列）
            if (!headers.isEmpty()) {
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, headers.size() - 1));
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, headers.size() - 1));
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
                // 设置最小列宽
                int width = sheet.getColumnWidth(i);
                if (width < 20 * 256) { // 最小20个字符宽度
                    sheet.setColumnWidth(i, 20 * 256);
                }
                // 设置最大列宽
                if (width > 50 * 256) { // 最大50个字符宽度
                    sheet.setColumnWidth(i, 50 * 256);
                }
            }
            
            // 写入响应流
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            
        } catch (IOException e) {
            logger.error("导出Excel文件失败", e);
            throw new RuntimeException("导出Excel文件失败", e);
        }
    }
    
    /**
     * 创建表单名称样式
     */
    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    /**
     * 创建下载时间样式
     */
    private static CellStyle createTimeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }
    
    /**
     * 创建表头样式
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    /**
     * 设置单元格值
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
        
        // 设置单元格样式
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        // 设置自动换行
        style.setWrapText(true);
        cell.setCellStyle(style);
    }
}