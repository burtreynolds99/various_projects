package w3se.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import w3se.Controller.Controller;
import w3se.Controller.ControllerFactory;
import w3se.Controller.MainViewController;
import w3se.Model.IMS;
import w3se.Model.Base.States;
import w3se.View.Panels.BookSearchPanel;
import w3se.View.Panels.ConfigManagePanel;
import w3se.View.Panels.LogManagePanel;
import w3se.View.Panels.LoginPanel;
import w3se.View.Panels.LogoutPanel;
import w3se.View.Panels.SellManagePanel;
import w3se.View.Subpanels.SearchPanel;

/**
 * 
 * Class  : MainView.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to serve as the main window of the program
 */
@SuppressWarnings("serial")
public class MainView extends JFrame implements Observer
{

	private JPanel contentPane;
	private Controller m_controller;
	private ControllerFactory m_conFac = ControllerFactory.getInstance();
	
	private static States m_state = States.LOGGED_OUT;
	
	public static final int SEARCH_TAB = 0;
	public static final int LOGIN_TAB = 1;
	public static final int MANAGE_TAB = 2;
	
	private JTabbedPane m_topLevelPane;
	private JTabbedPane m_subLevelSearchPane;
	private JTabbedPane m_subLevelManagePane;
	
	/**
	 * Create the frame.
	 */
	public MainView()
	{
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1080, 630);
		setTitle("ShelfLife Version 1.0");
		setResizable(false);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(d.width/2-getSize().width/2, d.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		m_controller = m_conFac.createController(ControllerFactory.MAIN_VIEW);
		m_controller.registerView(this);
		addWindowListener(m_controller.getListener(MainViewController.WINDOW_CLOSE));
		
		m_topLevelPane = new JTabbedPane(JTabbedPane.TOP);
		
		m_subLevelSearchPane = new JTabbedPane(JTabbedPane.TOP);
		m_subLevelSearchPane.addTab("Keyword", new BookSearchPanel(SearchPanel.KEYWORD_HEADER, false, m_conFac.createController(ControllerFactory.SEARCH)));
		m_subLevelSearchPane.addTab("Browse", new BookSearchPanel(SearchPanel.BROWSE_HEADER, false, m_conFac.createController(ControllerFactory.SEARCH)));
		
		m_subLevelManagePane = new JTabbedPane(JTabbedPane.TOP);
		m_subLevelManagePane.addTab("Sell", new SellManagePanel(m_conFac.createController(ControllerFactory.SELL)));
		m_subLevelManagePane.addTab("Add/Edit", new BookSearchPanel(SearchPanel.KEYWORD_HEADER, true, m_conFac.createController(ControllerFactory.SEARCH)));
		m_subLevelManagePane.addTab("Config.", new ConfigManagePanel(this, m_conFac.createController(ControllerFactory.CONFIG)));
		m_subLevelManagePane.addTab("Log", new LogManagePanel(m_conFac.createController(ControllerFactory.LOGS)));
		
		// tab 0
		m_topLevelPane.addTab("Search", m_subLevelSearchPane);
		// tab 1
		m_topLevelPane.addTab("Login", new LoginPanel(this, m_conFac.createController(ControllerFactory.LOGIN)));
		// tab 2
		m_topLevelPane.addTab("Manage", m_subLevelManagePane);
		
		contentPane.add(m_topLevelPane, BorderLayout.CENTER);
		
		updateLoginTab();
		IMS.getInstance().addView(this);
		
	}
	
	/**
	 * change the state of the view
	 * @param state
	 */
	public void changeState(States state)
	{
		m_state = state;
	}
	
	/**
	 * update the login tab to logout tab
	 */
	public void updateLoginTab()
	{
		if (m_state == States.LOGGED_OUT)
		{ 
			m_topLevelPane.setTitleAt(LOGIN_TAB, "Login");
			m_topLevelPane.setComponentAt(LOGIN_TAB, new LoginPanel(this, m_conFac.createController(ControllerFactory.LOGIN)));
			
			m_topLevelPane.setBackgroundAt(MANAGE_TAB, Color.DARK_GRAY);
			m_topLevelPane.setEnabledAt(MANAGE_TAB, false);
		}
		if (m_state == States.LOGGED_IN)
		{
			m_topLevelPane.setTitleAt(LOGIN_TAB, "Logout");
			m_topLevelPane.setComponentAt(LOGIN_TAB, new LogoutPanel(this, m_conFac.createController(ControllerFactory.LOGIN)));
			m_topLevelPane.setBackgroundAt(MANAGE_TAB, Color.LIGHT_GRAY);
			m_topLevelPane.setEnabledAt(MANAGE_TAB, true);
		}
	}
	

	/**
	 * 
	 */
	public void update(Observable arg0, Object arg1)
	{
		updateLoginTab();
	}

}
