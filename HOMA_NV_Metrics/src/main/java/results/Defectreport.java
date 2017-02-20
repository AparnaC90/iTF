package results;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import keyword.Passfail;
import wrappers.iTF;

public class Defectreport extends iTF {
	
	static String data= null,data1= null,data2= null,data3= null,data4= null,data5= null,data6= null;

	public void Defect() throws IOException {
		
		FileInputStream fs = new FileInputStream(new File(Passfail.pf.FileName));
		XSSFWorkbook workbk = new XSSFWorkbook(fs);

		XSSFSheet sheet = workbk.getSheetAt(0);

		FileInputStream fs1 = new FileInputStream(new File("./reports/Defect/Defects.xlsx"));
		XSSFWorkbook workbk1 = new XSSFWorkbook(fs1);

		XSSFSheet sheet1 = workbk1.getSheetAt(0);
		int outputrow=0;
		for(int j=0 ; j<sheet1.getLastRowNum(); j++){
			if(sheet1.getRow(j)!=null) {
				Cell col1 =sheet1.getRow(j).getCell(0);
				if(!col1.getStringCellValue().isEmpty()){
					outputrow++;
				}else{
					break;
				}
			}
		}
		
		outputrow--;
		int row = sheet.getLastRowNum();
			try{		
		for(int j=0 ; j<row; j++){
			if(sheet.getRow(j)!=null) {
				//Title
			Cell col1 =sheet.getRow(j).getCell(0);
			//Test Procedure
			Cell col2 =sheet.getRow(j).getCell(2);
			//Prority
			sheet.getRow(j).getCell(6).setCellType(Cell.CELL_TYPE_STRING);
			Cell col3 =sheet.getRow(j).getCell(6);
			//severity
			Cell col5 =sheet.getRow(j).getCell(7);
			//Expected result
			Cell col6 =sheet.getRow(j).getCell(12);
			//Actual Result
			Cell col7 =sheet.getRow(j).getCell(13);
			
			Cell col4 =sheet.getRow(j).getCell(14);
			if(col1!=null && col2!=null && col3!=null && col4!=null && col5!=null && col6!=null && col7!=null) {
			data = col1.getStringCellValue();
			data1 = col2.getStringCellValue();
			
			data2 = col3.getStringCellValue();
			data4 = col5.getStringCellValue();
			data5 = col6.getStringCellValue();
			data6 = col7.getStringCellValue();
			
			data3 = col4.getStringCellValue();

			
				if(data3.contains("Fail")){
					outputrow++;
					Cell value1 =sheet1.getRow(outputrow).getCell(0);
					Cell value2 =sheet1.getRow(outputrow).getCell(1);
					
					Cell value3 =sheet1.getRow(outputrow).getCell(2);
				//	Cell value4 =sheet1.getRow(outputrow).getCell(3);
					Cell value5 =sheet1.getRow(outputrow).getCell(3);
					Cell value6 =sheet1.getRow(outputrow).getCell(4);
					Cell value7 =sheet1.getRow(outputrow).getCell(5);
					
					
					value1.setCellValue(data);
					value2.setCellValue(data1);
					value3.setCellValue(data2);
					value5.setCellValue(data4);
					value6.setCellValue(data5);
					value7.setCellValue(data6);
					
				}
			}
		}
			}}
		catch(Exception e){
			
			System.out.println("wfwefwr");
		}
		

		fs.close();
		fs1.close();

		FileOutputStream out = new FileOutputStream(new File("./reports/Defect/Defects.xlsx"));

		workbk1.write(out);	
		out.close();

	}

	}


