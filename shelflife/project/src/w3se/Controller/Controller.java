package w3se.Controller;

/**
 * 
 * Class  : Controller.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Nov 26, 2012
 * Desc   : Interface to define register view and getListener methods
 */
public interface Controller
{
	/**
	 * method to get a listener 
	 * @param key - key is found by using public fields
	 * @return - ListenerAdaptor 
	 */
	public ListenerAdaptor getListener(String key);
	
	/**
	 * method to register a view to the controller
	 * @param view
	 */
	public void registerView(Object view);
}
