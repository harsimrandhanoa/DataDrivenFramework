package com.rediff.ddf.base;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import reporting.ExtentManager;

public class GenericKeywords {

	public WebDriver driver;
	public Properties prop;
	public Properties envProp;
	public ExtentTest test;
	public SoftAssert softAssert;

	public void openBrowser(String browser) {
		log("Opening The Browser " + browser);

		if (prop.get("grid_run").equals("Y")) {
			driver = openBrowserOnGrid(browser);
		}

		else {

			if (browser.equals("Chrome")) {
				driver = DriverOptions.getChromeDriver(prop);

            } else if (browser.equals("Mozilla")) {
				driver = DriverOptions.getFirefoxDriver(prop);
				threadWait(10);

			} else if (browser.equals("Edge")) {
				driver = DriverOptions.getEdgeDriver(prop);
			}
		}
		threadWait(2);
		// implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		// driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
	}

	public WebDriver openBrowserOnGrid(String browser) {
		log("Tests to be run on selenium grid");
		ChromeOptions chromeOptions = new ChromeOptions();
		FirefoxOptions firefoxOptions = new FirefoxOptions();

		DesiredCapabilities cap = new DesiredCapabilities();
		if (browser.equals("Mozilla")) {

			 try {
				driver = new RemoteWebDriver(new URL("http://192.168.56.1:4444/"), firefoxOptions);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (browser.equals("Chrome")) {

			chromeOptions.addArguments("start-maximized");
			chromeOptions.addArguments("disable-infobards");
		}
		try {
			// hit the hub
			driver = new RemoteWebDriver(new URL("http://192.168.56.1:4444/"), chromeOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return driver;
	}

	public void navigate(String urlKey) {
		String url = envProp.getProperty(urlKey);
		log("Navigating to " + url);
		driver.get(url);
	}

	public void click(String locatorKey) {
		String locator = prop.getProperty(locatorKey);
		log("Clicking on " + locator);
		getElement(locator).click();
	}

	public void clickOnText(String text) {
		log("Clicking on text " + text);
		getElementByText(text).click();
	}

	public void type(String locatorKey, String data) {
		String locator = prop.getProperty(locatorKey);
		log("Typing in " + locator + " . Data " + data);
		getElement(locator).sendKeys(data);
	}

	public void clear(String locatorKey) {
		String locator = prop.getProperty(locatorKey);
		log("Clearing text field " + locator);
		getElement(locator).clear();
	}

	public void clickEnterButton(String locatorText) {
		getElementByText(locatorText).sendKeys(Keys.ENTER);
		threadWait(2);
	}

	public void selectByVisibleText(String locatorKey, String data) {
		threadWait(2);
		String locator = prop.getProperty(locatorKey);
		Select s = new Select(getElement(locator));
		s.selectByVisibleText(data);

	}

	public String getText(String locatorKey) {
		String locator = prop.getProperty(locatorKey);
		return getElement(locator).getText();
	}

	// central functions to extract elements
	public WebElement getElement(String locator) {
		// check the presence
		if (!isElementPresent(locator)) {
			// report failure
			log("Element not present " + locator);
		}
		// check the visibility
		if (!isElementVisible(locator)) {
			// report failure
			log("Element not visible " + locator);
		}

		WebElement e = driver.findElement(getLocator(locator));
		log("Element was present " + locator);
		return e;
	}

	public WebElement getElementByText(String locatorText) {

		WebElement e = driver.findElement(By.xpath("//*[text()='" + locatorText + "']"));
		return e;
	}

	public boolean isElementPresent(String locator) {
		log("Checking presence of " + locator);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locator)));

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean isElementVisible(String locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locator)));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public By getLocator(String locator) {
		By by = null;
		log("Looking for element " + locator);
		if (locator.contains("//")) {
			log("Finding on basis of xpath " + locator);

			by = By.xpath(locator);
		} else if (locator.contains(".") || locator.contains("#")) {
			log("Finding on basis of css " + locator);
			by = By.cssSelector(locator);

		} else {
			log("Finding on basis of id " + locator);

			by = By.id(locator);
		}
		return by;

	}

	// reporting functions
	public void log(String msg) {
		test.log(Status.INFO, msg);
	}

	public void reportFailure(String failureMsg, boolean stopOnFailure) {
		test.log(Status.FAIL, failureMsg);// failure in extent reports
		takeScreenShot();// put the screenshot in reports
		softAssert.fail(failureMsg);// failure in TestNG reports

		if (stopOnFailure) {
			Reporter.getCurrentTestResult().getTestContext().setAttribute("criticalFailure", "Y");
			assertAll();// report all the failures
		}
	}

	public void assertAll() {
		softAssert.assertAll();
	}

	public void takeScreenShot() {
		// fileName of the screenshot

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String screenshotFile = dateFormat.format(date).replaceAll(":", "_") + ".png";
		// take screenshot
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + "//" + screenshotFile));
			// put screenshot file in reports

			// test.info("Screenshot-> "+
			// test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath+"//"+screenshotFile));

			test.fail(
					"<p><font color=red>" + " Click the below link or check the latest  report folder named "
							+ ExtentManager.screenshotFolderPath + " and then view the screenshot named "
							+ screenshotFile + "</font></p>",
					MediaEntityBuilder
							.createScreenCaptureFromPath(ExtentManager.screenshotFolderPath + "//" + screenshotFile)
							.build());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void acceptAlert() {
		log("Switching to alert");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.alertIsPresent());
		log("alert is present");
		try {
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			log("Alert accepted successfully");
		} catch (Exception e) {
			reportFailure("Alert not found when mandatory", true);
		}

	}

	// finds the row number of the data
	public int getRowNumWithCellData(String tableLocatorKey, String data) {
		String locator = prop.getProperty(tableLocatorKey);

		if (data.equals("Reliance Industries Ltd."))
			data = "Reliance Inds.";

		log("Checking company in table " + data);
		WebElement table = getElement(locator);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for (int rNum = 0; rNum < rows.size(); rNum++) {
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for (int cNum = 0; cNum < cells.size(); cNum++) {
				WebElement cell = cells.get(cNum);
				if (!cell.getText().trim().equals(""))
					if (data.startsWith(cell.getText()))
						return (rNum + 1);
			}
		}

		return -1; // data is not found
	}

	public void quit() {
		driver.quit();

	}

	public void waitForPageToLoad() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int i = 0;
		// ajax status
		while (i != 10) {
			String state = (String) js.executeScript("return document.readyState;");

			if (state.equals("complete"))
				break;
			else
				threadWait(2);

			i++;
		}
		// check for jquery status
		i = 0;
		while (i != 10) {

			Long d = (Long) js.executeScript("return jQuery.active;");
			if (d.longValue() == 0)
				break;
			else
				threadWait(2);
			i++;

		}

	}

	public void moveToElement(String locatorKey) {
		String locator = prop.getProperty(locatorKey);
		WebElement element = getElement(locator);
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();

	}

	public void threadWait(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
