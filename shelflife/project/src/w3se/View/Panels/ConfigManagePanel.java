package w3se.View.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import w3se.Controller.Controller;
import w3se.Model.IMS;
import w3se.Model.Base.Receipt;
import w3se.Model.Base.User;
import w3se.View.MainView;
import w3se.View.Subpanels.AddUserPanel;
import w3se.View.Subpanels.ConfigSQLPanel;
import w3se.View.Subpanels.ConfigSubPanel;
import w3se.View.Subpanels.ExportPanel;
import w3se.View.Subpanels.GeneralSettingsPanel;
import w3se.View.Subpanels.RemoveUserPanel;

/**
 * 
 * Class  : ConfigManagePanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for managing configurations
 */
@SuppressWarnings("serial")
public class ConfigManagePanel extends JPanel implements Observer
{
	public static final int WIDTH = 1020;
	public static final int HEIGHT = 500;
	public static final int NULL = 0;
	public static final int GENERAL = 1;
	public static final int USER_ADD = 2;
	public static final int USER_EDIT = 3;
	public static final int USER_REMOVE = 4;
	public static final int SQL_CONFIG = 5;
	public static final int EXPORT = 6;
	
	private MainView m_parent = null;
	private Controller m_controller = null;
	private JSplitPane m_splitPane;
	private ConfigSubPanel m_mainPanel;
	private GeneralSettingsPanel m_generalSettings;
	private AddUserPanel m_addUser;
	private RemoveUserPanel m_removeUser;
	private ConfigSQLPanel m_sqlConfig;
	private ExportPanel m_exportPanel;
	private JPanel m_nullPanel;
	private int m_currentPanel = NULL;

	/**
	 * Create the panel.
	 */
	public ConfigManagePanel(MainView mainView, Controller controller)
	{
		m_parent = mainView;
		m_controller = controller;
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new BorderLayout());
		m_splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		m_splitPane.setEnabled(true);
		add(m_splitPane, BorderLayout.CENTER);
			
		m_mainPanel = new ConfigSubPanel(this, m_controller);
		m_nullPanel = new JPanel();
		m_nullPanel.setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		
		m_splitPane.setLeftComponent(m_mainPanel);
		m_splitPane.setRightComponent(m_nullPanel);
		m_splitPane.setEnabled(false);
		m_controller.registerView(this);
	
		IMS.getInstance().addView(this);
		
		m_generalSettings = new GeneralSettingsPanel(m_controller);
		m_addUser = new AddUserPanel(m_controller);
		m_removeUser = new RemoveUserPanel(m_controller);
		m_sqlConfig = new ConfigSQLPanel(m_controller);
		m_exportPanel = new ExportPanel(m_controller);
		m_nullPanel = new JPanel();
		m_nullPanel.setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		
	}

	/**
	 * method to set the info panel (right panel) dynamically
	 * @param panel
	 */
	public void setInfoPanel(int panel)
	{
		switch (panel)
		{
			case GENERAL:
				m_currentPanel = GENERAL;
				m_splitPane.setRightComponent(m_generalSettings);
				break;
			case USER_ADD:
				m_addUser.setAddMode();
				m_currentPanel = USER_ADD;
				m_splitPane.setRightComponent(m_addUser);
				break;
			case USER_EDIT:
				m_addUser.setEditMode();
				m_currentPanel = USER_EDIT;
				m_splitPane.setRightComponent(m_addUser);
				break;
			case USER_REMOVE:
				m_currentPanel = USER_REMOVE;
				m_splitPane.setRightComponent(m_removeUser);
				break;
			case SQL_CONFIG:
				m_currentPanel = SQL_CONFIG;
				m_splitPane.setRightComponent(m_sqlConfig);
				break;
			case EXPORT:
				m_currentPanel = EXPORT;
				m_splitPane.setRightComponent(m_exportPanel);
				break;
			default:
				m_currentPanel = NULL;
				m_splitPane.setRightComponent(m_nullPanel);
				break;
		}
		
	}
	
	/**
	 * method to get the search term for user editing or removal
	 * @return
	 */
	public String getSearchTerm()
	{
		if (m_currentPanel == USER_EDIT)
			return m_addUser.getSearchTerm();
		else if (m_currentPanel == USER_REMOVE)
			return m_removeUser.getSearchTerm();
		else
			return "";
	}
	
	/**
	 * method to get the selected search by field for editing and removing users
	 * @return
	 */
	public String getSearchBy()
	{
		if (m_currentPanel == USER_EDIT)
			return m_addUser.getSearchBy();
		else if (m_currentPanel == USER_REMOVE)
			return m_removeUser.getSearchBy();
		else
			return "";
	}
	
	/**
	 * method to set the current user for editing
	 * @param user
	 */
	public void setUser(User user)
	{
		m_addUser.setUser(user);
	}
	
	/**
	 * method to get the current user from editing
	 * @return
	 */
	public User getUser()
	{
		return m_addUser.getUser();
	}

	/**
	 * method to clear user editing and removing fields
	 */
	public void clear()
	{
		m_addUser.clear();
		m_removeUser.clearList();
	}
	
	/**
	 * method to get the sql configurations
	 * @return
	 */
	public String[] getSQLParams()
	{
		return m_sqlConfig.getSQLParams();
	}
	
	/**
	 * method to get the selected export type
	 * @return
	 */
	public int getExportType()
	{
		return m_exportPanel.getExporterSelection();
	}
	
	/**
	 * method to see if the set error dialog checkbox is set
	 * @return
	 */
	public boolean isDialogCheckSelected()
	{
		return m_generalSettings.getDialogCBSelection();
	}
	
	/**
	 * method to see if the set server mode notification checkbox is set
	 * @return
	 */
	public boolean isNotifyCheckSelected()
	{
		return m_generalSettings.getNotifyUserSelection();
	}
	
	/**
	 * method to get the receipt parameters after accepting changes
	 * @return
	 */
	public Receipt getReceipt()
	{
		return m_generalSettings.getReceiptTemplate();
	}
	
	@Override
	public void update(Observable o, Object arg)
	{
		clear();
		
		m_addUser.setUser(IMS.getInstance().getVolatileUser());
		m_removeUser.setRemoveList(IMS.getInstance().getRemoveUserList());
		m_sqlConfig.setSQLParams(IMS.getInstance().getDatabaseAdaptor().getDatabaseConfig());
		m_generalSettings.setDialogCheckBox(IMS.getInstance().showDialogs());
		m_generalSettings.setNotifyCheckBox(IMS.getInstance().showNotifyOfServerMode());
		
		m_mainPanel.updateColor();
		m_addUser.updateColor();
		m_removeUser.updateColor();
		m_sqlConfig.updateColor();
		m_generalSettings.updateColor();
		m_generalSettings.updateReceipt(IMS.getInstance().getReceipt());
		m_mainPanel.updateButtons();
	}
}
