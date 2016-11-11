package com.capgemini.scripts;


import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jxl.read.biff.BiffException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.capgemini.driver.CreateDriver;
import com.capgemini.driver.ScriptExecutor;
import com.capgemini.driver.StepExecutor;
import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.executor.WriteMaster;
import com.capgemini.utilities.CommonFunctions;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.ReadObject;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;

public class Footer {
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

	private void corpNavigationCheck() throws IOException, InterruptedException {

		try{
			stepExecutor.waitForPageToLoad(webDriver);
			List<WebElement> footerLinks = webDriver.findElements(By.xpath("//*[@id='preFooter']/div/div/a"));
			String menu_footer = DataMap.get("Menu_URLs");
			String menu_title = DataMap.get("Menu");
			System.out.println("Footer Links List: "+menu_footer);
			Actions act = new Actions(webDriver);
			for (WebElement footer: footerLinks){				
				if(menu_footer.contains(footer.getAttribute("href"))){	
					System.out.println("Footer link: "+footer.getAttribute("href"));
					act.moveToElement(footer).perform();					
					if(menu_title.contains(footer.getAttribute("title"))){
						reporter.writeStepResult("Footer menu text validation", "Expected List of menu: " +menu_title , "Actual: " +footer.getAttribute("title"), "Pass", "Footer menu text validated", true, null, webDriver);
					}else{
						reporter.writeStepResult("Footer menu text validation", "Expected List of menu: " +menu_title , "Actual: " +footer.getAttribute("title"), "Fail", "Footer menu text validation Failed", true, null, webDriver);
					}						
					reporter.writeStepResult("Footer menu validation", "Expected List of menu URLs: " +menu_footer , "Actual: " +footer.getAttribute("href"), "Pass", "Footer menu validated", true, null, webDriver);	
				}
				else{
					System.out.println("Footer link: "+footer.getAttribute("href"));
					reporter.writeStepResult("Footer menu validation", "Expected List of menu URLs: " +menu_footer , "Actual: " +footer.getAttribute("href"),  "Fail", "Footer validation failed", true, null, webDriver);
				}
			}

		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("Footer menu validation failed", "Footer menu validation failed", "Expected: " + "Footer menu validation failed", "Fail", "Footer menu validation failed", true, null, webDriver);
		}

	}
	private void endModuleDisplayCheck() throws IOException, InterruptedException {
		try{
			stepExecutor.waitForPageToLoad(webDriver);
			//List<WebElement> footerLinks = webDriver.findElements(By.xpath("//*[@id='preFooter']/div/div/a"));
			stepExecutor.clickLink("xpath", ".//a[@href='http://uatst.torkusa.com/about/whytork/']", webDriver, "Click on Why Tork updated?");
			stepExecutor.waitForPageToLoad(webDriver);
			JavascriptExecutor js = (JavascriptExecutor)webDriver;
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			verify.verifyElementTextPresent(webDriver, DataMap, "Copyright", ".//*[@id='logoFooter']/div/p", "xpath", "Verifying Copyrights");
			verify.verifyLinkPresent(webDriver, "//a[@title='www.sca.com']", "xpath", "Verifying link www.sca.com");
			stepExecutor.clickLink("xpath", "//a[@title='www.sca.com']", webDriver, "Verifying link www.sca.com is clickable");
			stepExecutor.waitForPageToLoad(webDriver);
			verify.verifyElementPresent(webDriver, "//*[@id='mainnav']/ul/li[2]/a", "xpath", "Verifying www.sca.com link has been clicked");
		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("Footer menu validation failed", "Footer menu validation failed", "Expected: " + "Footer menu validation failed", "Fail", "Footer menu validation failed", true, null, webDriver);
		}
	}	
	private void torkLogoFunc() throws IOException, InterruptedException {
		Properties object = readObjects.getObjectRepository();
		int x =0,y=0;
		try{
			stepExecutor.waitForPageToLoad(webDriver);
			stepExecutor.clickLink("xpath", ".//a[@href='http://uatst.torkusa.com/about/whytork/']", webDriver, "Click on Why Tork updated?");
			stepExecutor.waitForPageToLoad(webDriver);			
			String hyperlink = webDriver.findElementByXPath(object.getProperty("tork_logo")).getAttribute("href");
			System.out.println("hyperlink: "+hyperlink);
			if (hyperlink != null){
				reporter.writeStepResult("tork_logo validation", "Tork logo should be hyperlinked", "Expected: " + "Tork logo should be hyperlinked", "Pass", "Tork logo validated", true, null, webDriver);
			}else{
				reporter.writeStepResult("tork_logo validation", "Tork logo should be hyperlinked", "Expected: " + "Tork logo should be hyperlinked", "Fail", "Tork logo validation failed", true, null, webDriver);
			}						
			String pos = DataMap.get("TorkLogoPosition");
			String position[] = pos.split(",");
			String xPos = position[0];
			String yPos = position[1];
			x = Integer.parseInt(xPos);
			y = Integer.parseInt(yPos);		
			verify.verifyElementPosition(x, y,webDriver,"xpath",object.getProperty("tork_logo"),"Position of Tork Logo is proper");
			stepExecutor.clickLink("xpath", object.getProperty("tork_logo"), webDriver, "Click on Tork Logo");

		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("Footer menu validation failed", "Footer menu validation failed", "Expected: " + "Footer menu validation failed", "Fail", "Footer menu validation failed", true, null, webDriver);
		}
	}
	private void followUs() throws IOException, InterruptedException {
		try{
			stepExecutor.waitForPageToLoad(webDriver);
			JavascriptExecutor js = (JavascriptExecutor)webDriver;
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");			
			verify.verifyLinkPresent(webDriver, "//a[@href='https://www.youtube.com/user/TorkBetterBusiness']", "xpath", "Verifying link www.youtube.com");
			verify.verifyText("Follow",webDriver.findElementByXPath("//*[@id='mainFooter']/div/div/div[2]/h3[2]").getText(),"Verifying Follow Text",webDriver);
			stepExecutor.clickLink("xpath", "//a[@href='https://www.youtube.com/user/TorkBetterBusiness']", webDriver, "Click link www.youtube.com");
			stepExecutor.waitForPageToLoad(webDriver);
			Thread.sleep(6000);
			Set<String> windowIterator = webDriver.getWindowHandles();
			for(String window:windowIterator){
				webDriver.switchTo().window(window);
			}
			verify.verifyPageTitle(webDriver, "youtube", "Verifying Youtube link in FOLLOW US");
		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("Footer menu validation failed", "Footer menu validation failed", "Expected: " + "Footer menu validation failed", "Fail", "Footer menu validation failed", true, null, webDriver);
		}
	}
	private void information() throws IOException, InterruptedException {
		try{
			stepExecutor.waitForPageToLoad(webDriver);
			JavascriptExecutor js = (JavascriptExecutor)webDriver;
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");			
			String hyperlink = webDriver.findElementByXPath(".//*[@id='mainFooter']/div/div/div[1]/h3").getAttribute("href");
			System.out.println("hyperlink: "+hyperlink);
			if (hyperlink == null){
				reporter.writeStepResult("Information validation", "Information is not hyperlinked", "Expected: " + "Information should not be hyperlinked", "Pass", "Information validated", true, null, webDriver);				
			}else{
				reporter.writeStepResult("Information validation", "Information is hyperlinked", "Expected: " + "Information should not be hyperlinked", "Fail", "Information validation failed", true, null, webDriver);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("Information validation failed", "Information validation failed", "Expected: " + "Information validation failed", "Fail", "Information menu validation failed", true, null, webDriver);
		}
	}
	private void SCALinkValidation() {
		try{
			stepExecutor.waitForPageToLoad(webDriver);
			JavascriptExecutor js = (JavascriptExecutor)webDriver;
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			stepExecutor.clickLink("xpath", "//a[@title='www.sca.com']", webDriver, "Verifying link www.sca.com is clickable");
			stepExecutor.waitForPageToLoad(webDriver);
			verify.verifyElementPresent(webDriver, "//*[@id='mainnav']/ul/li[2]/a", "xpath", "Verifying www.sca.com link has been clicked");
		}catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("sca link validation failed", "sca link validation failed", "Expected: " + "www.sca.com link should be clicked", "Fail", "sca link validation failed", true, null, webDriver);
		}
	}
	private void SCALogoValidation() {
		try{
			stepExecutor.waitForPageToLoad(webDriver);
			JavascriptExecutor js = (JavascriptExecutor)webDriver;
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");			
			String hyperlink = webDriver.findElementByXPath(".//*[@id='logoFooter']/div/img").getAttribute("href");
			System.out.println("hyperlink: "+hyperlink);
			if (hyperlink == null){
				reporter.writeStepResult("SCA LOGO validation", "SCA LOGO is not hyperlinked", "Expected: " + "SCA LOGO should not be hyperlinked", "Pass", "SCA LOGO validated", true, null, webDriver);				
			}else{
				reporter.writeStepResult("SCA LOGO validation", "SCA LOGO is hyperlinked", "Expected: " + "SCA LOGO should not be hyperlinked", "Fail", "SCA LOGO validation failed", true, null, webDriver);
			}			
		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("SCA LOGO validation failed", "SCA LOGO validation failed", "Expected: " + "SCA LOGO validation failed", "Fail", "SCA LOGO menu validation failed", true, null, webDriver);
		}
		
	}
	private void contact() {
		try{
			stepExecutor.waitForPageToLoad(webDriver);
			JavascriptExecutor js = (JavascriptExecutor)webDriver;
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");			
			String hyperlink = webDriver.findElementByXPath("//*[@id='mainFooter']/div/div/div[2]/h3[1]").getAttribute("href");
			System.out.println("hyperlink: "+hyperlink);
			if (hyperlink == null){
				reporter.writeStepResult("Contact validation", "Contact is not hyperlinked", "Expected: " + "Contact should not be hyperlinked", "Pass", "Contact validated", true, null, webDriver);				
			}else{
				reporter.writeStepResult("Contact validation", "Contact is hyperlinked", "Expected: " + "Contact should not be hyperlinked", "Fail", "Contact validation failed", true, null, webDriver);
			}			
		}
		catch(Exception e){
			e.printStackTrace();
			reporter.writeStepResult("Contact validation failed", "Contact validation failed", "Expected: " + "Contact validation failed", "Fail", "Contact menu validation failed", true, null, webDriver);
		}
		
	}
	public void testcaseMain() throws InterruptedException, BiffException,
	Exception {
		
		try{
			String testcase_sr = DataMap.get("Testcase ID");
			if(testcase_sr.equals("Footer_1")){
				corpNavigationCheck();
			}else if(testcase_sr.equals("Footer_2")){
				endModuleDisplayCheck();				
			}else if(testcase_sr.equals("Footer_3")){
				torkLogoFunc();				
			}else if(testcase_sr.equals("Footer_4")){
				followUs();				
			}else if(testcase_sr.equals("Footer_5")){
				information();				
			}else if(testcase_sr.equals("Footer_6")){
				SCALinkValidation();				
			}else if(testcase_sr.equals("Footer_7")){
				followUs();				
			}else if(testcase_sr.equals("Footer_8")){
				contact();				
			}else if(testcase_sr.equals("Footer_9")){
				SCALogoValidation();				
			}else if(testcase_sr.equals("Footer_10")){
				SCALinkValidation();				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
