package results;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import wrappers.iTF;

public class TestingTypeChart  {
	static int c=0;
	
	public void chartT() throws IOException
	{
		FileInputStream chart_file_input = new FileInputStream(new File(Passfail.pf.FileName));
		XSSFWorkbook workbook = new XSSFWorkbook(chart_file_input);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DefaultCategoryDataset bar_chart_dataset = new DefaultCategoryDataset();

		int chart_Positive=0,chart_Negative=0;   
	
		for(int j=0; j<sheet.getLastRowNum(); j++)
		{
			if(sheet.getRow(j)!=null){
				Cell Text = sheet.getRow(j).getCell(4);
				if(Text!=null){
				if(Text.getStringCellValue().contains("Positive")){
					chart_Positive++;
				}
				if(Text.getStringCellValue().contains("Negative")){
					chart_Negative++;
				}
			}
		}
		}
		bar_chart_dataset.addValue(chart_Positive,"Status","Positive"); 
		bar_chart_dataset.addValue(chart_Negative,"Status","Negative");

		//3D Chart
		JFreeChart BarChartObject=ChartFactory.createBarChart3D("Test Execution Report by Testing Type","Type","No. of Test cases",bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);  

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

		FileOutputStream out = new FileOutputStream(new File("./reports/Chart/Testingtype"+c+".xlsx"));
		workbook.write(out);
		out.close();  
	}


}
