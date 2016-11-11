package com.capgemini.scripts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jxl.read.biff.BiffException;

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

public class Header {
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

	private void torkHeaderValidation() throws IOException, InterruptedException {
		Properties object = readObjects.getObjectRepository();

		try{
			stepExecutor.waitForPageToLoad(webDriver);			
			String hyperlink = webDriver.findElementByXPath(object.getProperty("tork_logo")).getAttribute("href");
			System.out.println("hyperlink: "+hyperlink);
			if (hyperlink == null){
				reporter.writeStepResult("tork_logo validation", "Tork logo should be hyperlinked", "Expected: " + "Tork logo should be hyperlinked", "Fail", "Tork logo validation failed", true, null, webDriver);
			}else{
				reporter.writeStepResult("tork_logo validation", "Tork logo should be hyperlinked", "Expected: " + "Tork logo should be hyperlinked", "Pass", "Tork logo validated", true, null, webDriver);
			}
			webDriver.findElementByXPath(object.getProperty("tork_logo")).click();	
		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("tork_logo validation", "Tork logo should be hyperlinked", "Expected: " + "Tork logo should be hyperlinked", "Fail", "Tork logo validation failed", true, null, webDriver);
		}

	}	
	public void testcaseMain() throws InterruptedException, BiffException,
	Exception {
		
		try{
			String testcase_sr = DataMap.get("Testcase ID");
			if(testcase_sr.equals("Header_1")){
				torkHeaderValidation();
			}else {
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
