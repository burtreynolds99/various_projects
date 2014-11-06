package w3se.Controller;

import java.util.HashMap;

/**
 * 
 * Class  : AbstractController.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Nov 26, 2012
 * Desc   : Abstract class to implement getListener and addListener methods
 */
public abstract class AbstractController implements Controller
{
	
	private HashMap<String, ListenerAdaptor> m_listenerMap = new HashMap<String, ListenerAdaptor>();
	
	/**
	 * method to obtain a listener
	 * @param key - String key to find a particular listener (e.g. "config_user_accept")
	 */
	public ListenerAdaptor getListener(String key)
	{
		return m_listenerMap.get(key);
	}
	
	/**
	 * method to add a listener 
	 * @param key - String identifier for the listener
	 * @param listener - listener object to be added
	 */
	protected void addListener(String key, ListenerAdaptor listener)
	{
		m_listenerMap.put(key, listener);
	}
	
	/**
	 * method to fill the hash map with listener-key pairs during object construction
	 */
	abstract protected void propagateMap();
	
}
