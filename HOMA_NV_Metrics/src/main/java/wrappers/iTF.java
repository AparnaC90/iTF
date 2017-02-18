package wrappers;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import results.Chartclass;
import results.Defectreport;
import results.PriorityChart;
import results.Severitychart;
import results.TypeChart;
import tFS.Defectcreation;
import utils.Reporter;


public class iTF extends GenericWrappers {
	
	protected String browserName;
	protected String dataSheetName;
	protected static String testCaseName;
	protected static String testDescription;
	
	@BeforeSuite
	public void beforeSuite() throws FileNotFoundException, IOException{
		
		Reporter.startResult();
	
	}

	@BeforeTest
	public void beforeTest(){
		
	}
	
	@BeforeMethod
	public void beforeMethod() throws IOException{
		Reporter.startTestCase();
		invokeApp(browserName);
	}
		
	@AfterSuite
	public void afterSuite(){
		Reporter.endResult();
	}

	@AfterTest
	public void afterTest(){
		
	}
	
	@AfterMethod
	public void afterMethod() throws IOException{
		quitBrowser();
		
		Chartclass Chartclass = new Chartclass();
		Chartclass.chart();
		
		PriorityChart priority = new PriorityChart();
		priority.chartp();
		
		Severitychart severity = new Severitychart();
		severity.chartS();
		
		TypeChart type = new TypeChart();
		type.TypeC();
		
		Defectreport defect = new Defectreport();
		defect.Defect();
		
		Defectcreation tfs = new Defectcreation();
		Defectcreation.createBugsinTFS();
	}
	
	
}
