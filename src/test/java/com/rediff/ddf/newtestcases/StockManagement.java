package com.rediff.ddf.newtestcases;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.rediff.ddf.base.BaseTest;

public class StockManagement extends BaseTest {
	
	@Test
	public void addNewStock(ITestContext context) {
		JSONObject data = (JSONObject)context.getAttribute("data");
	    String companyName = (String)data.get("stockname");
		String selectionDate= (String)data.get("date");// dd-MM-yyyy
		String stockQuantity=(String)data.get("quantity");
		String stockPrice=(String)data.get("price");
		
	   	System.out.println("-------------------------------AddNewStock-----------------------------------------------------------------");

		
		
		/*String companyName = "Birla Corporation Ltd.";
		String selectionDate="14-12-2020";
		String stockQuantity="100";
		String stockPrice="200";*/
		String companyNameStartWith = companyName.substring(0,10); 
		

	   app.log("Adding Stock Test :- Adding "+stockQuantity+" stocks of  "+ companyName);
		// find quantity
	   int quantityBeforeModification = app.findCurrentStockQuantity(companyName);
	   context.setAttribute("quantityBeforeModification", quantityBeforeModification);
       // quantity before adding/selling stocks
	   int qBM = (Integer)context.getAttribute("quantityBeforeModification");

     	app.click("addStock_id");
		app.type("addstockname_css", companyNameStartWith);
		app.wait(5);
		app.clickEnterButton(companyName);
		
		app.click("stockPurchaseDate_id");
		app.selectDateFromCalendar(selectionDate);
		app.wait(5);

		
		app.type("addstockqty_id", stockQuantity);
		app.type("addstockprice_id", stockPrice);
		app.click("addStockButton_id");
	   	app.waitForPageToLoad();
	   	
		app.log("Stocks added successfully ");
		

	}

	//sell or buy existing stock
	
	@Parameters ({"action"})
	@Test
	public void modifyStock(String action,ITestContext context) {
		String companyName = "Birla Corporation Ltd";
		String selectionDate="14-12-2020";
		String stockQuantity="100";
		String stockPrice="200";
		
	   	System.out.println("--------------------------------------------------ModifyStock-----------------------------------------------------------------");


		app.log("Modify Stock :- Selling "+stockQuantity +" of company "+ companyName);
		int quantityBeforeModification = app.findCurrentStockQuantity(companyName);
		context.setAttribute("quantityBeforeModification", quantityBeforeModification);
		
		// quantity before adding/selling stocks
		int qBM = (Integer)context.getAttribute("quantityBeforeModification");


		
		app.goToBuySell(companyName);
		if(action.equals("sellstock"))
		   app.selectByVisibleText("equityaction_id", "Sell");
		else
			app.selectByVisibleText("equityaction_id", "Buy");

		app.click("buySellCalendar_id");
		app.log("Selecting Date "+ selectionDate);

		app.selectDateFromCalendar(selectionDate);
		app.type("buysellqty_id", stockQuantity);
		app.type("buysellprice_id", stockPrice);

		app.click("buySellStockButton_id");
		app.waitForPageToLoad();
		app.log("Stock Sold ");

		
	}
	
	// checks if stock is present in the table
	@Test
	public void verifyStockPresent() {
	   	System.out.println("--------------------------------------------------VerifyStockPresent-----------------------------------------------------------------");

        String companyName = "Birla Corporation Ltd";
		int row = app.getRowNumWithCellData("stocktable_css", companyName);
		if(row ==-1)
			app.reportFailure("Stock Not present "+companyName, true);
		
		app.log("Stock Found in list "+companyName );
		
	}
	
	// checks the stock quantity
   @Parameters ({"action"})
	@Test
	public void verifyStockQuantity(String action, ITestContext context) {
		String companyName = "Birla Corporation Ltd";
		String selectionDate="14-12-2020";
		String stockQuantity="100";
		String stockPrice="200";
		
	   	System.out.println("-----------------------------------------------verifyStockQuantity-----------------------------------------------------------------");

		
		
		app.log("Verifying stock quantity after action - "+ action);
		// quantity after adding/selling stocks
		int quantityAfterModification = app.findCurrentStockQuantity(companyName);
		int modifiedquantity=Integer.parseInt(stockQuantity);
		int expectedModifiedQuantity=0;

		
		// quantity before adding/selling stocks
		int quantityBeforeModification = (Integer)context.getAttribute("quantityBeforeModification");

		if(action.equals("addstock"))
			expectedModifiedQuantity = quantityAfterModification-quantityBeforeModification;
		else if(action.equals("sellstock"))
			expectedModifiedQuantity = quantityBeforeModification-quantityAfterModification;
		
		app.log("Old Stock Quantity--------------------------------------------> "+quantityBeforeModification);
		app.log("New Stock Quantity-------------------------------------------> "+quantityAfterModification);
		
		if(modifiedquantity != expectedModifiedQuantity)
		    app.reportFailure("Quantity did not match", true);
		
		app.log("Stock Quantity Changed as per expected "+ modifiedquantity);
	}
	
	@Test
	public void verifyStockAvgBuyPrice() {
		
	   	System.out.println("--------------------------------veridyStockAvgBuyPrice-----------------------------------------------------------------");

		
	}
	
	// verifies the transaction history
	@Parameters ({"action"})
	@Test
	public void verifyTransactionHistory(String action) {
		
	   	System.out.println("--------------------------------verifyTrasactionHistory-----------------------------------------------------------------");

		
	


		String companyName = "Birla Corporation Ltd";
		String selectionDate="14-12-2020";
		String stockQuantity="100";
		String stockPrice="200";
		
		app.log("Verifying transaction History for "+action+"for quantity "+stockQuantity);
		app.goToTransactionHistory(companyName);
		String changedQuantityDisplayed  = app.getText("latestShareChangeQuantity_xpath");
		app.log("Got Changed Quantity "+ changedQuantityDisplayed);
		
		if(action.equals("sellstock"))
			stockQuantity="-"+stockQuantity;
		
		
		if(!changedQuantityDisplayed.equals(stockQuantity))
		   app.reportFailure("Got changed quantity in transaction history as "+ changedQuantityDisplayed, true);	
		
		app.log("Transaction History OK");
	}




}
