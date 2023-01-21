package base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

@SuppressWarnings("deprecation")
public class TestBase {
	public static WebDriver driver;
	public ExcelDataProvider readExcelData;
	public configDateProvider config;
	public ExtentReports report;
	public ExtentTest logger;
	public int elementWaitInSeconds = 20;
	public int timeOut = 30;
	public int retryattempts = 2;
	public Select select;
	public ExtentTest test;
	WebElement element;

	String url = "https://www.google.com/";

	@BeforeClass
	public void initiateSetup() {
		readExcelData = new ExcelDataProvider();
		config = new configDateProvider();

		ExtentHtmlReporter extent = new ExtentHtmlReporter(
				new File("./Extent_Automation_Reports/ExtentReport_" + Helper.getCurrentDateTime() + ".html"));
		extent.loadXMLConfig(new File(System.getProperty("user.dir") + "./ExtentReport_XML Files/Extent-config.xml"));
		report = new ExtentReports();
		report.setSystemInfo("Evironment", "OPEN");
		report.setSystemInfo("Build#", "Test_Build");
		report.attachReporter(extent);

	}

	@BeforeMethod(alwaysRun = true)
	public void registerTestName(Method method) {
		driver = BrowserFactory.startApplication(driver, "chrome", url);
		String testName = method.getName();
		logger = report.createTest(testName);
	}

	@AfterClass
	public void tearDownMethod() {
		BrowserFactory.quitBrowser(driver);
	}

	@AfterMethod(alwaysRun = true)
	public void teadDownMethod(ITestResult result) throws IOException {

		if (result.getStatus() == ITestResult.FAILURE) {
			System.out.println("Inside the filed step");
			System.out.println("Taking the screenshots of failure step");
			Helper.captureScreenshots(driver);
			System.out.println("Screenshot Captured for Failed Step");
			logger.fail("Test Failed",
					MediaEntityBuilder.createScreenCaptureFromPath(Helper.captureScreenshots(driver)).build());
			System.out.println("Failed Screenshot added in the extent report");
			logger.log(Status.FAIL, "The Test Method Named as: " + result.getName() + " is Failed");
			logger.log(Status.FAIL, "Test Failed: " + result.getThrowable());

		}

		else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(Status.PASS, "The Test Method Named as: " + result.getName() + " is Passed");
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP, "The test Method: " + result.getName() + " is skipped");
		}

		report.flush();
		BrowserFactory.quitBrowser(driver);

	}

///////Generic Methods////////////////

	public void setImplicit(int timeOut) {

		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	}

	public WebDriverWait webDriverWait() {

		return new WebDriverWait(driver, elementWaitInSeconds);
	}

	public String getText(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element.getText();

	}

	public void clickEvent(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		webDriverWait().until(ExpectedConditions.visibilityOf(element));
		element.click();
		System.out.println("Element: " + locator + " Clicked");

	}

	public void enterText1(String value, String locator) {

		WebElement element = driver.findElement(By.xpath(locator));
		scrollToElement(element);
		waitVisibilityOfElement(element);
		// element.clear();
		waitFor(1000);
		logger.info("Entered Text - " + value);
		element.sendKeys(value);

	}

	public static void printAllElements(String locator) {
		java.util.List<WebElement> list = driver.findElements(By.xpath(locator));

		for (WebElement ele : list) {
	 		String name = ele.getText();
			System.out.println(name);

		}

	}

	public void waitFor(int sleepTime) {

		try {

			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void notClickableAtPointRetryClick(WebElement element) {

		int attempts = 0;
		while (attempts < retryattempts) {
			try {

				waitVisibilityOfElement(element);
				waitElementToBeClickable(element);
				element.click();
				break;
			} catch (Exception e) {

				waitFor(1000);
			}
			attempts++;
		}
	}

	public void clickButton(String locator) {

		try {
			// loaderDisappear();
			element = driver.findElement(By.xpath(locator));
			scrollToElement(element);
			waitVisibilityOfElement(element);
			waitElementToBeClickable(element);
			waitFor(2000);
			element.click();
			System.out.println("button clicked");
		} catch (StaleElementReferenceException e) {

			staleRetryingFindClick(element);
		} catch (Exception e) {

			notClickableAtPointRetryClick(element);
		}
	}

	public void enterTextWithoutScroll(WebElement element, String textValue) {

		waitVisibilityOfElement(element);
		element.clear();
		waitFor(1000);
		logger.info("Entered Text - " + textValue);
		element.sendKeys(textValue);
	}

	public void waitVisibilityOfElement(WebElement element) {
		webDriverWait().until(ExpectedConditions.visibilityOf(element));
	}

	public void waitElementToBeClickable(WebElement element) {

		webDriverWait().until(ExpectedConditions.elementToBeClickable(element));
	}

	public void waitPresenceOfElementLocated(By by) {

		webDriverWait().until(ExpectedConditions.presenceOfElementLocated(by));
	}

	public void highLighterMethod(WebDriver driver, WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: ; border: 2px solid blue;');", element);
	}

	public boolean isElementDisplayed(List<WebElement> elements) {

		return !elements.isEmpty();
	}

	public boolean staleRetryingFindClick(WebElement element) {
		System.out.println("Inside in the staleRetryingFindClick");
		boolean result = false;
		int attempts = 0;
		while (attempts < retryattempts) {
			try {

				waitVisibilityOfElement(element);
				waitElementToBeClickable(element);
				element.click();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {

				waitFor(500);
			}
			attempts++;
		}
		return result;
	}

	public String getAttributeValue(String searchbox, String attributeName) {

		WebElement ele = driver.findElement(By.xpath(searchbox));

		logger.info("Attribute Value - " + ele.getAttribute(attributeName));
		return ele.getAttribute(attributeName);
	}

	public static String getCurrentDir() {

		String currentDir = System.getProperty("user.dir");
		currentDir = currentDir.replace('\\', '/');
		return currentDir;
	}

	public void scrollToElement(WebElement element) {

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

}
