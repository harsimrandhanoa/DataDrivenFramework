package com.rediff.ddf.base;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import reporting.ExtentManager;

public class BaseTest {
	public ApplicationKeywords app;
	public ExtentReports rep;
	public ExtentTest test;
	
	
	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext context) throws NumberFormatException, FileNotFoundException, IOException {
		System.out.println("----------Before Test will run before all the tests ---------");
		String dataFlag = context.getCurrentXmlTest().getParameter("dataflag");
		System.out.println("The data flag in before test is "+dataFlag);
		
		rep = ExtentManager.getReports();//made object of rep
		System.out.println("The name is test is "+context.getCurrentXmlTest().getName());
		test =rep.createTest(context.getCurrentXmlTest().getName()); //made object of test
		test.log(Status.INFO, "Starting Test "+context.getCurrentXmlTest().getName());
		
		context.setAttribute("report", rep); //set rep and test objects in context
		context.setAttribute("test", test);
		
		app = new ApplicationKeywords(); // 1 app keyword object for entire test -All @Test
        app.setReport(test); //passed the test  object created above to ApplicationKeywords Class
		app.defaultLogin();		
        context.setAttribute("app", app); //set object of app
		
        /*String datafilpath = context.getCurrentXmlTest().getParameter("datafilpath");
		String dataFlag = context.getCurrentXmlTest().getParameter("dataflag");
		String iteration = context.getCurrentXmlTest().getParameter("iteration");
		String sheetName = context.getCurrentXmlTest().getParameter("suitename");
		// suitename(sheetname)
		System.out.println(datafilpath);
		System.out.println(dataFlag);
		System.out.println(iteration);
		System.out.println(sheetName);*/
		// reading data from JSON
		//JSONObject data = new DataUtil().getTestData(datafilpath, dataFlag, Integer.parseInt(iteration));
	//	JSONObject data = new ReadingXLS().getTestData(sheetName, dataFlag, (Integer.parseInt(iteration)+1), datafilpath);
		//JSONObject data = from xls
	/*	context.setAttribute("data", data);
		String runmode = (String)data.get("runmode");*/
		
		// what is the path to data json / xls
		// what is the data flag
		// what is the iteration number
		// read the data and keep it in a map
		
		
		// init the reporting for the test
	/*	rep = ExtentManager.getReports();
		test =rep.createTest(context.getCurrentXmlTest().getName());
		test.log(Status.INFO, "Starting Test "+context.getCurrentXmlTest().getName());
		test.log(Status.INFO, "Data "+data.toString());

		context.setAttribute("report", rep);
		context.setAttribute("test", test);
        if(!runmode.equals("Y")) {
        	test.log(Status.SKIP, "Skpping as Data Runmode is N");
        	throw new SkipException("Skpping as Data Runmode is N");
		}*/
		
		
		
		
		// init and share it with all tests
	//	app = new ApplicationKeywords(); // 1 app keyword object for entire test -All @Test
      //  app.setReport(test);
		
	//	app.openBrowser("Chrome");
        //app.defaultLogin();		
		
	//	context.setAttribute("app", app);
		
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(ITestContext context) {
		System.out.println("****Before Method will run before all tests ****");

		
		String criticalFailure = (String)context.getAttribute("criticalFailure");
		if(criticalFailure != null && criticalFailure.equals("Y")) {
			test.log(Status.SKIP, "Critical Failure in Prevoius Tests");
			throw new SkipException("Critical Failure in Prevoius Tests");// skip in testNG
		}
		  //Use these variables set in before test in each method 
	     app = (ApplicationKeywords)context.getAttribute("app");
		 test = (ExtentTest)context.getAttribute("test");
		 rep = (ExtentReports)context.getAttribute("report");
	}
		
/*
		
*/
	
	@AfterTest(alwaysRun = true)
	public void quit(ITestContext context) {
		app = (ApplicationKeywords)context.getAttribute("app");
		if(app!=null)
			app.quit();
		
		rep = (ExtentReports)context.getAttribute("report");

		if(rep !=null)
			rep.flush();
	}

	


}
