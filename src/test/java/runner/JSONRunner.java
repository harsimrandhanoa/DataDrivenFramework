package runner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class JSONRunner {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		
		Map<String, String> classMethods = new DataUtil().loadClassMethods();

		JSONRunnerHelper jsonRunnerHelper = new JSONRunnerHelper();

		JSONObject json = jsonRunnerHelper.getTestConfigObject();

		TestNGRunner testNG = jsonRunnerHelper.createTestNgRunner(json);//create runner

		JSONArray browsersArray = jsonRunnerHelper.getBrowsersArray(json);

		JSONArray testSuites = jsonRunnerHelper.getSuitesArray(json);

		for (int browserId = 0; browserId < browsersArray.size(); browserId++) {

			String browserName = (String) browsersArray.get(browserId);

			for (int tsId = 0; tsId < testSuites.size(); tsId++) {

				Map<String, String> testSuiteData = jsonRunnerHelper
						.getTestSuiteData((JSONObject) testSuites.get(tsId));
				
				
			   if (!testSuiteData.isEmpty()) {
					jsonRunnerHelper.addSuiteToRunner(testNG, testSuiteData); //add test suite to runner

					if (tsId == 0)
						jsonRunnerHelper.addTestNGListener(testNG); //add listener for very first suite

					JSONArray suiteTestCases = jsonRunnerHelper.getTestCasesFromSuite(testSuiteData);

					for (int sId = 0; sId < suiteTestCases.size(); sId++) {

						Map<String, Object> testCaseData = jsonRunnerHelper
								.getTestCaseData((JSONObject) suiteTestCases.get(sId));

						JSONArray executions = (JSONArray) testCaseData.get("executions");

						for (int eId = 0; eId < executions.size(); eId++) {

							Map<String, Object> executionData = jsonRunnerHelper
									.getExecutionData((JSONObject) executions.get(eId));
							String tRunMode = (String) executionData.get("runmode");

							if (tRunMode != null && tRunMode.equals("Y")) {
								String executionName = (String) executionData.get("name");
								String dataflag = (String) executionData.get("dataFlag");

								String testDataJsonFilePath = System.getProperty("user.dir")
										+ "//src//test//resources//json//" + testSuiteData.get("jsonFilePath");

								int testdatasets = new DataUtil().getTestDataSets(testDataJsonFilePath, dataflag);
								

								// To read from excel file
                              //String testDataXLSFilePath = System.getProperty("user.dir")+"//src//test//resources//json//"+testSuiteData.get("xlsFilePath");
                              //int testdatasets = new ReadDataSample().getTestDataSets(testDataXLSFilePath,dataflag,testSuiteData.get("name"));

								for (int dsId = 0; dsId < testdatasets; dsId++) {
									JSONArray parametervalues = (JSONArray) executionData.get("parameterValues");
									JSONArray methods = (JSONArray) executionData.get("methods");

									testNG.addTest(testCaseData.get("testName") + "-" + executionName + "-It."
											+ (dsId + 1) + "-" + browserName); //adding a test case to suite

									jsonRunnerHelper.addParametersToRunner(testNG,
											(JSONArray) testCaseData.get("parameterNames"), parametervalues);

									testNG.addTestParameter("datafilepath", testDataJsonFilePath);

									// To read from excel
									//testNG.addTestParameter("datafilepath",testDataXLSFilePath);

									testNG.addTestParameter("dataflag", dataflag);
									testNG.addTestParameter("iteration", String.valueOf(dsId));
									testNG.addTestParameter("suitename", testSuiteData.get("name"));
									testNG.addTestParameter("browserName", browserName);

									jsonRunnerHelper.addMethods(testNG, methods, classMethods);

								} //for loop for data sets in portfoliodata or suitedata
							} //if for runmode in executions in portfoliosuite or stocksuite
						}//for loop for executions in portfoiosuite ends
					} //for loop for test cases in portfoliosuite or stocksuite ends
				} 
			}// for loop for test suites ends
		} //for loop for browsers in testconfig ends
		testNG.run();

	}
}
