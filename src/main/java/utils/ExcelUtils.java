package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/TestData.xlsx";
    
    public static Object[][] getTestData(String sheetName) {
        try (FileInputStream fis = new FileInputStream(TEST_DATA_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();
            
            Object[][] data = new Object[rowCount][1];
            
            // Get headers
            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < colCount; i++) {
                headers.add(getCellValueAsString(headerRow.getCell(i)));
            }
            
            // Get data
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < colCount; j++) {
                    String header = headers.get(j);
                    String value = getCellValueAsString(row.getCell(j));
                    rowData.put(header, value);
                }
                
                data[i-1][0] = rowData;
            }
            
            return data;
            
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }
    
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    public static String getCellData(String filePath, String sheetName, int rowIndex, String columnName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            int colNum = -1;
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                    colNum = cell.getColumnIndex();
                    break;
                }
            }
            if (colNum == -1) throw new RuntimeException("Column not found: " + columnName);
            Row dataRow = sheet.getRow(rowIndex);
            return dataRow.getCell(colNum).getStringCellValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 