package com.rediff.ddf.newtestcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.rediff.ddf.base.ApplicationKeywords;
import com.rediff.ddf.base.BaseTest;

public class Session extends BaseTest{
	
	@Test
	public void doLogin(ITestContext context) {
		ApplicationKeywords app = (ApplicationKeywords) context.getAttribute("app");
		app.log("Logging In");
        app.openBrowser("Chrome");
        
		app.navigate("url");
		app.type("username_css", "ashishthakur1983");

		app.type("password_css", "pass@1234");
		app.validateElementPresent("loginButton_css");
		app.click("loginButton_css");
		app.validateLogin();
		
		
	}
	
	/*@Test
	public void doLogout() {
	//	test.log(Status.INFO, "Logging out");
	}*/
}