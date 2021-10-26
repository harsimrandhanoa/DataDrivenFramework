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

import util.ReadDataSample;


public class JSONRunner {
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		
	    Map<String,String> classMethods=new DataUtil().loadClassMethods(); 
		System.out.println(classMethods);
		
		 JSONRunnerHelper jsonRunnerHelper = new JSONRunnerHelper();
		
	     JSONObject json  =  jsonRunnerHelper.getTestConfigObject();
	     
	     TestNGRunner testNG = jsonRunnerHelper.createTestNgRunner(json);
 
	     
		 JSONArray browsersArray = jsonRunnerHelper.getBrowsersArray(json);	

		 JSONArray testSuites = jsonRunnerHelper.getSuitesArray(json);	
		 

		 
		 
		 for(int browserId = 0; browserId < browsersArray.size();browserId++){
			 
			 String browserName = (String) browsersArray.get(browserId);
		 
		 for(int tsId=0;tsId<testSuites.size();tsId++){
			
			Map<String,String> testSuiteData = jsonRunnerHelper.getTestSuiteData((JSONObject)testSuites.get(tsId));

			
	    //     File testDataJSONFile =new File(System.getProperty("user.dir")+"//src//test//resources//json//"+testDataJsonFile);
      
         // To read from excel file
         //  File testDataXLSFile =new File(System.getProperty("user.dir")+"//src//test//resources//json//"+testDataJsonFile);

			if(!testSuiteData.isEmpty()) {
			jsonRunnerHelper.addSuiteToRunner(testNG,testSuiteData);
      

 	      if(tsId==0)
 	    	  jsonRunnerHelper.addTestNGListener(testNG);
			  
 	      JSONArray suiteTestCases = jsonRunnerHelper.getTestCasesFromSuite(testSuiteData);
 		
 		  for(int sId=0;sId<suiteTestCases.size();sId++){
 			  
 			Map<String,Object> testCaseData =     jsonRunnerHelper.getTestCaseData((JSONObject)suiteTestCases.get(sId));
  			 
	  		  JSONArray executions = (JSONArray) testCaseData.get("executions");
	  		  
	  		  for(int eId=0;eId<executions.size();eId++){
  			
	  		     Map<String,Object> executionData = jsonRunnerHelper.getExecutionData((JSONObject)executions.get(eId));
  			     String tRunMode = (String) executionData.get("runmode");
  			   
  			     if(tRunMode!=null&&tRunMode.equals("Y")){
  			      String executionName = (String)executionData.get("name");
	  			  String dataflag = (String)executionData.get("dataFlag");
	  			  
	  			  
	             String testDataJsonFilePath =System.getProperty("user.dir")+"//src//test//resources//json//"+testSuiteData.get("jsonFilePath");

	             int testdatasets  =   new DataUtil().getTestDataSets(testDataJsonFilePath,dataflag);
	             
	             
 			  
                //    To read from excel
               //     int testdatasets  =   new ReadDataSample().getTestDataSets(testdataexcelfile,dataflag,name);
	  	
	  	     for(int dsId=0;dsId<testdatasets;dsId++){
		  		    JSONArray parametervalues = (JSONArray)executionData.get("parameterValues");
		  		    JSONArray methods = (JSONArray)executionData.get("methods");
		  		    
		  		    
      //              System.out.println(" <-----------------------------------------------> ");
        //            System.out.println(tName+"----"+executionName);	
    //                System.out.println(" <-----------------------------------------------> ");

		  //	    	System.out.println(parameternames+"----"+parametervalues);
  //                  System.out.println(" <-----------------------------------------------> ");

		  		
			//	    System.out.println(methods);
//                    System.out.println(" <-----------------------------------------------> ");

							
             		testNG.addTest(testCaseData.get("testName")+"-"+executionName+"-It."+(dsId+1)+"-"+browserName);
            		System.out.println("Test name ,executionName ,dSid---------------> "+testCaseData.get("testName")+"----"+executionName+"---"+dsId);

             		
      			//   System.out.println("The number of parameters are "+parameternames.size());
            		
            		
            		jsonRunnerHelper.addParametersToRunner(testNG,(JSONArray)testCaseData.get("parameterNames"),parametervalues);

             	   //  System.out.println((testDataJsonFilePath));  

             		  
             	   testNG.addTestParameter("datafilepath",testDataJsonFilePath);
             	    
             	  System.out.println("datafilepat---------------> "+testDataJsonFilePath);

    		  			  
    		  			  
    					    //To read from excel
    
          		  		//	  testNG.addTestParameter("datafilepath",testdataexcelfile);
    
     		  			  testNG.addTestParameter("dataflag", dataflag);
     	  			      testNG.addTestParameter("iteration", String.valueOf(dsId));
      	  			      testNG.addTestParameter("suitename",testSuiteData.get("name"));
      	  			      testNG.addTestParameter("browserName",browserName);
      	  			     
      	  				System.out.println("dataflag---------------> "+dataflag);
      	  				System.out.println("iteration---------------> "+String.valueOf(dsId));
      	  				System.out.println("suitename---------------> "+testSuiteData.get("name"));


    	  	 //        System.out.println(dataflag + "=======" +name + "=================" +String.valueOf(dsId));
      	  				jsonRunnerHelper.addMethods(testNG,methods,classMethods);
    	  			
      	  			
                          }
   	  			       }			  			  
 		            }
			     }	
		      } //for loop for testsuites ends
		 }
	}
		testNG.run();
	
    	}
     }
		
	
