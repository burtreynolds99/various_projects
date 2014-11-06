package w3se.Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * 
 * Class  : PlainTextExporter.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Concrete implementation of Exporter utilizing plain text for format
 */
public class PlainTextExporter implements Exporter
{
		private String m_filename;
		
		/**
		 * default constructor
		 */
		public PlainTextExporter()
		{}
		
		/**
		 * constructor
		 * @param filename - filename to export to
		 */
		public PlainTextExporter(String filename)
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
				ArrayList<Exportable> expList = (ArrayList<Exportable>)list;
				
				StringBuilder strB = new StringBuilder();
				String[] headRow = expList.get(0).getExportableHeadRow();
				String[] row;
				
				strB.append("\t\t\tTitle:  "+expList.get(0).getExportableTitle()+"\n");
				
				
				for (int i = 0; i < expList.size(); i++)
				{	
					row = expList.get(i).getExportableRow();
					strB.append("\n");
					for (int j = 0; j < row.length; j++)
					{
						strB.append(headRow[j] + "  =  " + row[j]+"\n");
					}
				}
				
				File file = new File(m_filename);
				
				String output = strB.toString();
				FileWriter writer = new FileWriter(file.getAbsoluteFile()+".txt");
				BufferedWriter buffWriter = new BufferedWriter(writer);
				buffWriter.write(output);
				buffWriter.close();
			}
			
			
		}
}
