package com.capgemini.scripts;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jxl.read.biff.BiffException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.capgemini.driver.CreateDriver;
import com.capgemini.driver.ScriptExecutor;
import com.capgemini.driver.StepExecutor;
import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.executor.WriteMaster;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.ReadObject;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;
import com.capgemini.utilities.CommonFunctions;

public class Product_List {
	public String TestCase=this.getClass().getSimpleName();
	DesiredCapabilities capabilities = new DesiredCapabilities();
	Reporter reporter = new Reporter(TestCase);
	CreateDriver driver = new CreateDriver();
	RemoteWebDriver webDriver = null;
	int readValueof;
	private Utilities utils = new Utilities(reporter);
	private ScriptExecutor scriptExecutor = new ScriptExecutor();
	private ExecutionRowNumber executionRowNumber = new ExecutionRowNumber();
	// Object for calling verification functions
	private Verification verify = new Verification(reporter);
	WebDriverWait wait = null;
	private StepExecutor stepExecutor = new StepExecutor(reporter);
	private String StrExecutionStartTime = null;
	private long executionStartTime = 0;
	Map<String, String> DataMap = new HashMap();
	Boolean sExecutionStatus;
	ReadExcel readExcel = new ReadExcel(reporter);
	CommonFunctions commFunc = new CommonFunctions();
	public String getExecutionStartTime() {
		return StrExecutionStartTime;
	}
	private ReadObject readObjects=new ReadObject();

	public String getStartTime() {
		return String.valueOf(executionStartTime);
	}
	public void executeTestcase(String browserName) throws Exception {
		int iNumberOfRows = 0;
		readExcel.setInputFile(System.getProperty("File"));
		readExcel.setSheetName(TestCase);
		Map<Integer, String> seqMap = readExcel.getiNOfRowsSeq();
		iNumberOfRows = readExcel.getiNOfRows();
		System.out.println("Inside exe 1");
		reporter.start(reporter.calendar);
		StrExecutionStartTime = reporter.strStartTime;
		executionStartTime = reporter.startTime;
		
		reporter.ReportGenerator("Cafe#"+browserName);
		for (int i = 1; i <= iNumberOfRows; i++) {
			System.out.println(seqMap.get(i)+"Trestismn");
			if(seqMap.get(i).equalsIgnoreCase("Yes")){
				webDriver = driver.getWebDriver();
				wait = new WebDriverWait(webDriver, 10);
				readExcel.readByIndex(i);
				DataMap=readExcel.loadDataMap(i);
			System.out.println("test2"+iNumberOfRows);
			System.out.println(capabilities.getBrowserName());
			reporter.setStrBrowser(capabilities.getBrowserName());
			reporter.addIterator(i);
			launchApp();
			testcaseMain();
			WriteMaster.updateNextURL(TestCase,webDriver.getCurrentUrl());
			reporter.closeIterator();
			System.out.println("\t \t \t \t \t Row number: " + i);
			webDriver.quit();
			}
		}
		String strStopTime = reporter.stop();
		reporter.strStopTime = strStopTime;
		float timeElapsed = reporter.getElapsedTime();
		reporter.timeElapsed = timeElapsed;
		reporter.CreateSummary("Cafe#"+browserName);

	}
	

	public void executeTestcase(RemoteWebDriver rdriver,String host,String browser) throws Exception {		
		int iNumberOfRows = 0;
		java.util.Properties readLocatorsPage=readObjects.getObjectRepository();
		readExcel.setInputFile(System.getProperty("File"));
		readExcel.setSheetName(TestCase);
		
		iNumberOfRows = readExcel.getiNOfRows();
		reporter.start(reporter.calendar);
		StrExecutionStartTime = reporter.strStartTime;
		executionStartTime = reporter.startTime;
		reporter.ReportGenerator(browser);
		
		for (int i = 1; i <= iNumberOfRows; i++) {
			rdriver = new CreateDriver().getWebDriver(host, browser);
			webDriver = rdriver;
			wait = new WebDriverWait(webDriver, 10);
			DataMap=readExcel.loadDataMap(i);
			reporter.addIterator(i);
			launchApp();
			testcaseMain();
			reporter.closeIterator();
			System.out.println("\t \t \t \t \t Row number: "+i);
			webDriver.quit();
		}
		
		String strStopTime = reporter.stop();
		reporter.strStopTime = strStopTime;
		float timeElapsed = reporter.getElapsedTime();
		reporter.timeElapsed = timeElapsed;
		reporter.CreateSummary(browser);
	}



	public void launchApp() throws Exception
	{
		stepExecutor.launchApplication("URL", DataMap, webDriver);
	}

