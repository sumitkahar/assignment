package com.capgemini.scripts;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;

import org.openqa.selenium.remote.DesiredCapabilities;

public class Hero_Carousal {
	
	public String TestCase=this.getClass().getSimpleName();
	DesiredCapabilities capabilities = new DesiredCapabilities();
	Reporter reporter = new Reporter(TestCase);
	CreateDriver driver = new CreateDriver();
	RemoteWebDriver webDriver = null;
	
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

	public String getStartTime() {
		return String.valueOf(executionStartTime);
	}
	public void executeTestcase(String browserName) throws Exception {
		int iNumberOfRows = 0;
		readExcel.setInputFile(System.getProperty("File"));
		readExcel.setSheetName(TestCase);
		Map<Integer, String> seqMap = readExcel.getiNOfRowsSeq();
		iNumberOfRows = readExcel.getiNOfRows();
		
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
			verifyCarousal();
			//navigateProductSection();
			//navigateEditorialSection();
			//checkFunctionality(browserName);
			//NextFunctionCall
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
	
	private void verifyCarousal() {
		
		
	}

	public void executeTestcase(RemoteWebDriver rdriver,String host,String browser) throws Exception {		
		int iNumberOfRows = 0;
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
			//readExcel.readByIndex(i);
			//webDriver.switchTo().activeElement();
			//System.out.println(capabilities.getBrowserName());
			//reporter.setStrBrowser(capabilities.getBrowserName());
			DataMap=readExcel.loadDataMap(i);
			reporter.addIterator(i);
			launchApp();
			navigateProductSection();
			//navigateEditorialSection();
			//checkFunctionality(browser);
			//NextFunctionCall
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

	public void navigateProductSection() throws InterruptedException, BiffException,Exception 
	{	
		try 
		{
			stepExecutor.writeNewScenario(webDriver, "Go to Product details page");
			stepExecutor.clickLink("findElementByXPath", ".//*[@id='headerNav']/div/div/div[1]/a", webDriver, "Click on Products");
			Thread.sleep(2000);
			stepExecutor.clickLink("findElementByXPath", ".//*[@id='ProductNav_dispNavigationTable']/tbody/tr[1]/td[1]/a", webDriver, "Click on Product Category");
			Thread.sleep(8000);
			verify.verifyElementPresent(webDriver, ".//*[@id='specificProductFamily']/section/h2","xpath","Verify presence of Product Range header section");
			stepExecutor.selectProductFromProductsPage("findElementByXPath",webDriver,DataMap,"prodname",15,"Select any Product");
			Thread.sleep(7000);
		      	verify.verifyElementPresent(webDriver, ".//*[@id='productPage']/section/div/div[1]/div/div[1]/div[3]/div[2]/div","xpath","Verify presence of Product Model section");
				verify.verifyElementPresent(webDriver, ".//*[@id='productPage']/section/div/div[1]/div/div[1]/div[3]/div[3]/div/div/p","xpath","Verify presence of Product Description section");
				verify.verifyElementPresent(webDriver, ".//*[@id='contentPlaceholder_ProductDetail_btnAddToList']","xpath","Verify presence of Add To List button");
				verify.verifyImageBroken(webDriver,".//*[@id='images_dark_grey']/div[1]/div/ul/li/img","Check product image broken or not");
				stepExecutor.clickLink("findElementByXPath", ".//*[@id='readMore']/span[2]", webDriver, "Click on Read More");
				Thread.sleep(1000);
				verify.verifyElementPresent(webDriver, ".//*[@id='contentPlaceholder_ProductDetail_lblProductDescPara']/ul","xpath","Verify presence of Read More content");
				stepExecutor.clickLink("findElementByXPath", ".//*[@id='readLess']/span[2]", webDriver, "Click on Read Less");
				Thread.sleep(1000);
				/*JavascriptExecutor js = (JavascriptExecutor)webDriver;
			      for(int sl=0;;sl++)
			        {
			            if(sl>=1)
			            {
			                break;
			            }
			            js.executeScript("window.scrollBy(0,200)","");
			            Thread.sleep(1000);
			        }*/
				stepExecutor.scrollForTridion(webDriver,1,"pagedownsteps","Scroll down the page by 1 step");
				stepExecutor.clickLink("findElementByXPath", ".//*[@id='productPage']/section/div/div[1]/div/div[2]/div/div/a[1]", webDriver, "Click on Product Data tab");
				Thread.sleep(2000);
				verify.verifyElementPresent(webDriver, ".//*[@id='productDataTab']","xpath","Verify presence of Product Data content");
				stepExecutor.clickLink("findElementByXPath", ".//*[@id='productPage']/section/div/div[1]/div/div[2]/div/div/a[1]", webDriver, "Click on Product Data tab to close the Product Data content section");	
				Thread.sleep(1000);
				stepExecutor.clickLink("findElementByXPath", ".//*[@id='productPage']/section/div/div[1]/div/div[2]/div/div/a[2]", webDriver, "Click on Shipping Data tab");
				Thread.sleep(2000);
				verify.verifyElementPresent(webDriver, ".//*[@id='shippingDataTab']","xpath","Verify presence of Shipping Data content");
				stepExecutor.clickLink("findElementByXPath", ".//*[@id='productPage']/section/div/div[1]/div/div[2]/div/div/a[2]", webDriver, "Click on Shipping Data tab to close the Shipping Data content section");
				Thread.sleep(1000);
				stepExecutor.clickLink("findElementByXPath", ".//*[@id='productPage']/section/div/div[1]/div/div[2]/div/div/a[3]", webDriver, "Click on Downloads tab");
				Thread.sleep(2000);
				verify.verifyElementPresent(webDriver, ".//*[@id='downloadsTab']","xpath","Verify presence of Shipping Data content");
				stepExecutor.clickLink("findElementByXPath", ".//*[@id='productPage']/section/div/div[1]/div/div[2]/div/div/a[3]", webDriver, "Click on Downloads Data tab to close the Downloads Data content section");
				Thread.sleep(2000);
			    JavascriptExecutor js2 = ((JavascriptExecutor)webDriver);
			    js2.executeScript("window.scrollTo(document.body.scrollHeight,0)");
			    stepExecutor.writeNewScenario(webDriver, "Add the product to Product List");
			    stepExecutor.clickLink("findElementByXPath", ".//*[@id='contentPlaceholder_ProductDetail_btnAddToList']", webDriver, "Click on Add To List button to add the product into Basket");
			    Thread.sleep(5000);
			    /*JavascriptExecutor js3 = (JavascriptExecutor)webDriver;
			      for(int sl=0;;sl++)
			        {
			            if(sl>=1)
			            {
			                break;
			            }
			            js3.executeScript("window.scrollBy(0,200)","");
			            Thread.sleep(1000);
			        }*/
			    	stepExecutor.scrollForTridion(webDriver,1,"pagedownsteps","Scroll down the page by 1 step");
			      verify.verifyElementEnableStatus(webDriver, ".//*[@id='emailButton']","xpath", "disable", "Verify Email Selected button disabled");
			      verify.verifyElementEnableStatus(webDriver, "downloadButton","id", "disable", "Verify Download as PDF button disabled");
			      webDriver.findElementByXPath(".//*[@id='shoppingList']/div/div/section/div[3]/div[1]/div/div/label[1]").click();
			      Thread.sleep(4000);
			      verify.verifyElementEnableStatus(webDriver, ".//*[@id='emailButton']","xpath", "enable", "Verify Email Selected button enabled"); 
			      verify.verifyElementEnableStatus(webDriver, ".//*[@id='downloadButton']","xpath", "enable", "Verify Download as PDF button enabled");
			      stepExecutor.writeNewScenario(webDriver, "Verify user is able to send product uggestion to any friend");
			      stepExecutor.clickLink("findElementByXPath", ".//*[@id='emailButton']", webDriver, "Click on Email Selected button");
			      Thread.sleep(4000);
			      	stepExecutor.enterTextValue("findElementById", "textFrom", DataMap,"enteryourname", webDriver,"Enter First Name in Newsletter form");
					stepExecutor.enterTextValue("findElementById", "textTo", DataMap,"enterrecepientemail", webDriver,"Enter Recepient Email in Newsletter form");
					stepExecutor.enterTextValue("findElementById", "textYouremail", DataMap,"enteruseremail", webDriver,"Enter Your Email in Newsletter form");
					stepExecutor.enterTextValue("findElementById", "textYourphone", DataMap,"enterphnone", webDriver,"Enter Your Phone Number in Newsletter form");
					stepExecutor.enterTextValue("findElementById", "textSubject", DataMap,"entersubject", webDriver,"Enter Subject in Newsletter form");
					stepExecutor.enterTextValue("findElementById", "emailMessage", DataMap,"entermessage", webDriver,"Enter Email Message in Newsletter form");
					/*JavascriptExecutor js4 = (JavascriptExecutor)webDriver;
				      for(int sl=0;;sl++)
				        {
				            if(sl>=1)
				            {
				                break;
				            }
				            js4.executeScript("window.scrollBy(0,200)","");
				            Thread.sleep(1000);
				        }*/
					stepExecutor.scrollForTridion(webDriver,1,"pagedownsteps","Scroll down the page by 1 step");
					webDriver.findElementByXPath(".//*[@id='emailForm']/div[2]/div/div/input").click();
					Thread.sleep(8000);
					verify.verifyElementTextPresent(webDriver, DataMap, "subscribesuccessmessage", ".//*[@id='productListMessageDiv']", "xpath", "Check presence of Newsletter subscription success message"); 	
					JavascriptExecutor js5 = ((JavascriptExecutor)webDriver);
				    js5.executeScript("window.scrollTo(document.body.scrollHeight,0)");
				    
				    /*JavascriptExecutor js6 = (JavascriptExecutor)webDriver;
				      for(int sl=0;;sl++)
				        {
				            if(sl>=1)
				            {
				                break;
				            }
				            js6.executeScript("window.scrollBy(0,200)","");
				            Thread.sleep(1000);
				        }*/
				    	stepExecutor.scrollForTridion(webDriver,1,"pagedownsteps","Scroll down the page by 1 step");
				      stepExecutor.writeNewScenario(webDriver, "Verify user is able to delete the selected product or not");
				      stepExecutor.clickLink("findElementByXPath", ".//*[@id='deleteButton']", webDriver,"Click on Delete button");
				      verify.verifyElementNotPresent(webDriver, ".//*[@id='Div_ShoppingList']/div/div/div[2]/a[1]/h3", "xpath", "Check product deleted should not be present in tha table");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
}
	
	public void navigateEditorialSection() throws InterruptedException, BiffException,Exception 
	{	
		try 
		{
			verify.verifyImageBroken(webDriver, ".//*[@id='headerToolbar']/div[2]/div/a/img", "Verify presence of Tork logo on homepage");
			stepExecutor.Scroll(webDriver, ".//*[@id='testimonials']/div/div[2]/div[2]", "findElementByXPath");
			stepExecutor.clickLink("findElementByXPath", ".//*[@id='preFooter']/div/div[1]/a[1]", webDriver, "Click on About Tork");
			Thread.sleep(5000);
			verify.verifyImageBroken(webDriver,".//*[@id='mainForm']/section/div/div/section/article/div/div[1]/img","Check About Tork Banner Image");
			verify.verifyElementTextPresent(webDriver, DataMap,"abouttorktitle", ".//*[@id='mainForm']/section/div/div/section/article/div/div/div[1]/div[4]/div[1]/h1/div","xpath", "Check presence of About Tork title");
			/*stepExecutor.Scroll(webDriver, ".//*[@id='mainForm']/section/div/div/section/article/div/div[2]", "findElementByXPath");
			stepExecutor.clickLink("findElementByLinkText", "What´s new", webDriver, "Click on What's new editorial page");		
			Thread.sleep(5000);
			//verify.verifyImageBroken(webDriver,".//*[@id='mainForm']/section/div/div/section/article/div/div/div[1]/img","Check What's New Banner Image");
			verify.verifyElementTextPresent(webDriver, DataMap,"editorialtitle", ".//*[@id='mainForm']/section/div/div/section/article/div/div/div[1]/h1","xpath", "Check presence of What's New title");
			stepExecutor.Scroll(webDriver, ".//*[@id='divdata']/article[1]/div", "findElementByXPath");
			stepExecutor.clickLink("findElementByXPath", ".//*[@id='divdata']/span[1]/article/div/div[2]/div/a", webDriver, "Click on Read More link of any campaign page");
			Thread.sleep(2000);
			stepExecutor.Scroll(webDriver, ".//*[@id='campaignform']", "findElementByXPath");
			verify.verifyLinkPresent(webDriver, ".//*[@id='printBtn']", "xpath", "Verify presence of Print icon link");
			verify.verifyLinkPresent(webDriver, ".//*[@id='contentPlaceholder_ctl01_ctl00_savePdfBtn']", "xpath", "Verify presence of Download icon link");
			verify.verifyLinkPresent(webDriver, ".//*[@id='shareBtn']/a", "xpath", "Verify presence of Share icon link");	*/	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void checkFunctionality(String browser) throws InterruptedException, BiffException,Exception 
	{	
		try 
		{
			stepExecutor.Scroll(webDriver, ".//*[@id='solutions']/div/div/section/article/div/div[2]/div", "findElementByXPath");
			Thread.sleep(2000);
			if(browser.equalsIgnoreCase("IE"))
			{
			stepExecutor.clickLink("findElementByXPath", ".//*[@id='printBtn']/span[1]", webDriver, "Click on Print icon link");
			Thread.sleep(4000);
			Runtime.getRuntime().exec("D:\\Automation_Projects_Home\\AutoIT\\Print.exe");
			}
			Thread.sleep(3000);
			stepExecutor.Scroll(webDriver, ".//*[@id='solutions']/div/div/section/article/div/div[2]/div", "findElementByXPath");
			Thread.sleep(2000);
			/*stepExecutor.clickLink("findElementByXPath", ".//*[@id='contentPlaceholder_ctl01_ctl00_savePdfBtn']/span", webDriver, "Click on Download icon link");
			Thread.sleep(4000);*/
			stepExecutor.clickLink("findElementByXPath", ".//*[@id='shareBtn']/a", webDriver, "Click on Share icon link");
			Thread.sleep(4000);
			stepExecutor.childframeswitch(webDriver,"at3winshare-iframe");
			verify.verifyElementTextPresent(webDriver, DataMap,"sharetitle", ".//*[@id='at3winheadermsg']","xpath","Check presence of Share Title");
			stepExecutor.clickLink("findElementByXPath", ".//*[@id='toolbox']/a[2]/span[2]", webDriver, "Click on Gmail");
			Thread.sleep(15000);
			for (String newwindow : webDriver.getWindowHandles()) {
				webDriver.switchTo().window(newwindow); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
			}
			stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='Email']", DataMap, "gmailuser", webDriver, "Enter Gmail Username");
			stepExecutor.clickButton("findElementByXPath",".//*[@id='next']", webDriver,"Click on Next button");
			stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='Passwd']", DataMap, "gmailpassword", webDriver, "Enter Gmail Password");
			stepExecutor.clickButton("findElementByXPath",".//*[@id='signIn']", webDriver,"Click on SignIn button");
			Thread.sleep(10000);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
}


