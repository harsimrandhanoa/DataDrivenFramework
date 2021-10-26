package com.rediff.ddf.base;

import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
public class ValidationKeywords extends GenericKeywords {
	
	public void validateTitle() {
		log("Validating title");
	}
	
	public void validateText() {
		log("Validating text");
   }
	
	public void validateElementPresent(String locator) {
		// failure
		boolean result  = isElementPresent(locator);
		//reportFailure("Element not found "+ locator,true);
	}
	
	public void validateLogin() {
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.urlToBe("https://portfolio.rediff.com/portfolio"));
	}
	
	public void validateSelectedValueInDropDown(String locatorKey, String option) {
		Select s = new Select(getElement(locatorKey));
		String text = s.getFirstSelectedOption().getText();
		if(!text.equals(option)){
			reportFailure("Option"+option+" not present in Drop Down "+locatorKey,true);
		}
		
	}
	
	public void validateSelectedValueNotInDropDown(String locatorKey, String option) {
		Select s = new Select(getElement(locatorKey));
		String text = s.getFirstSelectedOption().getText();
		if(text.equals(option)){
			reportFailure("Option"+option+" present in Drop Down "+locatorKey,true);
		}
		
	}
	
	public void verifyStockPresent(String companyName) {
	/*	int row = getRowNumWithCellData("stocktable_css", companyName);
		if(row ==-1)
			reportFailure("Stock Not present "+companyName, true);
		
		   log("Stock Found in list "+companyName );
		*/
	}

}

