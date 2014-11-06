package w3se.Controller;

import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
import w3se.Model.IMS;
import w3se.Model.Task;
import w3se.Model.Base.LogItem;
import w3se.Model.Base.LogItemFactory;
import w3se.Model.Base.User;
import w3se.View.MainView;

/**
 * 
 * Class  : MainViewController.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to define the listeners for the main view
 */
public class MainViewController extends AbstractController
{
	public static final String WINDOW_CLOSE = "exit_system";
	public static final String TAB_CHANGED = "tab_changed";
	
	private IMS m_model = null;
	private MainView m_view = null;
	
	/**
	 * constructor
	 * @param model - main model of the system (IMS)
	 */
	public MainViewController(IMS model)
	{
		m_model = model;
		propagateMap();
	}
	
	// fill the map with listener-key pairs
	protected void propagateMap()
	{
		// listener to watch for the system to close
		addListener("exit_system", new 
				ListenerAdaptor()
				{
					// cannot run in task manager as it is a shutdown method and will jam the manager
					public void windowClosing(WindowEvent e)
					{
						m_model.addLog(LogItemFactory.createLogItem(LogItem.SYSTEM, "System shutdown."));
						try
						{
							m_model.shutdownSystem();
						} 
						catch (Exception ex){}
					}
					
					public void windowOpened(WindowEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
										m_model.addLog(LogItemFactory.createLogItem(LogItem.SYSTEM, "System startup."));
									}
								}));
					}
				});
		
		addListener("tab_changed", new
				ListenerAdaptor()
				{
					public void stateChanged(ChangeEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
									}
								}));
					}
				});
	}
	
	@Override
	public void registerView(Object view)
	{
		m_view = (MainView)view;
	}

}
