package base;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataProvider {

	XSSFWorkbook wb;

	public ExcelDataProvider() {
		File src = new File("./Test Data/Data.xlsx");
		try {
			FileInputStream fis = new FileInputStream(src);
			wb = new XSSFWorkbook(fis);

		} catch (Exception e) {
			System.out.println("Unable to Read the Excel File"+ e.getMessage());
		}
	}

	public String getStringData(String sheetName, int row, int col) {
		XSSFRichTextString celData = wb.getSheet(sheetName).getRow(row).getCell(col).getRichStringCellValue();
		return celData.toString();
	}


	public int getNumbericData(String sheetName, int row, int col) {
		int celData = (int) wb.getSheet(sheetName).getRow(row).getCell(col).getNumericCellValue();
		return celData;

	}

}
