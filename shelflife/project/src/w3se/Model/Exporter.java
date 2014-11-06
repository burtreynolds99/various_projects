package w3se.Model;

import java.util.ArrayList;

/**
 * 
 * Class  : Exporter.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Interface for exporter methods
 */
public interface Exporter
{
	/**
	 * method to set the filename to export to
	 * @param filename
	 */
	public void setFilename(String filename);
	
	/**
	 * method to export the contents of an arraylist
	 * @param list - list of exportable objects
	 * @throws Exception - if an error occurs
	 */
	public void export(ArrayList list) throws Exception;
}
