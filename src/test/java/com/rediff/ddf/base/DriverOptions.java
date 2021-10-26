package com.rediff.ddf.base;

import java.util.Properties;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverOptions {

	
	public static ChromeDriver getChromeDriver(Properties prop){
		System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver"));
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("disable-infobards");
        ChromeDriver driver = new ChromeDriver(options);
		return driver;
	}
	
	public static FirefoxDriver getFirefoxDriver(Properties prop){
        System.setProperty("webdriver.gecko.driver", prop.getProperty("firefoxdriver"));
	    return new FirefoxDriver();
	}
	
	public static EdgeDriver getEdgeDriver(Properties prop){
	    System.setProperty("webdriver.ie.driver", prop.getProperty("edgedriver"));
		return new EdgeDriver();
	}
	

}
