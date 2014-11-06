package w3se.Model;

/**
 * 
 * Class  : Exportable.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Interface for exportable methods
 */
public interface Exportable
{
	/**
	 * method to get the exportable row of the object
	 * @return - String[] 
	 */
	public String[] getExportableRow();
	
	/**
	 * method to get the exportable title of the object
	 * @return - String title
	 */
	public String getExportableTitle();
	
	/**
	 * method to get the exportable head row of the object
	 * @return - String[] head row
	 */
	public String[] getExportableHeadRow();
}
