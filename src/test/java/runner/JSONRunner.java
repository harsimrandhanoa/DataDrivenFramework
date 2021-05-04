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

public class JSONRunner {
	
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		Map<String,String> classMethods=new DataUtil().loadClassMethods();
//		System.out.println(classMethods);
	
		
	//	System.out.println("************************************************************************************************************");
		
		String path=System.getProperty("user.dir")+"//src//test//resources//json//testconfig.json";
		JSONParser parser = new JSONParser();
		JSONObject json=(JSONObject)parser.parse(new FileReader(new File(path)));
		
		String parallelSuites = (String)json.get("parallelsuites");
		JSONArray testSuites = (JSONArray)json.get("testsuites");
		TestNGRunner testNG = new TestNGRunner(Integer.parseInt(parallelSuites));
		


		for(int tsId=0;tsId<testSuites.size();tsId++){
			
			JSONObject testSuite=(JSONObject)testSuites.get(tsId);
			
			String runMode = (String)testSuite.get("runmode");
			if(runMode.equals("Y")){
				  String name = (String)testSuite.get("name");
				  String testdatajsonfile =System.getProperty("user.dir")+"//src//test//resources//json//"+(String)testSuite.get("testdatajsonfile");
				  String suitefilename = (String)testSuite.get("suitefilename");
				  String paralleltests = (String)testSuite.get("paralleltests");
				  System.out.println("Runmode: " +runMode+" name: "+name+" testdatajsonfile: "+testdatajsonfile+" suitefilename: "+suitefilename+" paralleltests: "+paralleltests);
			      boolean pTests = false;
			      if(paralleltests.equals("Y"))
					pTests=true;
				    testNG.createSuite(name, pTests);
			      
		    	  testNG.addListener("listeners.MyTestNGListener");
		    	  String pathSuiteJson  =  System.getProperty("user.dir")+"//src//test//resources//json//"+suitefilename;
		  		  JSONParser suiteParser = new JSONParser();
		  		  JSONObject suiteJSON = (JSONObject)suiteParser.parse(new FileReader(new File(pathSuiteJson)));
		  		  JSONArray suiteTestCases = (JSONArray)suiteJSON.get("testcases");
		  		  
		  		  for(int sId=0;sId<suiteTestCases.size();sId++){
		  			  JSONObject suiteTestCase = (JSONObject)suiteTestCases.get(sId);
		  			  String tName = (String)suiteTestCase.get("name");
		  			  JSONArray parameternames = (JSONArray)suiteTestCase.get("parameternames");
			  		  JSONArray executions = (JSONArray)suiteTestCase.get("executions");
			  		  
			  		  for(int eId=0;eId<executions.size();eId++){
		  			  JSONObject execution = (JSONObject)executions.get(eId);
		  			  String tRunMode = (String)execution.get("runmode");
		  			
		  			  
		  			 if(tRunMode!=null&&tRunMode.equals("Y")){
		  			  String executionName = (String)execution.get("executionname");
		  			  String dataflag = (String)execution.get("dataflag");
		  			  
		  		      int testdatasets  =   new DataUtil().getTestDataSets(testdatajsonfile,dataflag);
		  		      
		  		      for(int dsId=0;dsId<testdatasets;dsId++){
		  			  JSONArray parametervalues = (JSONArray)execution.get("parametervalues");
		  			  JSONArray methods = (JSONArray)execution.get("methods");
		  			  
		  		  
		  		            System.out.println(tName+"----"+executionName);
							System.out.println(parameternames+"----"+parametervalues);
							System.out.println(methods);
							
							testNG.addTest(tName+"-"+executionName+"-It."+(dsId+1));
		  		 
		  		 

		  			  


		  			  for(int pId = 0;pId<parameternames.size();pId++){
		  			  testNG.addTestParameter((String)parameternames.get(pId),(String)parametervalues.get(pId)); }
	  	    		 
		  			  testNG.addTestParameter("datafilepath",testdatajsonfile);
	  		    	  testNG.addTestParameter("dataflag", dataflag);
	  			     testNG.addTestParameter("iteration", String.valueOf(dsId));
	  			      testNG.addTestParameter("suitename",name);
	  			      
	              //      System.out.println(" <=============================================================> ");
	                    
	                //    System.out.println(name + "=======" +dataflag + "=================" +testdatajsonfile);



		  			List<String> includedMethods = new ArrayList<String>();  
                    for(int mId=0;mId<methods.size();mId++) {
			        String method = (String)methods.get(mId);
			       String methodClass=classMethods.get(method);
                //  System.out.println(method +" <-------------------------------------------------------> "+ methodClass);
                         
                  if(mId==methods.size()-1 || !((String)classMethods.get((String)methods.get(mId+1))).equals(methodClass)) {
           				  // next method is from different class
                        includedMethods.add(method);
           			   testNG.addTestClass(methodClass, includedMethods);
           			   
           			   
           			
           			   System.out.println("-------------------------->"+includedMethods);
           			
           			   includedMethods = new ArrayList<String>();;
           	   	 }else {
           		     	   	 // same class
           					includedMethods.add(method);
            			 }	 
            		 }
	        	 }	     
		      }
		  			  
		   } 
		      
	     }
      }
    }
		//System.out.println("Here");
		testNG.run();
  }		
}		
		
		
		
	
		
		
		
		
		
		
