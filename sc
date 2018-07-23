package com.walmart.workday.BaseTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.ExtentTestInterruptedException;
import com.relevantcodes.extentreports.LogStatus;
public class TestSetup {

	public static WebDriver driver = null;
	Utilities util = new Utilities(driver);
	public static boolean chromeBrowser = false; 
	//public static boolean ieBrowser = false; 
	//public static String browser = null;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static ExtentTestInterruptedException testexception;
	
	@BeforeSuite
	public void beforeSuite() 
	{
		//Report Directory and Report Name
		extent = new ExtentReports("src/resources/java/ExtentReport/api_execution_report.html", true); //Provide Desired Report Directory Location and Name
		extent.loadConfig(new File("extent-config.xml")); //Supporting File for Extent Reporting
		extent.addSystemInfo("Environment","QA-Sanity"); //It will provide Execution Machine Information
	}
	
	
	@Parameters({ "browser","seleniumURL"})
	@BeforeMethod(alwaysRun = true)
	public void beforeClass(String browser, String seleniumURL) throws MalformedURLException {
		System.out.println(browser);
		driver = getDriverInstance(browser, seleniumURL);
		
		//System.out.println(method.getName());
		
		
	}

	public WebDriver getDriver() {
		return driver;
	}

	public String takeScreenShot(ITestResult testResult) throws IOException {
		File file = null;
		String filePath = null;
		String testMethodName = testResult.getMethod().getMethodName();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		if (testResult.getStatus() == ITestResult.FAILURE) {
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
					.getInstance().getTime());
			file = new File("test-output/Failure_Screenshot/" + date);
			if (!file.exists()) {
				file.mkdir();
			}
			filePath = file + "/" + testMethodName + "_ " + timeStamp + ".jpg";
			FileUtils.copyFile(scrFile, new File(filePath));
		}
		else if (testResult.getStatus() == ITestResult.SUCCESS) {
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
					.getInstance().getTime());
			file = new File("test-output/Success_Screenshot/" + date);
			if (!file.exists()) {
				file.mkdir();
			}
			filePath = file + "/" + testMethodName + "_ " + timeStamp + ".jpg";
			FileUtils.copyFile(scrFile, new File(filePath));
		}
		return filePath;
	}

	@AfterMethod(alwaysRun = true)
	public void closeBrowser(ITestResult testResult, Method method)
			throws Exception {
		String filePath = takeScreenShot(testResult);
		System.out.println("filePath: " + filePath);
		/*TestInfo testInfo = method.getAnnotation(TestInfo.class);
		List<String> testcaseID = new ArrayList<String>(Arrays.asList(testInfo
				.testId()));*/
		//UpdateTest updateTest = new UpdateTest();
		//updateTest.testUpdateTestCase(testcaseID, testResult.getStatus(),
		//		filePath);
		if (getDriver() != null) {
			getDriver().quit();
		}
		
		extent.endTest(test);
		extent.flush();
	}

	@AfterSuite(alwaysRun = true)
	public void closeBrowserAfterSuite() {
		getDriver().quit();
		extent.close();
	}

	public static Capabilities setIECap() {
		DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		caps.setCapability("ignoreZoomSetting", true);
		caps.setCapability("nativeEvents", false);
		caps.setCapability(
				InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				true);
		return caps;
	}

	public static Capabilities setChromeCap() {
		ChromeOptions option = new ChromeOptions();
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setCapability(ChromeOptions.CAPABILITY, option);
		return caps;
	}

	public static WebDriver getDriverInstance(String browser, String seleniumURL)
			throws MalformedURLException {
		WebDriver driver = null;
		//String gridUrl = "http://us07805s4116d0a.s07805.us:4444/wd/hub";
		if (browser.equalsIgnoreCase("iexplorer")) {
			System.setProperty("webdriver.ie.driver","src/resources/java/IEDriverServer.exe");
			driver= new InternetExplorerDriver(setIECap());
			
			//driver = new RemoteWebDriver(new URL(seleniumURL), setIECap());
			//http://us07805s4116d0a.s07805.us:4444/grid/console#
		} if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver","src/resources/java/chromedriver.exe");
			System.setProperty("java.net.useSystemProxies","true");
			driver= new ChromeDriver();
			
			//driver = new RemoteWebDriver(new URL(
					//"http://us07805s4116d0a.s07805.us:4444/wd/hub"), setChromeCap());
		//driver = new RemoteWebDriver(new URL(
					//"http://10.226.191.208:4444/wd/hub"), setChromeCap());
			//driver = new RemoteWebDriver(new URL(seleniumURL), setChromeCap());
			//http://172.17.212.247:4444/wd/hub
		}
		//driver.manage().window().maximize();
		return driver;
	}

	public void loadPage(String URL) {
		driver.get(URL);
		util.waitForPageLoad(driver);
	}

	
	
	
}
