package com.rediff.ddf.newtestcases;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import com.rediff.ddf.base.BaseTest;

public class PortfolioManagement extends BaseTest {

	@Test
	public void createPortfolio(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String portfolioName = (String) data.get("portfolioname");

		app.threadWait(2);
		app.log("Creating Profolio");
		app.click("createPortfolio_id");
		app.clear("porfolioname_id");
		app.type("porfolioname_id", portfolioName);
		app.click("createPortfolioButton_css");
		app.waitForPageToLoad();
		app.validateSelectedValueInDropDown("portfolioid_dropdown_id", portfolioName);
	}

	@Test
	public void deletePortfolio(ITestContext context) throws InterruptedException {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String portfolioName = (String) data.get("portfolioname");
		app.log("Deleting Profolio");
		app.selectByVisibleText("portfolioid_dropdown_id", portfolioName);
		app.waitForPageToLoad();
		app.click("deletePortfolio_id");
		app.threadWait(2);
		app.acceptAlert();
		app.threadWait(2);
		app.validateSelectedValueNotInDropDown("portfolioid_dropdown_id", portfolioName);
	}

	@Test
	public void selectPortfolio(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String portfolioName = (String) data.get("portfolioname");
		app.log("Selecting Profolio " + portfolioName);
		app.selectByVisibleText("portfolioid_dropdown_id", portfolioName);

	}

}
