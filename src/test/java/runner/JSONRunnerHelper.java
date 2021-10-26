package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONRunnerHelper {
	
	public  String getJsonFolderPath(){
		return System.getProperty("user.dir")+"//src//test//resources//json//";
    }
	
	public JSONObject getTestConfigObject(){
		
		File file = new File(getJsonFolderPath()+"testconfig.json");//put this in 
		//config file
		JSONParser parser = new JSONParser();
		
		try {
			return  (JSONObject)parser.parse(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	
    public TestNGRunner createTestNgRunner(JSONObject json){
		String parallelSuites = (String)json.get("parallelsuites");
		System.out.println("Parallel Tests---------------> "+parallelSuites);
        return new TestNGRunner(Integer.parseInt(parallelSuites));
	}
    
    public JSONArray getBrowsersArray(JSONObject json){ 
		return 	(JSONArray)json.get("browsers");
    }
	
	public JSONArray getSuitesArray(JSONObject json){ 
		return 	(JSONArray)json.get("testsuites");
    }
	
	
	public Map<String,String>  getTestSuiteData(JSONObject testSuite){
	String runMode = (String)testSuite.get("runmode");
	Map<String,String> testSuiteData = new HashMap<String,String>();

	if(runMode.equals("Y")){
		
       	testSuiteData.put("name", (String)testSuite.get("name"));
		testSuiteData.put("jsonFilePath", (String)testSuite.get("testdatajsonfile"));
		testSuiteData.put("xlsFilePath", (String)testSuite.get("testdataxlsfile"));
		testSuiteData.put("suiteFileName",(String)testSuite.get("suitefilename"));
		testSuiteData.put("runmode",(String)testSuite.get("runmode"));
		testSuiteData.put("parallelTests",(String)testSuite.get("paralleltests"));
		
	}
	System.out.println(testSuiteData);
    return testSuiteData;
	}
	
	public void addSuiteToRunner(TestNGRunner testNG,Map<String,String> suiteData){
		
		

	 boolean pTests = false;
     String parallelTests = suiteData.get("parallelTests");
     System.out.println(suiteData);
     String name = suiteData.get("name");
      if(parallelTests.equals("Y"))
	   pTests=true;
	 testNG.createSuite(name, pTests);
	 System.out.println("name and pTest---------------> "+name+ "------"+pTests);
		
	}	
	
	public void addTestNGListener(TestNGRunner testNG){
		
		System.out.println("Adding testng listener for the first time");

      testNG.addListener("listeners.MyTestNGListener");

	}	
	
	public JSONArray getTestCasesFromSuite(Map<String,String> testSuiteData){
		 JSONArray suiteTestCases = null;


	 String pathSuiteJson  = getJsonFolderPath()+testSuiteData.get("suiteFileName");
	 System.out.println("The path suite is "+pathSuiteJson);
	 JSONParser suiteParser = new JSONParser();
	 JSONObject suiteJSON;
	try {
		suiteJSON = (JSONObject)suiteParser.parse(new FileReader(pathSuiteJson));
		suiteTestCases =  (JSONArray)suiteJSON.get("testcases");

	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	 System.out.println("The size of suites is "+suiteTestCases.size());
      return suiteTestCases;
	}	 
	
	public Map<String,Object> getTestCaseData(JSONObject suiteTestCase){
		
		Map<String,Object> testCaseData   =  new HashMap<String,Object>();
		testCaseData.put("testName",(String)suiteTestCase.get("name"));
		testCaseData.put("parameterNames",suiteTestCase.get("parameternames"));
		testCaseData.put("executions",suiteTestCase.get("executions"));
		
		System.out.println("Test name "+testCaseData.get("testName"));
		System.out.println("Parameters name "+testCaseData.get("parameterNames"));
		System.out.println("Executions "+testCaseData.get("executions"));

		
		return testCaseData;
	}	
	
	
	public Map<String,Object> getExecutionData(JSONObject execution){
		
		Map<String,Object> executionData   =  new HashMap<String,Object>();
		
		executionData.put("runmode", (String)execution.get("runmode"));
		executionData.put("name", (String)execution.get("executionname"));
		executionData.put("dataFlag",(String)execution.get("dataflag"));
		executionData.put("parameterValues",execution.get("parametervalues"));
 		executionData.put("methods",execution.get("methods"));
 		System.out.println("Execution data is:- \n"+executionData);
		
		return executionData;

	
		
	}

	public void addParametersToRunner(TestNGRunner testNG,JSONArray parameterNames,JSONArray parameterValues){
		
		System.out.println("The numner of paramters are "+parameterNames.size());

	   for(int pId = 0;pId<parameterNames.size();pId++){    
		 testNG.addTestParameter((String)parameterNames.get(pId),(String)parameterValues.get(pId));
		 System.out.println("Paramter names and values"+(String)parameterNames.get(pId)+"==============="+(String)parameterValues.get(pId));  
		   
	  }
	}
	
	public void addMethods(TestNGRunner testNG,JSONArray methods, Map<String,String> classMethods){

	 List<String> includedMethods = new ArrayList<String>();  
	 
	 System.out.println("---------===========================-----------------------------================================");
		
	    for(int mId=0;mId<methods.size();mId++) {
           String method = (String)methods.get(mId);
           String methodClass=classMethods.get(method);
			     System.out.println(method+"-------------------------->"+methodClass);

           if(mId==methods.size()-1 || !((String)classMethods.get((String)methods.get(mId+1))).equals(methodClass)) {
//			  // next method is from different class
                 includedMethods.add(method);
		             testNG.addTestClass(methodClass, includedMethods);
	  				System.out.println("if methhodClass---------------> "+methodClass);
	  				System.out.println("if includeMethods---------------> "+includedMethods);


		          
                 includedMethods = new ArrayList<String>();;
           } 
           else {
//	     	   	 // same class
	  				System.out.println("if else");
	  				includedMethods.add(method); }	 
	  
         }
	}	
}
