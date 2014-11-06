package w3se.Controller;

import w3se.Model.IMS;

/**
 * 
 * Class  : ControllerFactory.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Nov 26, 2012
 * Desc   : Singleton class to produce the controllers for the various views
 */
public class ControllerFactory
{
	/**
	 * Main window of the program
	 */
	public static final int MAIN_VIEW = 0;
	
	/**
	 * Keyword search, Browse Search, or Add/Edit panel 
	 */
	public static final int SEARCH = 1;
	
	/**
	 * Logon panel
	 */
	public static final int LOGIN = 2;
	
	/**
	 * Configurations panel
	 */
	public static final int CONFIG = 3;
	
	/**
	 * Sell Book panel
	 */
	public static final int SELL = 4;
	
	/**
	 * Log manage panel
	 */
	public static final int LOGS = 5;
	
	private static ControllerFactory m_conFactory = null;
	
	private static IMS m_model = null;
	
	/**
	 * private constructor
	 */
	private ControllerFactory()
	{
		m_model = IMS.getInstance();
	}
	
	/**
	 * private constructor
	 * @param model - main model of the system
	 */
	private ControllerFactory(IMS model)
	{
		m_model = model;
	}
	
	/**
	 * Method to obtain the instance of the ControllerFactory
	 * @return - ControllerFactory instance
	 */
	public static ControllerFactory getInstance()
	{
		if (m_conFactory == null)
			m_conFactory = new ControllerFactory();
		
		return m_conFactory;
	}
	
	/**
	 * method to get the instance of the controller factory
	 * @param model - main model of the system (IMS)
	 * @return - ControllerFactory instance
	 */
	public static ControllerFactory getInstance(IMS model)
	{
		if (m_conFactory == null)
			m_conFactory = new ControllerFactory(model);
		
		return m_conFactory;
	}
	
	/**
	 * method to register the model to the ControllerFactory
	 * @param model - main model of the system (IMS)
	 */
	public static void registerModel(IMS model)
	{
		m_model = model;
	}
	
	/**
	 * method to produce Controller's 
	 * @param controllerType - enum of the controller type based on the view (MAIN_VIEW, SEARCH, SELL, LOGIN, CONFIG, LOGS)
	 * @return - Controller
	 */
	public Controller createController(int controllerType)
	{
		Controller controller = null;
	
		switch (controllerType)
		{
			case MAIN_VIEW:
				controller = new MainViewController(m_model);
				return controller;
				
			case SEARCH:
				controller = new BookSearchController(m_model);
				return controller;
			
			case SELL:
				controller = new SellViewController(m_model);
				return controller;
				
			case LOGIN:
				controller = new LoginViewController(m_model);
				return controller;
				
			case CONFIG:
				controller = new ConfigViewController(m_model);
				return controller;
				
			case LOGS:
				controller = new LogViewController(m_model);
				return controller;
			
			default:
				return controller;
		}
		
	}
}
