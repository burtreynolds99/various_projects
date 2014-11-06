package w3se.Model.Base;

import java.util.UUID;

/**
 * 
 * Class  : LogItemFactory.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to produce log entries with unique IDs and current timestamp
 */
public class LogItemFactory
{
	/**
	 * Creates a log item to be stored and view by a manager at a later date.
	 *
	 * @param action
	 * @param desc
	 * @return A created log item
	 * @see createLogItem
	 */
	public static LogItem createLogItem(int action, String desc)
	{
		LogItem item = new LogItem();
		long uuid = UUID.randomUUID().getLeastSignificantBits();
		
		// I hate negative id numbers and Java doesn't have unsigned long so I am just masking the leading bit
		long posMask = ~(1 << 63);
		uuid &= posMask;
		item.setID(uuid);
		item.setAction(action);
		item.setDesc(desc);
		
		return item;
	}
}