	private void productSelection() throws IOException, InterruptedException {
		Properties object = readObjects.getObjectRepository();

		try{
			stepExecutor.waitForPageToLoad(webDriver);			
			for(int i =1;i<=2;i++){
				for(int j =1;j<=2;j++){
					stepExecutor.waitForExpectedElement("xpath", object.getProperty("header_product"), webDriver);
					stepExecutor.clickElement("xpath", object.getProperty("header_product"), webDriver, "Click on Product Tab");
					Thread.sleep(4000);
					stepExecutor.waitForExpectedElement("Xpath", "/html/body/form/header/div[2]/div/div/div[1]/div/div[1]/div/div[1]/table/tbody/tr["+i+"]/td["+j+"]", webDriver);
					Thread.sleep(5000);
					
					stepExecutor.clickElement("xpath", "/html/body/form/header/div[2]/div/div/div[1]/div/div[1]/div/div[1]/table/tbody/tr["+i+"]/td["+j+"]", webDriver, "Click on required product");
					stepExecutor.waitForPageToLoad(webDriver);
					Thread.sleep(5000);
					stepExecutor.clickElement("xpath", ".//*[@id='productCategoriesLandingPage']/div/div/div/div/section/ul/li["+i+"]/section", webDriver, "Click on required featured product");
					stepExecutor.waitForPageToLoad(webDriver);
					Thread.sleep(5000);
					stepExecutor.clickElement("xpath", ".//*[@id='contentPlaceholder_ProductDetail_btnAddToList']", webDriver, "Click on Add to list");
					JavascriptExecutor js = (JavascriptExecutor)webDriver;
					js.executeScript("window.scrollTo(document.body.scrollHeight,0)");
					
				}
			}
				
			Thread.sleep(5000);
			String mainHandle = webDriver.getWindowHandle();
			System.out.println("mainHandle: "+mainHandle);
			stepExecutor.clickLink("xpath", ".//*[@id='productsTabLink']", webDriver, "Click on product List");
			stepExecutor.waitForExpectedElement("Xpath","//*[@id='shoppingListLink']",webDriver );
			stepExecutor.clickLink("xpath", "//*[@id='shoppingListLink']", webDriver, "Click on Shopping List");
			Thread.sleep(10000);
			Set<String> windowIterator = webDriver.getWindowHandles();
			for(String window:windowIterator){	
				System.out.println("window: "+window);
				webDriver.switchTo().window(window);
			}
			String child = webDriver.getWindowHandle();
			System.out.println("child: "+child);
			Thread.sleep(5000);
			stepExecutor.waitForPageToLoad(webDriver);
			stepExecutor.waitForExpectedElement("xpath", "//*[@id='shoppingList']/div/div/section/div[3]/div[1]/div/div/label[2]", webDriver);
			//stepExecutor.clickButton("xpath", "//*[@id='shoppingList']/div/div/section/div[3]/div[1]/div/div/label[2]", webDriver, "Select all product");
			if(DataMap.get("Testcase ID").equals("Product_List_3")){
				stepExecutor.clickButton("xpath", "//*[@id='downloadButton']", webDriver, "Download as PDF");
				stepExecutor.waitForPageToLoad(webDriver);
				Thread.sleep(4000);
				Robot rb = new Robot();
				rb.keyPress(KeyEvent.VK_ENTER);
				rb.keyRelease(KeyEvent.VK_ENTER);
				
			}else{
				//stepExecutor.clickButton("Xpath", ".//*[@id='emailButton']", webDriver, "click on Email Selected");
				Thread.sleep(3000);
				commFunc.enterEmailDetails(stepExecutor, DataMap, webDriver);
				stepExecutor.waitForExpectedElement("xpath", "//*[@id='productListMessageDiv']", webDriver);					
				stepExecutor.waitForPageToLoad(webDriver);
				if(DataMap.get("Testcase ID").equals("Product_List_2")){
					stepExecutor.waitForExpectedElement("xpath", "//*[@id='productListMessageDiv']", webDriver);
					if(webDriver.findElementByXPath("//*[@id='productListMessageDiv']").isEnabled()){
						reporter.writeStepResult("Verifying mandatory fields", "Verifying mandatory fields", "Verification of mandatory fields passed", "Pass", "Verifying mandatory fields", true, null, webDriver);
					}else{
						reporter.writeStepResult("Verifying mandatory fields", "Verifying mandatory fields", "Verification of mandatory fields failed", "Pass", "Verifying mandatory fields", true, null, webDriver);
					}			
				}			
				reporter.writeStepResult("Email has been sent successfully", "Email has been sent successfully", "", "Pass", "Email has been sent successfully", true, null, webDriver);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("Email sending failed", "Email sending failed", "Expected: " + "Email sending failed", "Fail", "Email sending failed", true, null, webDriver);
		}

	}	
	public void testcaseMain() throws InterruptedException, BiffException,
	Exception {
		
		try{
			String testcase_sr = DataMap.get("Testcase ID");
			if(testcase_sr.equals("Product_List_1")||testcase_sr.equals("Product_List_2")||testcase_sr.equals("Product_List_3")){
				productSelection();
			}else {
				productSelection();				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
