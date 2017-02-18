package wrappers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import keyword.Passfail;
import keyword.RunScripts;
import utils.Reporter;

public class GenericWrappers {

	protected static RemoteWebDriver driver;
	protected static Properties prop;
	public String sUrl,primaryWindowHandle,sHubUrl,sHubPort;
	int i=0;
	static int c=0;
	static String data= null,data1= null,data2= null,data3= null;

	//public static String strExcelFileName="";
	public String file = Passfail.pf.FileName;
	public GenericWrappers() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./config.properties")));
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			sUrl = prop.getProperty("URL");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will launch only firefox and maximise the browser and set the
	 * wait for 30 seconds and load the url
	 * @author -Karthikeyan
	 * @param url - The url with http or https
	 * 
	 */
	public boolean invokeApp(String browser) {
		boolean bReturn = false;
		try {

			/*	DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browser);
			dc.setPlatform(Platform.WINDOWS);*/
			if(browser.equalsIgnoreCase("chrome")){
				System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
				driver = new ChromeDriver();
			}else
				driver = new FirefoxDriver();

			//	driver = new RemoteWebDriver(new URL("http://"+sHubUrl+":"+sHubPort+"/wd/hub"), dc);

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(sUrl);

			primaryWindowHandle = driver.getWindowHandle();		
			Reporter.reportStep("The browser:" + browser + " launched successfully", "PASS");
			bReturn = true;

		} catch (Exception e) {
			e.printStackTrace();
			Reporter.reportStep("The browser:" + browser + " could not be launched", "FAIL");
		}
		return bReturn;
	}

