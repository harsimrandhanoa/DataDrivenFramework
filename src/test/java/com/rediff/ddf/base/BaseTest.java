package com.rediff.ddf.base;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import reporting.ExtentManager;
import runner.DataUtil;
import util.ReadDataSample;

public class BaseTest {
	public ApplicationKeywords app;
	public ExtentReports rep;
	public ExtentTest test;

	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext context)
			throws NumberFormatException, FileNotFoundException, IOException, ParseException {

		String dataFilePath = context.getCurrentXmlTest().getParameter("datafilepath");
		String dataFlag = context.getCurrentXmlTest().getParameter("dataflag");
		String iteration = context.getCurrentXmlTest().getParameter("iteration");
		String sheetName = context.getCurrentXmlTest().getParameter("suitename");
		String browserName = context.getCurrentXmlTest().getParameter("browserName");

		JSONObject data = new DataUtil().getTestData(dataFilePath, dataFlag, Integer.parseInt(iteration));

		// To read data from excel

		// JSONObject data = new ReadDataSample().excelData(dataFilePath,sheetName,dataFlag,(Integer.parseInt(iteration)+1));

		context.setAttribute("data", data);

		String runmode = (String) data.get("runmode");

		rep = ExtentManager.getReports(); // made object of rep
		test = rep.createTest(context.getCurrentXmlTest().getName()); // made object of test
		test.log(Status.INFO, "Starting Test " + context.getCurrentXmlTest().getName());
		test.log(Status.INFO, "Data " + data.toString());

		context.setAttribute("report", rep); // set rep and test objects in context
		context.setAttribute("test", test);

		if (runmode.equals("N")) {

			test.log(Status.SKIP, "Skipping as runmod is N");
			throw new SkipException("Skipping as runmod is N");

		}

		app = new ApplicationKeywords(); // 1 app keyword object for entire test -All @Test
		app.setReport(test); // passed the test object created above to ApplicationKeywords Class
		context.setAttribute("app", app); // set object of app
		app.defaultLogin(browserName);

	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(ITestContext context) {

		String criticalFailure = (String) context.getAttribute("criticalFailure");
		if (criticalFailure != null && criticalFailure.equals("Y")) {
			throw new SkipException("Critical Failure in Prevoius Tests");// skip in testNG
		}

		// Use these variables set in before test in each method

		app = (ApplicationKeywords) context.getAttribute("app");
		test = (ExtentTest) context.getAttribute("test");
		rep = (ExtentReports) context.getAttribute("report");
	}

	@AfterTest(alwaysRun = true)
	public void quit(ITestContext context) {

		app = (ApplicationKeywords) context.getAttribute("app");
		if (app != null)
			app.quit();

		rep = (ExtentReports) context.getAttribute("report");

		if (rep != null)
			rep.flush();
	}

}
