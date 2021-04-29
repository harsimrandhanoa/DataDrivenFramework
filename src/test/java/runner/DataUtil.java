package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataUtil {

	public Map<String,String> loadClassMethods() throws FileNotFoundException, IOException, ParseException{
		
		
		String path=System.getProperty("user.dir")+"//src//test//resources//json//classmethods.json";
		Map<String,String> classMethodsMap = new HashMap<String,String>();

		JSONParser parser = new JSONParser();
		JSONObject json=(JSONObject)parser.parse(new FileReader(new File(path)));
		JSONArray classDetails = (JSONArray)json.get("classdetails");
		for(int cmId=0;cmId<classDetails.size();cmId++) {
			JSONObject classDetail = (JSONObject)classDetails.get(cmId);
			String className = (String)classDetail.get("class");
		//	System.out.println(className);
			JSONArray methods  = (JSONArray)classDetail.get("methods");
			for(int mId=0;mId<methods.size();mId++) {
				String method = (String)methods.get(mId);
			//	System.out.println(method);
				classMethodsMap.put(method,className);
			}
		//	System.out.println("--------------------------------------------------------------");
        }
           return classMethodsMap;		
	}
	
	
}