//	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
//	    System.out.println("**************************************************************************");
//
//	
//		Map<String,String> classMethods=new DataUtil().loadClassMethods(); 
//		System.out.println(classMethods);
//	
//		
//	    System.out.println("**************************************************************************");
//		
//	  
//		String path=System.getProperty("user.dir")+"//src//test//resources//json//testconfig.json";
//		JSONParser parser = new JSONParser();
//		JSONObject json=(JSONObject)parser.parse(new FileReader(new File(path)));
//		
//		String parallelSuites = (String)json.get("parallelsuites");
//		JSONArray testSuites = (JSONArray)json.get("testsuites");
//		TestNGRunner testNG = new TestNGRunner(Integer.parseInt(parallelSuites));
//		
//
//
//		for(int tsId=0;tsId<testSuites.size();tsId++){
//			
//			JSONObject testSuite=(JSONObject)testSuites.get(tsId);
//			
//			String runMode = (String)testSuite.get("runmode");
//			if(runMode.equals("Y")){
//				  String name = (String)testSuite.get("name");
//		
//		//  String testdatajsonfile =System.getProperty("user.dir")+"//src//test//resources//json//"+(String)testSuite.get("testdatajsonfile");
//				  
//			    //To read from excel
//     	 String testdataexcelfile =System.getProperty("user.dir")+"//src//test//resources//"+(String)testSuite.get("testdataxlsfile");
//
//				  
//				  String suitefilename = (String)testSuite.get("suitefilename");
//				  String paralleltests = (String)testSuite.get("paralleltests");
//				
//		//  System.out.println("Runmode: " +runMode+" name: "+name+" testdatajsonfile: "+testdatajsonfile+" suitefilename: "+suitefilename+" paralleltests: "+paralleltests);
//			      
//				    //To read from excel
//				  
//		  System.out.println("Runmode: " +runMode+" name: "+name+" testdataexcelfile: "+testdataexcelfile+" suitefilename: "+suitefilename+" paralleltests: "+paralleltests);
//
//	               boolean pTests = false;
//			      if(paralleltests.equals("Y"))
//					pTests=true;
//				    testNG.createSuite(name, pTests);
//			      if(tsId==0)
//				   testNG.addListener("listeners.MyTestNGListener");
//		    	  String pathSuiteJson  =  System.getProperty("user.dir")+"//src//test//resources//json//"+suitefilename;
//		  		  JSONParser suiteParser = new JSONParser();
//		  		  JSONObject suiteJSON = (JSONObject)suiteParser.parse(new FileReader(new File(pathSuiteJson)));
//		  		  JSONArray suiteTestCases = (JSONArray)suiteJSON.get("testcases");
//		  		  
//		  		  for(int sId=0;sId<suiteTestCases.size();sId++){
//		  			  JSONObject suiteTestCase = (JSONObject)suiteTestCases.get(sId);
//		  			  String tName = (String)suiteTestCase.get("name");
//		  			  JSONArray parameternames = (JSONArray)suiteTestCase.get("parameternames");
//			  		  JSONArray executions = (JSONArray)suiteTestCase.get("executions");
//			  		  
//			  		  for(int eId=0;eId<executions.size();eId++){
//		  			  JSONObject execution = (JSONObject)executions.get(eId);
//		  			  String tRunMode = (String)execution.get("runmode");
//		  			
//		  			  
//		  			 if(tRunMode!=null&&tRunMode.equals("Y")){
//		  			  String executionName = (String)execution.get("executionname");
//		  			  String dataflag = (String)execution.get("dataflag");
//		  			  
//		  	//	      int testdatasets  =   new DataUtil().getTestDataSets(testdatajsonfile,dataflag);
//		  			  
//					    //To read from excel
//		  			  
//		       int testdatasets  =   new ReadDataSample().getTestDataSets(testdataexcelfile,dataflag,name);
//		       
//		       
//		 
//		  	      
//		  	          for(int dsId=0;dsId<testdatasets;dsId++){
//		  			  JSONArray parametervalues = (JSONArray)execution.get("parametervalues");
//		  			  JSONArray methods = (JSONArray)execution.get("methods");
//		  			  
//		  		  
//		  		            System.out.println(tName+"----"+executionName);
//							System.out.println(parameternames+"----"+parametervalues);
//							System.out.println(methods);
//							
//							testNG.addTest(tName+"-"+executionName+"-It."+(dsId+1));
//		  		 
//		  		 
//
//		  			  
//
//
//		  			  for(int pId = 0;pId<parameternames.size();pId++){
//		  			  testNG.addTestParameter((String)parameternames.get(pId),(String)parametervalues.get(pId)); }
//	  	    		 
//		  			//  testNG.addTestParameter("datafilepath",testdatajsonfile);
//		  			  
//		  			  
//					    //To read from excel
//
//		  			  testNG.addTestParameter("datafilepath",testdataexcelfile);
//
//		  			  testNG.addTestParameter("dataflag", dataflag);
//	  			      testNG.addTestParameter("iteration", String.valueOf(dsId));
//	  			      testNG.addTestParameter("suitename",name);
//	  			      
//	              //      System.out.println(" <=============================================================> ");
//	                    
//	                //    System.out.println(name + "=======" +dataflag + "=================" +testdatajsonfile);
//
//
//
//		  			List<String> includedMethods = new ArrayList<String>();  
//                    for(int mId=0;mId<methods.size();mId++) {
//			        String method = (String)methods.get(mId);
//			       String methodClass=classMethods.get(method);
//                //  System.out.println(method +" <-------------------------------------------------------> "+ methodClass);
//                         
//                  if(mId==methods.size()-1 || !((String)classMethods.get((String)methods.get(mId+1))).equals(methodClass)) {
//           				  // next method is from different class
//                        includedMethods.add(method);
//           			   testNG.addTestClass(methodClass, includedMethods);
//           			   System.out.println("-------------------------->"+includedMethods);
//           			
//           			   includedMethods = new ArrayList<String>();;
//           	   	 }else {
//           		     	   	 // same class
//           					includedMethods.add(method);
//            			 }	 
//            		 }
//	        	 }	     
//		      }
//		  			  
//		   } 
//		      
//	     }
//      }
//    }
//	//	testNG.run();
//  }		
//}		
		
		
		
	
		
		
		
		
		
		
