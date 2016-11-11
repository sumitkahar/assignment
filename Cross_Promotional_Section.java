package com.capgemini.scripts;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import junit.framework.Assert;
import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
import com.thoughtworks.selenium.Selenium;

import org.openqa.selenium.remote.DesiredCapabilities;

public class Cross_Promotional_Section{
	
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
	public void checkCrossPrmoOnTopOfTestimonial() throws Exception
	{
		Properties object = readObjects.getObjectRepository();
		verify.verifyElementPresent(webDriver, object.getProperty("testimonials_xpath"),"xpath","Testimonial module is present");
		System.out.println("Verifying cross promotional module is on the third position before testimonial");
		verify.verifyElementPresent(webDriver, object.getProperty("cross_promotional_xpath"), "xpath","Cross Promotional module is present above testimonial");
		
		List<WebElement> CrossPromoSections = webDriver.findElements(By.xpath("//*[@id='BusinessArea']/div/div/div[1]/div[2]"));
		for(int i =1;i<=CrossPromoSections.size();i++){
			String hyperlink = webDriver.findElementByXPath(".//*[@id='BusinessArea']/div/div/div[1]/div[2]/div/div["+i+"]/div/div/div[2]/a/div/h3/div").getAttribute("href");
			System.out.println("hyperlink: "+hyperlink);
			if (hyperlink == null){
				reporter.writeStepResult("Hyperlink validation", "Verify Hyperlink does not exist for text", "Expected: " + "Hyperlink should not present", "Pass", "Hyperlink is not present", true, null, webDriver);
							}
			else{
				reporter.writeStepResult("Hyperlink validation", "Verify Hyperlink does not exist for text", "Expected: " + "Hyperlink should not present", "Fail", "Hyperlink is present", true, null, webDriver);
			}		
			webDriver.findElementByXPath(".//*[@id='BusinessArea']/div/div/div[1]/div[2]/div/div["+i+"]/div/div/div[2]/a/div/h3/div").click();
		}
	
	}	
	private void learnMoreLink() throws IOException {
		System.out.println("To Check whether learn more is clickable");
		Properties object = readObjects.getObjectRepository();
		verify.verifyElementPresent(webDriver, object.getProperty("testimonials_xpath"),"xpath","Testimonial module is present");
		System.out.println("Verifying cross promotional module is on the third position before testimonial");
		verify.verifyElementPresent(webDriver, object.getProperty("cross_promotional_xpath"), "xpath","Cross Promotional module is present above testimonial");
		
		List<WebElement> CrossPromoSections = webDriver.findElements(By.xpath("//*[@id='BusinessArea']/div/div/div[1]/div[2]"));
		for(int i =1;i<=CrossPromoSections.size();i++){
			String hyperlink = webDriver.findElementByXPath(".//*[@id='BusinessArea']/div/div/div[1]/div[2]/div/div["+i+"]/div/div/div[2]/a").getAttribute("href");
			System.out.println("hyperlink: "+hyperlink);
			if (hyperlink == null){
				reporter.writeStepResult("Learn More Hyperlink validation", "Verify Hyperlink does not exist for text", "Expected: " + "Hyperlink should present for learn more", "Fail", "Hyperlink is not present for Learn More", true, null, webDriver);
							}
			else{
				reporter.writeStepResult("Learn More Hyperlink validation", "Verify Hyperlink exists for text", "Expected: " + "Hyperlink should present for learn more", "Pass", "Hyperlink is present for Learn More", true, null, webDriver);
			}		
			webDriver.findElementByXPath(".//*[@id='BusinessArea']/div/div/div[1]/div[2]/div/div["+i+"]/div/div/div[2]/a").click();
		}
		
	}
	private void verifyHeadingText() throws IOException {
		Properties object = readObjects.getObjectRepository();
		String hyperlink = webDriver.findElementByXPath(object.getProperty("choose_your_business")).getAttribute("href");
		System.out.println("hyperlink: "+hyperlink);
		if (hyperlink == null){
			reporter.writeStepResult("Choose your Business Area Hyperlink validation", "Verify Hyperlink does not exist for text", "Expected: " + "Hyperlink should not present for Choose your Business Area", "Pass", "Hyperlink is not present for Choose your Business Area", true, null, webDriver);
						}
		else{
			reporter.writeStepResult("Choose your Business Area Hyperlink validation", "Verify Hyperlink does not exist for text", "Expected: " + "Hyperlink should not present for Choose your Business Area", "Fail", "Hyperlink is present for Choose your Business Area", true, null, webDriver);
		}		
		webDriver.findElementByXPath(object.getProperty("choose_your_business")).click();
		Set<String> windowIterator = webDriver.getWindowHandles();
        System.err.println("No of windows :  " + windowIterator.size());
        if (windowIterator.size() == 1){
			reporter.writeStepResult("Choose your Business Area click validation", "There should not be other window opened on click", "Expected: " + "Choose your Business Area", "Pass", "Everything is fine on click", true, null, webDriver);
						}
		else{
			reporter.writeStepResult("Choose your Business Area click validation", "There should not be other window opened on click", "Expected: " + "Choose your Business Area", "Fail", "Mismatch on click", true, null, webDriver);
		}	
		
	}	
	public void testcaseMain() throws InterruptedException, BiffException,
	Exception {
		
		try{
			String testcase_sr = DataMap.get("Testcase ID");
			if(testcase_sr.equals("CP_1")){
				checkCrossPrmoOnTopOfTestimonial();
			}else if(testcase_sr.equals("CP_2")){
				learnMoreLink();
			}else if(testcase_sr.equals("CP_5")){
				verifyHeadingText();
			}else{}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}


