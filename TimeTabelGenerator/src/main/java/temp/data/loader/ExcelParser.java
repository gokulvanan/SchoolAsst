package temp.data.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelParser {
	private String excelFileLocation=null;
	private Map<Integer,List<String>> tableVector =null;
	private File inputFile=null;
	
	public ExcelParser(String fileLocation)
	{
		this.excelFileLocation=fileLocation;
	}
	/**
	 * Validate input file if it is valid and if its an excel 
	 */
	
	boolean ValidateFile()
	{
		try
		{
			File input = new File(excelFileLocation);
			if(input.exists() && input.isFile())
			{
				if(!input.canRead())
				{
					input.setReadable(true);
				}
				String fileName= input.getName();
				int size= fileName.length();
				String extension = fileName.substring(size-3);
				if(extension.equalsIgnoreCase("xls"))
				{
					inputFile = input;
					System.out.println("Valid File Type");
				}
				else
				{
					System.out.println("Invalid File Type");
					System.out.println("Given file type =>"+extension);
					System.out.println("Expected File Type => xls");
					return false;
				}
					
			}else
				
			{
				System.out.println("File Not Found/Read exception");
				return false;
			}
		}catch(Exception e)
		{
			e.printStackTrace();;
			return false;
		}
		return true;
	}
	
	/**
	 * method to Read excel sheet using jxl jar file and returns a Vector of ArrayList with table data
	 * Input parameters are sheet No of the excel sheet and start row index for the table heading in the excel sheet
	 * @return Vector<ArrayList>
	 * @param int sheet No
	 * @param int rowStart
	 */
	public Map<Integer,List<String>> parser(int sheetNo, int rowStart) throws IOException{
		Workbook w;	
		try {
			w = Workbook.getWorkbook(inputFile);
			// Get the first sheet
			Sheet sheet = w.getSheet(sheetNo);
			

			tableVector= new LinkedHashMap<Integer,List<String>>();
			ArrayList<String> rowData;
			ArrayList<Integer> typeData= new ArrayList<Integer>();
		for(int row=rowStart,i=0; row<sheet.getRows(); row++,i++)
		{
			
			rowData = new ArrayList<String>();
			for (int col=0; col<sheet.getColumns(); col++ )
			{
				Cell cell = sheet.getCell(col,row);
				int size =cell.getContents().length();
				if(row==rowStart+1){
					typeData.add(col,size);
				}
				if (row>rowStart+1){
					if(size> typeData.get(col))
					{
						typeData.add(col,size);
						typeData.remove(col+1);
					}
				}
					
				rowData.add(col, cell.getContents());
			}
			tableVector.put(i,rowData);
		}
		} catch (BiffException e) {
			e.printStackTrace();
		}
		System.out.println(tableVector.toString());
	
		return tableVector;
	}
	
	
	public static void main (String args[]){
		ExcelParser exec = new ExcelParser("D:/Own App Work/SchoolApp/DBScripts.xls");
		if(exec.ValidateFile())
		{
			try{
//				DataBaseTableGenerator dbscriptTest = new DataBaseTableGenerator();
//				dbscriptTest.scriptCreatorExecutor(exec.parser(0,0),"NEW_CMPLX_TABLE",0);
				exec.parser(1,1);
			}catch(IOException e)
			{
				System.out.println("Error in WorkBook creatation during Excel Read");
				e.printStackTrace();
			}
		}
	}

}
