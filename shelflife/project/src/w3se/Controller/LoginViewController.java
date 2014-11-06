package w3se.Controller;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import w3se.Model.IMS;
import w3se.Model.Task;
import w3se.Model.Base.LogItem;
import w3se.Model.Base.LogItemFactory;
import w3se.Model.Base.User;
import w3se.View.Panels.LoginPanel;

/**
 * 
 * Class  : LoginViewController.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to define listeners for the login view
 */
public class LoginViewController extends AbstractController
{
	public static final String LOGIN = "login";
	public static final String LOGOUT = "logout";
	private IMS m_model = null;
	private LoginPanel m_view = null;
	
	/**
	 * constructor
	 * @param model - main model of the system (IMS)
	 */
	public LoginViewController(IMS model)
	{
		m_model = model;
		propagateMap();
	}
	
	protected void propagateMap()
	{
		// add the call back for the login button event
		addListener(LOGIN, new 
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
										m_model.setUser(m_view.getUser());
										try
										{
											m_model.addLog(LogItemFactory.createLogItem(LogItem.LOGIN, m_view.getUsername()+" logged onto the system."));
											m_model.login();
										} catch (Exception e)
										{
											if (m_model.showDialogs())
												JOptionPane.showMessageDialog(m_view, e.getMessage(), "Login Failure", JOptionPane.ERROR_MESSAGE);
										}
									}
								}));
					}
				});
		
		// create and add a listener for the logout button event
		addListener(LOGOUT, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
										m_model.addLog(LogItemFactory.createLogItem(LogItem.LOGIN, m_model.getCurrentUser().getUsername()+" logged out of the system."));
										m_model.setUser( new User(-1, User.GENERAL, "General", "General"));
										m_model.logout();
									}
								}));
					}
				});
	}

	@Override
	public void registerView(Object view)
	{
		m_view = (LoginPanel)view;
	}

	

}
