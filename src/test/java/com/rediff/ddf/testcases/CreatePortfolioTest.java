package com.rediff.ddf.testcases;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.rediff.ddf.base.ApplicationKeywords;

public class CreatePortfolioTest {
	 ApplicationKeywords app;
	

    @Test
	public void createPortFolioTest() {
	   // no webdriver code
       // login
	   // create
	   // verify
		
	   app = new ApplicationKeywords();// init prop
	   
	   app.openBrowser("Chrome");
	   app.navigate("url");
	   app.type("username_css", "ashishthakur1983");
	   app.type("password_css", "pass@1234");
	   app.validateElementPresent("loginButton_css");
	   app.click("loginButton_css");
	   app.validateLogin();
	   //app.selectDateFromCalendar();	
	}
    
    @AfterMethod
    public void quit(){
 	   app.quit();
 }
	
	/*@Test
	public void createReservation() {
		
	}*/
	



}
