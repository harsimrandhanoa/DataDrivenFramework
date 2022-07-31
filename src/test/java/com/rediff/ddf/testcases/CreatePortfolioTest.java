package com.rediff.ddf.testcases;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.rediff.ddf.base.ApplicationKeywords;

public class CreatePortfolioTest {
	ApplicationKeywords app;

	@Test
	public void createPortFolioTest() {

//		app = new ApplicationKeywords();// init prop
//
//		app.openBrowser("Chrome");
//		app.navigate("url");
//		app.type("username_css", "abc");
//		app.type("password_css", "123");
//		app.validateElementPresent("loginButton_css");
//		app.click("loginButton_css");
//		app.validateLogin();
	}

	@AfterMethod
	public void quit() {
		app.quit();
	}

	

}
