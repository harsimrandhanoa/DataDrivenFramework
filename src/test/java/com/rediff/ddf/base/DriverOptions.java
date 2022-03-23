package com.rediff.ddf.base;

import java.util.Properties;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverOptions {

	public static ChromeDriver getChromeDriver(Properties prop) {
		String chromeDriverLocation =  System.getProperty("user.dir") +prop.getProperty("chromedriver");
		System.setProperty("webdriver.chrome.driver",chromeDriverLocation);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("disable-infobards");
		ChromeDriver driver = new ChromeDriver(options);
		return driver;
	}

	public static FirefoxDriver getFirefoxDriver(Properties prop) {
		String firefoxDriverLocation =  System.getProperty("user.dir") + prop.getProperty("firefoxdriver");
        System.setProperty("webdriver.gecko.driver",firefoxDriverLocation);
		return new FirefoxDriver();
	}

	public static EdgeDriver getEdgeDriver(Properties prop) {
		String edgeDriverLocation =  System.getProperty("user.dir") + prop.getProperty("edgedriver");
        System.setProperty("webdriver.ie.driver", edgeDriverLocation);
		return new EdgeDriver();
	}

}
