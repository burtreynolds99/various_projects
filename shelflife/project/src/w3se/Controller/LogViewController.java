package w3se.Controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import w3se.Model.IMS;
import w3se.Model.Task;
import w3se.Model.Base.LogItem;
import w3se.Model.Base.User;
import w3se.Model.Database.LogsDB;
import w3se.View.Panels.LogManagePanel;

/**
 * Class  : LogViewController.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to define listeners for the log manage view
 */
public class LogViewController extends AbstractController
{
	public static final String SEARCH_ALL_LOGS = "search_all";
	public static final String SEARCH_BY_TERM = "search_by";
	public static final String REMOVE_LOGS = "clear";
	
	private IMS m_model = null;
	private LogManagePanel m_view = null;
	
	/**
	 * constructor
	 * @param model - main model of the system (IMS)
	 */
	public LogViewController(IMS model)
	{
		m_model = model;
		propagateMap();
	}
	
	// fill the map with listener-key pairs
	protected void propagateMap()
	{
		
		// listener for search for all log entries
		addListener(SEARCH_ALL_LOGS, new 
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
								Runnable()
								{
									public void run()
									{
										String orderBy;
										
										if (m_view.getOrderByIndex() == 0)
											orderBy = LogsDB.TIME;
										else if (m_view.getOrderByIndex() == 1)
											orderBy = LogsDB.ACTION;
										else
											orderBy = LogsDB.ID;
										m_view.clearLists();
										m_view.setSearchResults(m_model.getLogs(new String[] {LogsDB.ALL, orderBy, ""}));
									}
								}));
					}
				});
		
		// searching by keyword for log entries
		addListener(SEARCH_BY_TERM, new 
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
								Runnable()
								{
									public void run()
									{
										String orderBy;
										String searchBy;
										String searchTerm;
										
										if (m_view.getOrderByIndex() == 0)
											orderBy = LogsDB.TIME;
										else if (m_view.getOrderByIndex() == 1)
											orderBy = LogsDB.ACTION;
										else
											orderBy = LogsDB.ID;
										
										if (m_view.getSearchByIndex() == 0)
											searchBy = LogsDB.TIME;
										else if (m_view.getSearchByIndex() == 1)
											searchBy = LogsDB.ACTION;
										else
											searchBy = LogsDB.ID;
										
										searchTerm = m_view.getSearchTerm();
										m_view.clearLists();
										m_view.setSearchResults(m_model.getLogs(new String[] {searchBy, orderBy, searchTerm}));
									}
								}));
					}
				});
		
		// listener to remove all logs from the system
		addListener(REMOVE_LOGS, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
								Runnable()
								{
									public void run()
									{
										int result = JOptionPane.showConfirmDialog(null, "Remove all log entries?", "Remove Log Entries From Database", JOptionPane.YES_NO_OPTION);
										
										if (result == JOptionPane.YES_OPTION)
										{
											m_model.removeLogs();
											m_view.setSearchResults(new ArrayList<LogItem>());
											m_view.clearLists();
										}
									}
								}));
					}
				});
	}

	@Override
	public void registerView(Object view)
	{
		m_view = (LogManagePanel)view;
	}

}
