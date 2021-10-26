package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	
	public  static XSSFWorkbook workbook;
	
	
	public ReadExcel(String filePath){
	
	 try
     {
         FileInputStream file = new FileInputStream(new File(filePath));

         //Create Workbook instance holding reference to .xlsx file
          workbook = new XSSFWorkbook(file);

         //Get first/desired sheet from the workbook
         XSSFSheet sheet = workbook.getSheetAt(0);
     }
	 catch(Exception e){
		 //e.printStackTrace();
	 }
   }
	

	
	public static String getCellData(String sheetName,String colName,int rowNum){
		
           try{
			if(rowNum <0){
				//System.out.println("Row "+rowNum + " number is less than 0 ");
				return "";
             }

		    XSSFSheet sheet = workbook.getSheet(sheetName);
			if(sheet == null){
			//	System.out.println("No sheet found with the name "+sheetName);
				return "";
			}
			
			int col_Num = -1;

		Row row=sheet.getRow(rowNum);
		if(row==null)
		{
			System.out.println("Row is null for row number "+rowNum);
            return ""; 
		}
		
		
         for(int i=0;i<row.getLastCellNum();i++){
			Cell cell = row.getCell(i);
			if(cell.getCellType() == CellType.STRING){
				  String value =  cell.getStringCellValue().trim();
				  if(value.equals(colName.trim())){
						return value;}
			}
			  
			  else if(cell.getCellType() == CellType.NUMERIC||cell.getCellType()==CellType.FORMULA){
				  String value =   String.valueOf(cell.getNumericCellValue());
				  if(value.equals(colName.trim())){
					  return value;}
			}
			  
			  else if (cell.getCellType() == CellType.BLANK){
				  String value =  "";
				  if(value.equals(colName.trim())){
					  return value;}
			  }
			
	}
		
		if(col_Num==-1){
		//	System.out.println("Col number  "+col_Num + "was not updated above.So it is -1 ");
            return "";
		}
		//
		System.out.println("Finally here .No value found for first get Cell Data");
	      return "";
	     }
     	catch(Exception e){
      	// e.printStackTrace();
	    //
		//System.out.println("In catch block. So returning 0");
      return "";
     }
  }
	
	
	public static String getCellData(String sheetName,int colNum,int rowNum){
		 
		try{
			
			if(colNum==-1){
		   System.out.println("Col number "+colNum +"is -1");
           return "";
			}
              if(rowNum <=0){
       		   System.out.println("row number "+rowNum +"is less than equal to 0");
       		   return "";
              }
              
	        XSSFSheet sheet = workbook.getSheet(sheetName);
			if(sheet == null){
				System.out.println("No sheet found with the name "+sheetName);
				return "";
			}
			
        Row row=sheet.getRow(rowNum-1);
		if(row==null)
		{System.out.println("Row is null");
         return ""; }
	    row = sheet.getRow(rowNum);
	    
	     Cell cell=row.getCell(colNum-1);
		 if(cell==null)
			{System.out.println("Column is null");
             return ""; }
	   
		 
		  if(cell.getCellType() == CellType.STRING){
			  return cell.getStringCellValue();
		}
		  
		  else if(cell.getCellType() == CellType.NUMERIC||cell.getCellType()==CellType.FORMULA){
			  return String.valueOf(cell.getNumericCellValue());
		}
		  
		  else if (cell.getCellType() == CellType.BLANK){
				{System.out.println("Cell is blanck ");
				return "";}
		  }
		  System.out.println("At end of second getData method try block. returning empty String");
		   return "";
		}
		
	catch(Exception e){
	// e.printStackTrace();
	// System.out.println("In catch block return empty String");
	   return "";  
	 }
		
  }
	

public static int getRowCount(String sheetName){
	 int index = workbook.getSheetIndex(sheetName);
    	if(index==-1)
    	{System.out.println("Index is getRowCount method is -1.Hence returning 0");
		 return 0; }
	else{
	     XSSFSheet sheet = workbook.getSheetAt(index);
	     int number=sheet.getLastRowNum()+1;
	    return number;
	    }
   }
		
		
}