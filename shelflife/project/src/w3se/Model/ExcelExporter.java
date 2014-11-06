package w3se.Model;

import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * Class  : ExcelExporter.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to export an exportable object via Excel
 */
public class ExcelExporter implements Exporter
{
	private String m_filename;
	
	/**
	 * default constructor
	 */
	public ExcelExporter()
	{}
	
	/**
	 * constructor
	 * @param filename - filename to export to
	 */
	public ExcelExporter(String filename)
	{
		m_filename = filename;
	}
	
	@Override
	public void setFilename(String filename)
	{
		m_filename = filename;
	}

	@Override
	public void export(ArrayList list) throws Exception
	{
		if (list.size() > 0)
		{
			// create the excel title
			String title = ((Exportable)list.get(0)).getExportableTitle();
			// create the head row
			String[] head = ((Exportable)list.get(0)).getExportableHeadRow();
			// create the work book to use
			HSSFWorkbook workbook = new HSSFWorkbook();
			// create a new sheet with the title
			HSSFSheet sheet = workbook.createSheet(title);
			// create head row
			HSSFRow rowhead = sheet.createRow(0);
			
			// fill in the head row
			for (int i = 0; i < head.length; i++)
			{
				rowhead.createCell(i).setCellValue(head[i]);
			}
			
			// fill in the rest of the rows 
			for (int i = 0; i < list.size(); i++)
			{
				// get the next exportable object
				Exportable expo = (Exportable)list.get(i);
				// determine the row format
				String[] rowFormat = expo.getExportableRow();
				// create a new row
				HSSFRow row = sheet.createRow(i);
				
				for (int j = 0; j < rowFormat.length; j++)
				{
					row.createCell(j).setCellValue(rowFormat[j]);
				}
			}
			// create the output stream
			FileOutputStream output = new FileOutputStream(m_filename+".xls");
		
			workbook.write(output);
			output.close();
		}
		
		
	}
}