	/**
	 * This method will enter the value to the text field using id attribute to locate
	 * 
	 * @param idValue - id of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author -Karthikeyan
	 * @throws IOException 
	 * @throws COSVisitorException 
	 */
	public boolean enterById(String idValue, String data) {
		boolean bReturn = false;

		try {

			driver.findElement(By.id(idValue)).clear();
			driver.findElement(By.id(idValue)).sendKeys(data);	
			Reporter.reportStep("The data: "+data+" entered successfully in field :"+idValue, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be entered in the field :"+idValue, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}


	public boolean ClearById(String idValue) {
		boolean bReturn = false;

		try {
			driver.findElement(By.id(idValue)).clear();

			Reporter.reportStep("The data cleared "+idValue, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data could not be cleared"+idValue, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}


	public boolean enterByXpath(String Xpathvalue, String data) {
		boolean bReturn = false;
		try {
			driver.findElement(By.xpath(Xpathvalue)).clear();
			driver.findElement(By.xpath(Xpathvalue)).sendKeys(data);	
			Reporter.reportStep("The data: "+data+" entered successfully in field :"+Xpathvalue, "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be entered in the field :"+Xpathvalue, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}


	public boolean  switchToLastWindow() {
		boolean bReturn = false;
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				driver.switchTo().window(winHandle);
			}
			Reporter.reportStep("Switch to Last window was successful","PASS");
			passfail(file, "Pass");
			bReturn = true;
		} catch (WebDriverException e) {
			passfail(file, "Fail");
			System.out.println("The Browser could not be found");
		} return bReturn;

	}

	public boolean acceptAlert() {
		boolean bReturn = false;
		try {
			driver.switchTo().alert().accept();
			Reporter.reportStep("The alert found successful","PASS");
			passfail(file, "Pass");
			bReturn = true;
		} catch (NoAlertPresentException e) {
			Reporter.reportStep("The alert could not be found","Fail");
			passfail(file, "Fail");
		}
		return bReturn;
	}


	public boolean  switchToParentWindow() {
		boolean bReturn = false;
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				driver.switchTo().window(winHandle);
				break;
			}
			Reporter.reportStep("Switch to primary window was successful","PASS");
			passfail(file, "Pass");
			bReturn = true;
		} catch (WebDriverException e) {
			passfail(file, "Fail");
			System.out.println("The Browser could not be found");
		} return bReturn;

	}

	/**
	 * This method will verify the title of the browser 
	 * @param title - The expected title of the browser
	 * @author -Karthikeyan
	 */
	public boolean verifyTitle(String title){
		boolean bReturn = false;
		try{
			if (driver.getTitle().equalsIgnoreCase(title)){
				Reporter.reportStep("The title of the page matches with the value :"+title, "PASS");
				passfail(file, "Pass");
				bReturn = true;
			}else
				Reporter.reportStep("The title of the page:"+driver.getTitle()+" did not match with the value :"+title, "SUCCESS");
			passfail(file, "Pass");

		}catch (Exception e) {
			Reporter.reportStep("The title did not match", "FAIL");
			passfail(file, "Fail");
		}

		return bReturn;
	}

	/**
	 * This method will verify the given text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author -Karthikeyan
	 */
	public boolean verifyTextByXpath(String xpath, String text){
		boolean bReturn = false;
		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().equalsIgnoreCase(text)){
			Reporter.reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not match with the value :"+text, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	public boolean verifyTextByXpath(String xpath, String text, String result){
		boolean bReturn = false;
		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().equalsIgnoreCase(result)){
			Reporter.reportStep("The text: "+sText+" matches with the value :"+result, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not match with the value :"+result, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	public boolean verifyTextByXpathResult(String xpath, String result) throws InterruptedException{
		boolean bReturn = false;

		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().equalsIgnoreCase(result)){
			Reporter.reportStep("The text: "+sText+" matches with the value :"+result, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not match with the value :"+result, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	/**
	 * This method will verify the given text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author -Karthikeyan
	 * @throws Throwable 
	 */
	public boolean verifyTextContainsByXpath(String xpath, String text) {
		boolean bReturn = false;

		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().contains(text)){
			Reporter.reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	public boolean verifyTextContainsByXpathwithwait(String xpath, String text)  {
		boolean bReturn = false;
		WebDriverWait wait=new WebDriverWait(driver, 25);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().contains(text)){
			Reporter.reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	public boolean verifyTextContainsById(String idvalue, String text){
		boolean bReturn = false;
		String sText = driver.findElementById(idvalue).getText();
		if (driver.findElementById(idvalue).getText().trim().contains(text)){
			Reporter.reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	public boolean verifyTextContainsByIdwithResult(String idvalue, String result){
		boolean bReturn = false;
		String sText = driver.findElementById(idvalue).getText();
		if (driver.findElementById(idvalue).getText().trim().contains(result)){
			Reporter.reportStep("The text: "+sText+" contains the value :"+result, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not contain the value :"+result, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	/**
	 * This method will close all the browsers
	 * @author -Karthikeyan
	 */
	public void quitBrowser() {
		try {
			driver.quit();
		} catch (Exception e) {
			Reporter.reportStep("The browser:"+driver.getCapabilities().getBrowserName()+" could not be closed.", "FAIL");
		}

	}

	/**
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickById(String id) {
		boolean bReturn = false;
		try{
			driver.findElement(By.id(id)).click();
			Reporter.reportStep("The element with id: "+id+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with id: "+id+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author -Karthikeyans
	 */
	public boolean clickByClassName(String classVal) {
		boolean bReturn = false;
		try{
			driver.findElement(By.className(classVal)).click();
			Reporter.reportStep("The element with class Name: "+classVal+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with class Name: "+classVal+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}
	/**
	 * This method will click the element using name as locator
	 * @param name  The name (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickByName(String name) {
		boolean bReturn = false;
		try{
			driver.findElement(By.name(name)).click();
			Reporter.reportStep("The element with name: "+name+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with name: "+name+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will click the element using link name as locator
	 * @param name  The link name (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickByLink(String name) {
		boolean bReturn = false;
		try{
			Reporter.reportStep("The element with link name: "+name+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with link name: "+name+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will click the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickByXpath(String xpathVal) {
		boolean bReturn = false;
		try{
			//Thread.sleep(6000);
			driver.findElement(By.xpath(xpathVal)).click();
			Reporter.reportStep("The element : "+xpathVal+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean clickbyCssSelector(String selector) {
		boolean bReturn = false;
		try{
			Thread.sleep(1500);
			driver.findElement(By.cssSelector(selector)).click();
			Reporter.reportStep("The element with Css selector: "+selector+" is clicked with value :", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The value: "+selector+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will mouse over on the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be moused over
	 * @author -Karthikeyan
	 */
	public boolean mouseOverByXpath(String xpathVal) {
		boolean bReturn = false;
		try{
			new Actions(driver).moveToElement(driver.findElement(By.xpath(xpathVal))).build().perform();
			Reporter.reportStep("The mouse over by xpath : "+xpathVal+" is performed.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The mouse over by xpath : "+xpathVal+" could not be performed.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will mouse over on the element using link name as locator
	 * @param xpathVal  The link name (locator) of the element to be moused over
	 * @author -Karthikeyan
	 */
	public boolean mouseOverByLinkText(String linkName) {
		boolean bReturn = false;
		try{
			new Actions(driver).moveToElement(driver.findElement(By.linkText(linkName))).build().perform();
			Reporter.reportStep("The mouse over by link : "+linkName+" is performed.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The mouse over by link : "+linkName+" could not be performed.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public String getTextByXpath(String xpathVal){
		String bReturn = "";
		try{
			return driver.findElement(By.xpath(xpathVal)).getText();

		} catch (Exception e) {
			Reporter.reportStep("The element with xpath: "+xpathVal+" could not be found.", "FAIL");
		}
		return bReturn; 
	}

	/**
	 * This method will select the drop down value using id as locator
	 * @param id The id (locator) of the drop down element
	 * @param value The value to be selected (visibletext) from the dropdown 
	 * @author -Karthikeyan
	 */
	public boolean selectById(String id, String value) {
		boolean bReturn = false;
		try{
			new Select(driver.findElement(By.id(id))).selectByVisibleText(value);
			Reporter.reportStep("The element with id: "+id+" is selected with value :"+value, "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The value: "+value+" could not be selected.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean selectByXpath(String xpath, String value) {
		boolean bReturn = false;
		try{
			new Select(driver.findElement(By.xpath(xpath))).selectByVisibleText(value);;
			Reporter.reportStep("The element with xpath: "+xpath+" is selected with value :"+value, "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The value: "+value+" could not be selected.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	//Method for write the Pass Fail Status in to Excel

	public int passfail(String fileName,String Value){

		try {
			i++;	
			FileInputStream file = new FileInputStream(new File(fileName)); 
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Cell Text = sheet.getRow(i).getCell(14);
			Text.setCellValue(Value);
			file.close();
			FileOutputStream outFile =new FileOutputStream(new File(fileName));
			workbook.write(outFile);
			outFile.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return i;
	}



	/*	public void loadObjects() throws FileNotFoundException, IOException{
		prop = new Properties();
		prop.load(new FileInputStream(new File("./object.properties")));

	}*/


	

}


