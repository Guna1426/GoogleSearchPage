package base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import utility.BrowserFactory;
import utility.ExcelDataProvider;
import utility.Helper;
import utility.configDateProvider;

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

			
		ExtentHtmlReporter extent = new ExtentHtmlReporter( new
		File("./Extent_Automation_Reports/ExtentReport_" + Helper.getCurrentDateTime() + ".html"));
		extent.loadXMLConfig(new File(System.getProperty("user.dir") +
		"./ExtentReport_XML Files/Extent-config.xml")); report = new ExtentReports();
	report.setSystemInfo("Evironment", "OPEN"); report.setSystemInfo("Build#",
	 "Test_Build"); report.attachReporter(extent);
	 
	 
	}

	@BeforeMethod(alwaysRun = true)
	public void registerTestName(Method method) {
		driver = BrowserFactory.startApplication(driver, "chrome",url);
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

	public void mouseOver(WebElement element) {

		waitVisibilityOfElement(element);
		new Actions(driver).moveToElement(element).build().perform();
	}

	public void mouseOver(List<WebElement> element, int index) {

		waitVisibilityOfElement(element.get(index));
		new Actions(driver).moveToElement(element.get(index)).build().perform();
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
	
	public static void printAllElements(String locator)
	{
		java.util.List<WebElement> list = driver.findElements(By.xpath(locator));
		
		for(WebElement ele: list)
		{
			String name = ele.getText();
			System.out.println(name);
					
		}

	}

	public String getText(WebElement element) {

		waitVisibilityOfElement(element);
		highLighterMethod(driver, element);
		logger.info("Element Text - " + element.getText());
		return element.getText();
	}

	public boolean isTextMatch(String actual, String expected) {

		logger.info("Actual Value - " + actual + "\n" + "Expected Value - " + expected);
		return actual.equalsIgnoreCase(expected);
	}

	public boolean isTextContain(String actual, String expected) {

		logger.info("Actual text - " + actual + "\n" + "Expected text - " + expected);
		return actual.contains(expected);
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

	public void clickButtonWithOutScroll(WebElement element) {

		// loaderDisappear();
		waitVisibilityOfElement(element);
		waitElementToBeClickable(element);
		waitFor(2000);
		element.click();

	}

	public void scrollToTop() {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,0)");
		waitFor(1000);
	}

	public void scrollToBottom() {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		waitFor(1000);
	}

	public By locateXpath(String xpath) {

		return By.xpath(xpath);
	}

	public By locateCss(String css) {

		return By.cssSelector(css);
	}

	public void clickButton(WebElement scrollToElement, WebElement clickElement) {

		waitFor(1000);
		scrollToElement(scrollToElement);
		waitVisibilityOfElement(clickElement);
		waitElementToBeClickable(clickElement);
		clickElement.click();
	}

	public void clickDropDown(WebElement element, String xpath) {

		waitFor(1000);
		waitPresenceOfElementLocated(locateCss(xpath));
		element.click();
	}

	public void enterText(String locator, String textValue) {

		element = driver.findElement(By.xpath(locator));

		scrollToElement(element);
		waitVisibilityOfElement(element);
		// element.clear();
		waitFor(1000);
		logger.info("Entered Text - " + textValue);
		element.sendKeys(textValue);

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

	public void loaderDisappear() {
		List<WebElement> loader = driver.findElements(By.cssSelector("#maskoverlay[style *= 'display: block']"));
		if (!loader.isEmpty()) {

			webDriverWait().until(ExpectedConditions
					.invisibilityOfElementLocated((By.cssSelector("#maskoverlay[style *= 'display: block']"))));
			waitFor(500);
		}
	}

	public void switchToParentWindow() {

		Set<String> winHandles = driver.getWindowHandles();
		for (String wHandle : winHandles) {
			driver.switchTo().window(wHandle);
			break;
		}
	}

	public void switchToLastWindow() {

		Set<String> winHandles = driver.getWindowHandles();
		for (String wHandle : winHandles) {

			driver.switchTo().window(wHandle);
		}
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

	public boolean staleRetryingEnterText(WebElement element, String... value) {

		boolean result = false;
		int attempts = 0;
		while (attempts < retryattempts) {
			try {
				waitVisibilityOfElement(element);
				element.sendKeys(value);
				result = true;
				break;
			} catch (StaleElementReferenceException e) {

				waitFor(500);
			}
			attempts++;
		}
		return result;
	}

	public boolean staleRetryingElementIsDisplayed(WebElement element) {

		boolean result = false;
		int attempts = 0;
		while (attempts < retryattempts) {
			try {

				waitVisibilityOfElement(element);
				element.isDisplayed();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {

				waitFor(500);
			}
			attempts++;
		}
		return result;
	}

	public boolean isElementDisplayed(String locator) {

		boolean flag;

		WebElement element = driver.findElement(By.xpath(locator));

		try {
			waitFor(1000);
			setImplicit(10);
			scrollToElement(element);
			waitVisibilityOfElement(element);
			highLighterMethod(driver, element);
			flag = element.isDisplayed();
		} catch (StaleElementReferenceException stale) {

			flag = staleRetryingElementIsDisplayed(element);
		}

		catch (Exception e) {

			flag = false;
		}
		logger.info("Is element " + element + " displayed - " + flag);
		return flag;
	}

	public boolean isElementDisplayedWithoutScroll(WebElement element) {

		boolean flag;
		try {

			waitFor(1000);
			setImplicit(10);
			highLighterMethod(driver, element);
			element.isDisplayed();
			flag = true;
		} catch (Exception e) {

			setImplicit(timeOut);
			flag = false;
		}
		logger.info("Is element " + element + " displayed - " + flag);
		return flag;
	}

	public boolean isElementEnabled(WebElement element) {

		boolean flag;
		try {

			waitFor(1000);
			scrollToElement(element);
			highLighterMethod(driver, element);
			element.isEnabled();
			flag = true;
		} catch (Exception e) {

			flag = false;
		}
		logger.info("Is element " + element + " enabled - " + flag);
		return flag;
	}

	public boolean isEnabled(WebElement element) {

		logger.info("Is element " + element + "enabled - " + element.isEnabled());
		return element.isEnabled();
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

	public Select selectDropdown(WebElement element) {

		select = new Select(element);
		return select;
	}

	public void selectByIndex(WebElement element, int index) {
		waitFor(1000);
		selectDropdown(element).selectByIndex(index);
	}

	public void SelectByValue(WebElement element, String value) {
		waitFor(1000);
		selectDropdown(element).selectByValue(value);
	}

	public void SelectByVisibleText(WebElement element, String text) {
		waitFor(1000);
		selectDropdown(element).selectByVisibleText(text);
	}

	public void waitForLoadIconDisappear() {

		webDriverWait()
				.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oak-searchResults_preloader")));
	}

	public String getPageTitle() {

		return driver.getTitle();
	}

	public void swipeByXCoordinates(WebElement element, int offSet) {

		Point point = element.getLocation();
		Actions actions = new Actions(driver);
		System.out.println("val: " + point.getX());
		actions.dragAndDropBy(element, offSet, point.getY()).build().perform();
	}

	public WebElement getXpathElement(String xpath) {

		return driver.findElement(By.xpath(xpath));
	}

	@SuppressWarnings("unused")
	public void switchToFirstFrame() {

		List<WebElement> frames = driver.findElements(By.xpath("//iframe"));
		for (int i = 0; i < frames.size(); i++) {
			driver.switchTo().frame(i);
			break;
		}
	}

	public void clickAcceptInBrowserAlertPopUp() {

		try {
			webDriverWait().until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void clearTextInTheTextField(String locator) {

		WebElement ele = driver.findElement(By.xpath(locator));
		waitVisibilityOfElement(ele);
		ele.clear();
	}

	public String removingWhiteSpaces(String data) {

		String csvReqID = data.trim();
		String finalReqID = "";
		char[] value = csvReqID.toCharArray();
		for (int i = 0; i < value.length; i++) {
			if ((i % 2) == 0) {
				finalReqID = finalReqID + value[i];
			}
		}
		return finalReqID;
	}

	public void deleteAFile(String filePath) {
		File file = new File(filePath);
		file.delete();
	}

	public String gettingTextMessageFromAlertPopUp() {

		return (driver.switchTo().alert().getText());
	}

	public void closingTheAlertPopUp() {

		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	/*
	 * public Map<String, List<String>> getHorizontalData(cucumber.api.DataTable
	 * dataTable) { Map<String, List<String>> dataMap = null; try { dataMap = new
	 * HashMap<String, List<String>>(); List<String> headingRow =
	 * dataTable.raw().get(0); int dataTableRowsCount =
	 * dataTable.getGherkinRows().size() - 1; ArrayList<String> totalRowCount = new
	 * ArrayList<String>(); totalRowCount.add(Integer.toString(dataTableRowsCount));
	 * dataMap.put("totalRowCount", totalRowCount); for (int i = 0; i <
	 * headingRow.size(); i++) { List<String> dataList = new ArrayList<String>();
	 * dataMap.put(headingRow.get(i), dataList); for (int j = 1; j <=
	 * dataTableRowsCount; j++) { List<String> dataRow = dataTable.raw().get(j);
	 * dataList.add(dataRow.get(i)); } } } catch (Exception e) {
	 * e.printStackTrace(); } return dataMap; }
	 * 
	 * 
	 */
//	public String getTestDataValue(String classPath, String fieldName) {
//		String expectedPageName = null;
//		try {
//			Class<?> classObj = Class.forName(classPath);
//			Object obj = classObj.newInstance();
//			Field fieldValue = classObj.getDeclaredField(fieldName);
//			expectedPageName = (String)fieldValue.get(obj);
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		return expectedPageName;
//	}
//	public String[] getTestDataValues(String classPath, String fieldName) {
//		String[] expectedPageName = null;
//		try {
//			Class<?> classObj = Class.forName(classPath);
//			Object obj = classObj.newInstance();
//			Field fieldValue = classObj.getDeclaredField(fieldName);
//			expectedPageName = (String[])fieldValue.get(obj);
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		return expectedPageName; 
//	}

}
