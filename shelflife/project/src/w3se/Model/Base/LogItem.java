package w3se.Model.Base;

import java.sql.Timestamp;
import w3se.Model.Exportable;

/**
 * 
 * Class  : LogItem.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to hold information of a log entry
 */
public class LogItem implements Exportable
{
	/**
	 * Log Type - Add, Edited, or Removed books
	 */
	public static final int INVENTORY = 0;
	/**
	 * Log Type - Add, Edited, or Removed users
	 */
	public static final int USER = 1;
	/**
	 * Log Type - System errors
	 */
	public static final int SYSTEM = 2;
	/**
	 * Log Type - Successful/failed login attempts, and logouts
	 */
	public static final int LOGIN = 3;
	/**
	 * Log Type - Sell of books with simple sales information
	 */
	public static final int SALES = 4;
	/**
	 * Log Type - Used exclusively in searching for logs
	 */
	public static final int ALL = -1;
	
	private long m_id = 0;
	private Timestamp m_timeStamp;
	private int m_action = 0;
	private String m_desc = "";
	
	/**
	 * default constructor
	 */
	public LogItem()
	{}
	
	/**
	 * Constructor for a Log Item
	 *
	 * @param id 		ID for a specific log action
	 * @param timeStamp Timestamp for a specific log action
	 * @param action 	Log action 
	 * @param desc		Description of a specific log action
	 * @see LogItem    
	 */
	public LogItem(long id, Timestamp timeStamp, int action, String desc)
	{
		m_id = id;
		m_timeStamp = timeStamp;
		m_action = action;
		m_desc = desc;
	}

	/**
	 * Gets the ID for a specific log action
	 *
	 * @return The ID for a specific log action 
	 * @see getID    
	 */
	public long getID()
	{
		return m_id;
	}

	/**
	 * Sets the ID for a specific log action
	 *
	 * @param id ID for a specific log action
	 * @see setID    
	 */
	public void setID(long id)
	{
		m_id = id;
	}

	/**
	 * Returns the timestamp for the action that is being logged
	 *	
	 * @return The timestamp for the action that is being logged
	 * @see     
	 */
	public Timestamp getTimeStamp()
	{
		return m_timeStamp;
	}
	
	/**
	 * Returns the timestamp string for the action that is being logged
	 *	
	 * @return The timestamp string for the action that is being logged
	 * @see     
	 */
	public String getTimeStampString()
	{
		return m_timeStamp.toString();
	}
	
	/**
	 * Sets the timestamp for the action that is being logged
	 *
	 * @param timeStamp The timestamp for the action that is being logged
	 * @see setTimeStamp    
	 */
	public void setTimeStamp(Timestamp timeStamp)
	{
		m_timeStamp = timeStamp;
	}

	/**
	 *  Returns the action that took place that is being logged
	 *
	 * @return The action that took place that is being logged.
	 * @see getAction    
	 */
	public int getAction()
	{
		return m_action;
	}

	/**
	 * Sets the action that took place that is being logged.
	 *
	 * @param action The action that took place that is being logged.
	 * @see setAction    
	 */
	public void setAction(int action)
	{
		m_action = action;
	}

	/**
	 * Returns the description for a specific log item
	 *
	 * @return The description for a specific log item 
	 * @see getDesc    
	 */
	public String getDesc()
	{
		return m_desc;
	}

	/**
	 * Sets the description for a specific log iteam
	 *
	 * @param desc The description for a log item
	 * @see setDesc    
	 */
	public void setDesc(String desc)
	{
		m_desc = desc;
	}
	
	/**
	 * Returns the Exportable title for the Log Database
	 *
	 * @return The exportable title for the Log database
	 * @see getExportableTitle    
	 */
	public String getExportableTitle()
	{
		return "Log Entry Database Export";
	}
	
	/**
	 * Returns the exportable head rows for the log database
	 *	
	 * @return The exportable head rows for the log database
	 * @see getExportableHeadRow    
	 */
	public String[] getExportableHeadRow()
	{
		String[] format = new String[4];
		format[0] = "L_ID";
		format[1] = "Timestamp";
		format[2] = "Action";
		format[3] = "Description";
		
		return format;
	}
	
	/**
	 * Returns the exportable rows for the log database 
	 *
	 * @return 
	 * @see getExportableRow
	 */
	public String[] getExportableRow()
	{
		String[] format = new String[4];
		format[0] = ""+m_id;
		format[1] = getTimeStampString();
		format[2] = ""+m_action;
		format[3] = m_desc;
		
		return format;
	}
}
