package data;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;

public class TestDataProvider {
    private static final String EXCEL_PATH = "src/test/resources/testdata/testdata.xlsx";
    private static final String SHEET_NAME = "Sheet1";

    /**
     * Gets username from the specified row.
     * @param rowIndex The row index (1-based).
     * @return The username from the specified row.
     */
    public static String getUsername(int rowIndex) {
        return getCellData(rowIndex, "Username");
    }

    /**
     * Gets email from the specified row.
     * @param rowIndex The row index (1-based).
     * @return The email from the specified row.
     */
    public static String getEmail(int rowIndex) {
        return getCellData(rowIndex, "Email");
    }

    /**
     * Gets username from row 1 (backward compatibility).
     * @return The username from row 1.
     */
    public static String getUsername() {
        return getUsername(1);
    }

    /**
     * Gets email from row 1 (backward compatibility).
     * @return The email from row 1.
     */
    public static String getEmail() {
        return getEmail(1);
    }

    private static String getCellData(int rowIndex, String columnName) {
        try (FileInputStream fis = new FileInputStream(EXCEL_PATH);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheet(SHEET_NAME);
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