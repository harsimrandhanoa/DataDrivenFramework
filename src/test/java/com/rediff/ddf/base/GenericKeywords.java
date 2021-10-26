package com.rediff.ddf.base;

import java.io.File;
import java.io.IOException;
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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
		log("Opening The Browser "+ browser);
		
		if(prop.get("grid_run").equals("Y")){
			driver = openBrowserOnGrid(browser);
	    }
		  
		else {
			
			if(browser.equals("Chrome")) {
				driver  = DriverOptions.getChromeDriver(prop);
		    
			}else if(browser.equals("Mozilla")) {
				driver  = DriverOptions.getFirefoxDriver(prop);
			  threadWait(50);

			}else if(browser.equals("Edge")) {
				driver  = DriverOptions.getEdgeDriver(prop);
            }
		}
        threadWait(5);
		// implicit wait
         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
  		//driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
	}
	
	public WebDriver openBrowserOnGrid(String browser){
		log("Tests to be run on selenium grid");
		ChromeOptions chromeOptions = new ChromeOptions();
		FirefoxOptions firefoxOptions  = new FirefoxOptions();



		DesiredCapabilities cap=new DesiredCapabilities();
		if(browser.equals("Mozilla")){
			
			
		//	driver = new RemoteWebDriver(new URL("http://192.168.56.1:4444/"), firefoxOptions);

			
		}else if(browser.equals("Chrome")){

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
		log("Navigating to "+ urlKey);
		driver.get(envProp.getProperty(urlKey));
	}
	
	public void click(String locatorKey) {
		log("Clicking on "+locatorKey);
		getElement(locatorKey).click();// not present, not visible
	}
	
	public void type(String locatorKey,String data) {
		log("Typing in "+locatorKey+" . Data "+ data);
		getElement(locatorKey).sendKeys(data);
	}
	public void clear(String locatorKey) {
		log("Clearing text field "+ locatorKey);
		getElement(locatorKey).clear();
	}
	
	public void clickEnterButton(String locatorText) {
		log("Clinking enter button");
	//	getElementByText(locatorText).click();;
	}
	
	public void selectByVisibleText(String locatorKey, String data) {
		threadWait(5);
		Select s = new Select(getElement(locatorKey));
		s.selectByVisibleText(data);
     }
	
	
    public String getText(String locatorKey) {
		return getElement(locatorKey).getText();
	}
	
	// central functions to extract elements
	public WebElement getElement(String locatorKey) {
		//  check the presence
		if(!isElementPresent(locatorKey)) {
			// report failure
			log("Element not present "+locatorKey);
		}
		//  check the visibility
		if(!isElementVisible(locatorKey)) {
			// report failure
			log("Element not visible "+locatorKey);
		}
			
		WebElement e = driver.findElement(getLocator(locatorKey));
		     log("Element was present "+locatorKey);
        return e;
	}
	
	public WebElement getElementByText(String locatorText) {
		
			
		WebElement e = driver.findElement(By.xpath("//*[text()='"+locatorText+"']"));
		
		return e;
	}
	
	
	public boolean isElementPresent(String locatorKey) {
		log("Checking presence of "+locatorKey);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locatorKey)));
			
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	
	public boolean isElementVisible(String locatorKey) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locatorKey)));
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public By getLocator(String locatorKey) {
		By by=null;
		
		if(locatorKey.endsWith("_id"))
			by = By.id(prop.getProperty(locatorKey));
		else if(locatorKey.endsWith("_xpath"))
			by = By.xpath(prop.getProperty(locatorKey));
		else if(locatorKey.endsWith("_css")){
			by = By.cssSelector(prop.getProperty(locatorKey));
		}
		else if(locatorKey.endsWith("_name"))
			by = By.name(prop.getProperty(locatorKey));
		
		return by;
		
		
	}
	
	
	
	//reporting functions
	public void log(String msg) {
		test.log(Status.INFO, msg);
	}
	
	public void reportFailure(String failureMsg, boolean stopOnFailure) {
		test.log(Status.FAIL, failureMsg);// failure in extent reports
	 	takeScreenShot();// put the screenshot in reports
		softAssert.fail(failureMsg);// failure in TestNG reports
		
		if(stopOnFailure) {
			Reporter.getCurrentTestResult().getTestContext().setAttribute("criticalFailure", "Y");
		    assertAll();// report all the failures
		}
     }
	
	public void assertAll() {
		softAssert.assertAll();
	}
	
	public void takeScreenShot(){
		// fileName of the screenshot
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String screenshotFile = dateFormat.format(date).replaceAll(":", "_")+".png";
		// take screenshot
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath+"//"+screenshotFile));
			//put screenshot file in reports
			
		//test.info("Screenshot-> "+ test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath+"//"+screenshotFile));
		
		
			 test.fail("<p><font color=red>"
                     + " Click the below link or check the latest  report folder named "+ExtentManager.screenshotFolderPath+" and then view the screenshot named "+screenshotFile
                     + "</font></p>", MediaEntityBuilder.createScreenCaptureFromPath(ExtentManager.screenshotFolderPath+"//"+screenshotFile).build());

			 
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void acceptAlert(){
		log("Switching to alert");
		threadWait(5);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.alertIsPresent());
		try{
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			log("Alert accepted successfully");
		}catch(Exception e){
            reportFailure("Alert not found when mandatory",true);
		}
		
	}

	// finds the row number of the data
	public int getRowNumWithCellData(String tableLocator, String data) {
		
		WebElement table = getElement(tableLocator);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for(int rNum=0;rNum<rows.size();rNum++) {
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for(int cNum=0;cNum<cells.size();cNum++) {
				WebElement cell = cells.get(cNum);
				if(!cell.getText().trim().equals(""))
					if(data.startsWith(cell.getText()))
						return(rNum+1);
			}
		}
		
		return -1; // data is not found
	}
	

	public void quit() {
		driver.quit();
		
	}
	
	public void waitForPageToLoad(){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		int i=0;
		// ajax status
		while(i!=10){
		String state = (String)js.executeScript("return document.readyState;");

		if(state.equals("complete"))
			break;
		else
			threadWait(2);

		i++;
		}
		// check for jquery status
		i=0;
		while(i!=10){
	
			Long d= (Long) js.executeScript("return jQuery.active;");
			if(d.longValue() == 0 )
			 	break;
			else
				 threadWait(2);
			 i++;
				
			}
		
		}
	
	public void threadWait(int time) {
		try {
			Thread.sleep(time*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	
 


}
