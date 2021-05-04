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
		 e.printStackTrace();
	 }
     }
	

	
	public static String getCellData(String sheetName,String colName,int rowNum){
		
           try{
			if(rowNum <0)
				return "";

		    XSSFSheet sheet = workbook.getSheet(sheetName);
			if(sheet == null){
				System.out.println("No sheet found with the name "+sheetName);
				return " ";
			}
			
			int col_Num = -1;

		Row row=sheet.getRow(rowNum);
		if(row==null)
			return ""; 
		

		
		
         for(int i=0;i<row.getLastCellNum();i++){
			Cell cell = row.getCell(i);
			if(cell.getCellType() == CellType.STRING){
				  String value =  cell.getStringCellValue().trim();

				  if(value.equals(colName.trim())){
						col_Num=i;
						break;}
			}
			  
			  else if(cell.getCellType() == CellType.NUMERIC||cell.getCellType()==CellType.FORMULA){
				  String value =   String.valueOf(cell.getNumericCellValue());
				  if(value.equals(colName.trim())){
						col_Num=i;
						break;}
			}
			  
			  else if (cell.getCellType() == CellType.BLANK){
				  String value =  "";
				  if(value.equals(colName.trim())){
						col_Num=i;
						break;}
			  }
			
	}
		
		if(col_Num==-1)
			return "";
		
		row = sheet.getRow(rowNum);
		if(row==null){
			 System.out.println("Row is null");
				return "";
		}
		 Cell cell=row.getCell(col_Num);
		 if(cell==null)
		 {       
			 System.out.println("Cell is null");
				return "";
		 }		 
		 
		  if(cell.getCellType() == CellType.STRING){
			  return cell.getStringCellValue();
		}
		  
		  else if(cell.getCellType() == CellType.NUMERIC||cell.getCellType()==CellType.FORMULA){
			  System.out.println("Finally here .Value found is Numeric");
              return String.valueOf(cell.getNumericCellValue());
		}
		  
		  else if (cell.getCellType() == CellType.BLANK){
			  System.out.println("Finally here .Value found is blank");

			  return "";
		  }
		 
	}
	catch(Exception e){
	 e.printStackTrace();
	}
		  System.out.println("Finally here .No value found");

	   return " ";   
	}
	
	
	public static String getCellData(String sheetName,int colNum,int rowNum){
		 
		
		try{
			
			if(colNum==-1)
				return "";
			
              if(rowNum <=0)
				return "";
              
              
	        XSSFSheet sheet = workbook.getSheet(sheetName);
			if(sheet == null){
				System.out.println("No sheet found with the name "+sheetName);
				return "--3";
			}
			
        Row row=sheet.getRow(rowNum-1);
		if(row==null)
			return ""; 
	    row = sheet.getRow(rowNum);
	    
	    try{
		 Cell cell=row.getCell(colNum-1);
		 if(cell==null)
		  return "";
	   
		 
		  if(cell.getCellType() == CellType.STRING){
			  return cell.getStringCellValue();
		}
		  
		  else if(cell.getCellType() == CellType.NUMERIC||cell.getCellType()==CellType.FORMULA){
			  return String.valueOf(cell.getNumericCellValue());
		}
		  
		  else if (cell.getCellType() == CellType.BLANK){
			  return "Empty cell";
		  }
		 
	}
		 
	    catch(Exception e){
	    	return "";
	  }
		}
	catch(Exception e){
	 e.printStackTrace();
	}
	   return "";  
		
  }
	

public static int getRowCount(String sheetName){
	 int index = workbook.getSheetIndex(sheetName);
    	if(index==-1)
		return 0;
	else{
	     XSSFSheet sheet = workbook.getSheetAt(index);
	     int number=sheet.getLastRowNum()+1;
	    return number;
	    }
   }
		
		
}