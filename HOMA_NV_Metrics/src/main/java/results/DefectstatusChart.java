package results;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import wrappers.iTF;

public class DefectstatusChart extends iTF {
	static int c=0;
	
	public void chart() throws IOException
	{
		FileInputStream chart_file_input = new FileInputStream(new File(Passfail.pf.FileName));
		XSSFWorkbook workbook = new XSSFWorkbook(chart_file_input);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DefaultCategoryDataset bar_chart_dataset = new DefaultCategoryDataset();
		
		String fileName = new SimpleDateFormat("yyyy-MM-dd hhmm'.Defectstatus'").format(new Date());

		int chart_data1=0,chart_data2=0;   
		int row = sheet.getLastRowNum();

		for(int j=0; j<row; j++)
		{
		try{
			if(sheet.getRow(j)!=null){
				Cell Text = sheet.getRow(j).getCell(14);
				if(Text!=null){
				if(Text.getStringCellValue().contains("Pass")){
					chart_data1++;
				}
				if(Text.getStringCellValue().contains("Fail")){
					chart_data2++;
				}
			}
		}}catch(Exception e){}
		}
		bar_chart_dataset.addValue(chart_data1,"Status","Pass"); 
		bar_chart_dataset.addValue(chart_data2,"Status","Fail");

		//3D Chart
		JFreeChart BarChartObject=ChartFactory.createBarChart3D("Test Execution Status","Status","No. of Test cases",bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);  

		//2D Chart
		//	JFreeChart BarChartObject=ChartFactory.createBarChart("Test Execution Status","Status","No. of Test cases",bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);  

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

		FileOutputStream out = new FileOutputStream(new File("./reports/Chart/"+fileName+".xlsx"));
		workbook.write(out);
		out.close();  

		
	}}

