package com.buzzybees.master.beehives.export;

import jakarta.persistence.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    public static List<String> extractColumnNames(Class<?> entityClass) {
        List<String> columnNames = new ArrayList<>();

        for (Field field : entityClass.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                if (!column.name().isEmpty()) {
                    columnNames.add(column.name());
                } else {
                    columnNames.add(field.getName());
                }
            }
        }

        return columnNames;
    }
    public ByteArrayInputStream exportToExcel(List<String> header, List<List<String>> data) {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                XSSFWorkbook workbook = new XSSFWorkbook()
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Data");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < header.size(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(header.get(col));
                cell.setCellStyle(headerCellStyle);
            }

            CellStyle bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setFont(workbook.createFont());


            // Body
            for (int i = 0; i < data.size(); i++) {
                List<String> dataRow = data.get(i);
                Row bodyRow = sheet.createRow(i + 1);
                for (int col = 0; col < dataRow.size(); col++) {
                    Cell cell = bodyRow.createCell(col);
                    cell.setCellValue(dataRow.get(col));
                    cell.setCellStyle(bodyCellStyle);
                }
            }


            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel", e);
        }
    }
}