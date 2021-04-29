package com.rediff.ddf.newtestcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.rediff.ddf.base.BaseTest;

public class PortfolioManagement extends BaseTest {
	
	@Test
	public void createPortfolio(ITestContext context) {
		////JSONObject data = (JSONObject)context.getAttribute("data");
		String portfolioName= "Ashi_11";
		////(String)data.get("portfolioname");

		app.log("Creating Profolio");
		app.click("createPortfolio_id");
		app.clear("porfolioname_id");
		app.type("porfolioname_id", portfolioName);
		app.click("createPortfolioButton_css");
		app.waitForPageToLoad();
		app.validateSelectedValueInDropDown("portfolioid_dropdown_id",portfolioName);
	}
	
	
	@Test
	public void deletePortfolio(ITestContext context) {
	//	JSONObject data = (JSONObject)context.getAttribute("data");
		String portfolioName= "Abc23336";
		app.log("Deleting Profolio");
        app.selectByVisibleText("portfolioid_dropdown_id", portfolioName);
         app.waitForPageToLoad();
        app.click("deletePortfolio_id");
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        app.acceptAlert();
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        app.validateSelectedValueNotInDropDown("portfolioid_dropdown_id",portfolioName);
	}
	
	@Test
	public void selectPortfolio(ITestContext context) {
		
	//	JSONObject data = (JSONObject)context.getAttribute("data");

		String portfolioName="Ashi_11";		
		app.log("Selecting Profolio");
		app.selectByVisibleText("portfolioid_dropdown_id", portfolioName);
	}

}


