package runner;

import java.util.ArrayList;
import java.util.List;

public class Runner {

	public static void main(String[] args) {
		
      TestNGRunner testNG = new TestNGRunner(1);
		testNG.createSuite("Stock Management",false);
		testNG.addListener("listeners.MyTestNGListener");
		
		for(int i=1;i<3;i++){

		testNG.addTest("Add new Stock test. Iteration "+i);
		
		testNG.addTestParameter("action","addstock");
		List<String> includedMethods = new ArrayList<String>();
		includedMethods.add("selectPortfolio");
		testNG.addTestClass("com.rediff.ddf.newtestcases.PortfolioManagement", includedMethods);
		
		
		
		includedMethods = new ArrayList<String>();
		includedMethods.add("addNewStock");
		includedMethods.add("verifyStockPresent");
		includedMethods.add("verifyStockQuantity");
		includedMethods.add("verifyTransactionHistory");
		testNG.addTestClass("com.rediff.ddf.newtestcases.StockManagement", includedMethods);
		
		}
		
		testNG.run();

		
	
	/*	       TestNGRunner testNG = new TestNGRunner(1);
				testNG.createSuite("Portfolio Management",false);
				testNG.addListener("listeners.MyTestNGListener");
				testNG.addTest("Add Portfolio test");
				List<String> includedMethods = new ArrayList<String>();
				includedMethods.add("createPortfolio");
                testNG.addTestClass("com.rediff.ddf.newtestcases.PortfolioManagement", includedMethods);
				
				
				testNG.addTest("Delete Portfolio test");
				includedMethods = new ArrayList<String>();
				includedMethods.add("deletePortfolio");
                testNG.addTestClass("com.rediff.ddf.newtestcases.PortfolioManagement", includedMethods); */
				
				/*includedMethods = new ArrayList<String>();
				includedMethods.add("addNewStock");
				includedMethods.add("verifyStockPresent");
				includedMethods.add("verifyStockQuantity");
				includedMethods.add("verifyTransactionHistory");
				testNG.addTestClass("com.rediff.ddf.newtestcases.StockManagement", includedMethods);
				testNG.run(); */
		
		
		

	}

}