package results;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.jfree.data.general.DefaultPieDataset;

import keyword.Passfail;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ChartUtilities;

public class DefectstatususingPie {  
	
	static int c=0;
	
	public void ChartP() throws IOException{                

		FileInputStream chart_file_input = new FileInputStream(new File(Passfail.pf.FileName));
		XSSFWorkbook workbook = new XSSFWorkbook(chart_file_input);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DefaultPieDataset pie_chart_data = new DefaultPieDataset();
		
		String fileName = new SimpleDateFormat("yyyy-MM-dd hhmm'.DefectstatusreportinPie'").format(new Date());
		 
		String chart_label="";
		int chart_data=0,chart_Pass=0,chart_Fail=0,chart_Skip=0 ;
				
		int[] IntArray = new int[2];
		String[] StringArray = new String[2];
		
		for(int j=0; j<sheet.getLastRowNum();j++)
		{
			try{
			if(sheet.getRow(j)!= null){
			Cell Text = sheet.getRow(j).getCell(14);
			if(Text!=null){
			if(Text.getStringCellValue().contains("Pass")){
				chart_Pass++;
				
			}
			if(Text.getStringCellValue().contains("Fail")){
				chart_Fail++;
			}
			StringArray[0]="Pass";
			StringArray[1]="Fail";
			
			IntArray[0]=chart_Pass;
			IntArray[1]=chart_Fail;
			}
		}

		}catch(Exception e){}
		}
		for(int j=0; j<IntArray.length;j++){
			chart_data=IntArray[j];
			chart_label=StringArray[j];
		pie_chart_data.setValue(chart_label,chart_data);
		}
	
	     //2D Chart         
	//	JFreeChart PieChart=ChartFactory.createPieChart("Execution Based on Status",pie_chart_data,true,true,false);
		
		//3D Chart
		JFreeChart PieChart=ChartFactory.createPieChart3D("Test Execution Report", pie_chart_data, true,true,false);
		
		PiePlot plot = (PiePlot) PieChart.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}"));
		 
		int width=540; 
		int height=380; 
		float quality=1; 

		ByteArrayOutputStream chart_out = new ByteArrayOutputStream();          
		ChartUtilities.writeChartAsJPEG(chart_out,quality,PieChart,width,height);
		int my_picture_id = workbook.addPicture(chart_out.toByteArray(), Workbook.PICTURE_TYPE_JPEG);                

		chart_out.close();

		XSSFDrawing drawing = sheet.createDrawingPatriarch();

		ClientAnchor anchor = new XSSFClientAnchor();

		anchor.setCol1(4);
		anchor.setRow1(30);

		XSSFPicture  my_picture = drawing.createPicture(anchor, my_picture_id);

		my_picture.resize();

		chart_file_input.close();               

		FileOutputStream out = new FileOutputStream(new File("./reports/Chart/"+fileName+".xlsx"));
		workbook.write(out);
		out.close();            
	}
}