package util;

import org.json.simple.JSONObject;

public class ReadDataSample {

	public static void main(String[] args) {

		String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\xls_data\\Data.xlsx";
		String sheetName = "Stock Suite";
		String flagName = "deletestock";
		int iterationNumber = 1;
		JSONObject json = new ReadDataSample().excelData(filePath, sheetName, flagName, iterationNumber);
	}

	public JSONObject excelData(String filePath, String sheetName, String flagName, int iterationNumber) {
		ReadExcel readExcel = new ReadExcel(filePath);
		int flagNumber = 0;
		while (!readExcel.getCellData(sheetName, flagName, flagNumber).equals(flagName)) {
			flagNumber++;
		}
		int colStartRowNum = flagNumber + 1;
		int dataStartRowNum = flagNumber + 2;
		int index = 1;

		while (!readExcel.getCellData(sheetName, 1, dataStartRowNum).equals("")) {
			int colIndex = 1;

			JSONObject json = new JSONObject();

			if (index == iterationNumber) {
				while (!readExcel.getCellData(sheetName, colIndex, colStartRowNum).equals("")) {
					String colName = readExcel.getCellData(sheetName, colIndex, colStartRowNum);
					String data = readExcel.getCellData(sheetName, colIndex, dataStartRowNum);
					json.put(colName, data);
					colIndex++;
				}
				return json;
			} else
				index++;

			dataStartRowNum++;
		}

		return new JSONObject();
	}

	public int getTestDataSets(String filePath, String flagName, String sheetName) {

		ReadExcel readExcel = new ReadExcel(filePath);
		int flagNumber = 0;

		while (!readExcel.getCellData(sheetName, flagName, flagNumber).equals(flagName)) {
			flagNumber++;
		}

		int colStartRowNum = flagNumber + 1;
		int dataStartRowNum = flagNumber + 2;
		int totalRows = 0;

		while (!readExcel.getCellData(sheetName, 1, dataStartRowNum).equals("")) {
			totalRows++;
			dataStartRowNum++;
		}

		return totalRows;

	}

}