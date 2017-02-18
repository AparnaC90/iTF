package keyword;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import wrappers.GenericWrappers;
import wrappers.iTF;

public class RunScripts extends iTF{
    
	@BeforeClass
	public void startTestCase(){
		browserName 	= "chrome";
		testCaseName 	= "UserProfile";
		testDescription = "Userprofile using Keyword framework";	
	}

	@Test
	public void runScripts() throws IOException {

		CallWrappersUsingKeyword keywords = new CallWrappersUsingKeyword();

		try {
			FileInputStream fis = new FileInputStream("./Keywords/KeywordDriver.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);	

			// get the number of rows
			int rowCount = sheet.getLastRowNum();			

			// loop through the rows
			for(int i=1; i <rowCount+1; i++){
				try {
					XSSFRow row = sheet.getRow(i);
					
				String value=	row.getCell(2).getStringCellValue();
					
					if(row.getCell(3).getStringCellValue().toLowerCase().equals("yes")){
						
						Passfail.pf.FileName="./keywords/"+row.getCell(1).getStringCellValue()+".xlsx";
						keywords.getAndCallKeyword("./keywords/"+row.getCell(1).getStringCellValue()+".xlsx");
	
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}




	}

}
