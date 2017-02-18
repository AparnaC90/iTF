package results;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import keyword.Passfail;

public class Severitychart  {  
	static int i=0,c=0;
	public void chartS() throws IOException{                

		FileInputStream chart_file_input = new FileInputStream(new File(Passfail.pf.FileName));

		XSSFWorkbook workbook = new XSSFWorkbook(chart_file_input);

		XSSFSheet sheet = workbook.getSheetAt(0);

		DefaultCategoryDataset bar_chart_dataset = new DefaultCategoryDataset();
	//	String chart_label="a";
		int chart_High=0,chart_Medium=0,chart_Low=0,chart_Critical=0;   
		
		for(int j=0; j<sheet.getLastRowNum(); j++)
		{
			try{
			if(sheet.getRow(j)!=null){
				sheet.getRow(j).getCell(16).setCellType(Cell.CELL_TYPE_STRING);	
			Cell Text = sheet.getRow(j).getCell(16);
			
			if(Text!=null){
			if(Text.getStringCellValue().contains("1-Critical")){
				chart_Critical++;
			}
			if(Text.getStringCellValue().contains("2-High")){
				chart_High++;
			}
			if(Text.getStringCellValue().contains("3-Medium")){
				chart_Medium++;

			}
			if(Text.getStringCellValue().contains("4-Low")){
				chart_Low++;

			}
			}
		bar_chart_dataset.addValue(chart_Critical,"Severity","1-Critical"); 
		bar_chart_dataset.addValue(chart_High,"Severity","2-High");
		bar_chart_dataset.addValue(chart_Medium,"Severity","3-Medium");
		bar_chart_dataset.addValue(chart_Low,"Severity","4-Low");
		
		//2D Chart
		//JFreeChart BarChartObject=ChartFactory.createBarChart("Excecution Report Based on Severity","Level","Level",bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);  
		//3D chart
		JFreeChart BarChartObject=ChartFactory.createBarChart3D("Defects By Severity","Level","No. of Defects",bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);  

		int width=440;
		int height=380; 
		ByteArrayOutputStream chart_out = new ByteArrayOutputStream();          
		ChartUtilities.writeChartAsPNG(chart_out,BarChartObject,width,height);
		int picture_id = workbook.addPicture(chart_out.toByteArray(), Workbook.PICTURE_TYPE_PNG);
		chart_out.close();
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor =new XSSFClientAnchor();
		anchor.setCol1(3);
		anchor.setRow1(60);
		XSSFPicture  my_picture = drawing.createPicture(anchor, picture_id);
		my_picture.resize();

		chart_file_input.close();               

		FileOutputStream out = new FileOutputStream(new File("./reports/Chart/DefectsBySeverityReport"+".xlsx"));
		workbook.write(out);
		out.close();            

		}}catch(Exception e){}
			}
			}
	}