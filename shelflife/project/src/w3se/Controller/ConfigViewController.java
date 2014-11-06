package w3se.Controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import w3se.Model.Configurations;
import w3se.Model.ExcelExporter;
import w3se.Model.IMS;
import w3se.Model.PlainTextExporter;
import w3se.Model.Task;
import w3se.Model.Base.LogItem;
import w3se.Model.Base.LogItemFactory;
import w3se.Model.Base.Receipt;
import w3se.Model.Base.Theme;
import w3se.Model.Base.User;
import w3se.Model.Database.DatabaseAdaptor;
import w3se.View.Panels.ConfigManagePanel;
import w3se.View.Subpanels.ExportPanel;

/**
 * 
 * Class  : ConfigViewController.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Nov 26, 2012
 * Desc   : Class to implement controllers for ConfigView
 */
public class ConfigViewController extends AbstractController
{
	public static final String ADD_USER = "config_add_user";
	public static final String USER_SEARCH = "config_user_search";
	public static final String REMOVE_USER_SEARCH = "config_remove_user_search";
	public static final String REMOVE_USER_DELETE = "config_remove_user_remove";
	public static final String REMOVE_USER_CLEAR = "config_remove_user_clear";
	public static final String REMOVE_USER_ACCEPT = "config_remove_user_accept";
	public static final String SQL_CONFIG_ACCEPT = "config_sql_accept";
	public static final String EXPORT_BOOKS = "books_export";
	public static final String EXPORT_USERS = "users_export";
	public static final String EXPORT_LOGS = "logs_export";
	public static final String ERROR_DIALOG_TOGGLE = "dialog_toggle";
	public static final String SERVER_MODE_NOTIFY_TOGGLE = "notify_smode_toggle";
	public static final String SELECT_MAIN_COLOR = "select_main_color";
	public static final String SELECT_SEC_COLOR = "select_secondary_color";
	public static final String ACCEPT_RECEIPT_CHANGES = "accept_receipt_changes";
	
	private IMS m_model = null;
	private ConfigManagePanel m_view = null;
	
	/**
	 * constructor
	 * @param model - main model of the software (IMS)
	 */
	public ConfigViewController(IMS model)
	{
		m_model = model;
		propagateMap();
	}
	
	// fill the map with listener-key pairs
	protected void propagateMap()
	{
		//listener for adding a user
		addListener(ADD_USER, new 
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
								Runnable()
								{
									public void run()
									{
										if (m_model.createUser(m_view.getUser()))
											IMS.getInstance().getConfigurations().setNewConfiguration("MultiUser", "true");
										m_model.setVolatileUser(new User());			// clear the system's working user
										m_model.addLog(LogItemFactory.createLogItem(LogItem.USER, m_view.getUser().getUsername()+" added to the database."));
									}
								}));
					}
				});
		
		// listener for searching for a user
		addListener(USER_SEARCH, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
								Runnable()
								{
									public void run()
									{
										User user = m_model.retreiveUser(new String[]{m_view.getSearchBy(),m_view.getSearchTerm()}).get(0);
										m_model.setVolatileUser(user);
									}
								}));
					}
				});
		
		// listener for searching for a user for removal
		addListener(REMOVE_USER_SEARCH, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
								Runnable()
								{
									public void run()
									{
										ArrayList<User> users = m_model.retreiveUser(new String[]{m_view.getSearchBy(),m_view.getSearchTerm()});
										for (int i = 0; i < users.size(); i++)
											m_model.addToRemoveUserList(users.get(i));
									}
								}));
					}
				});
		
		// listener for removing a user from the remove user list
		addListener(REMOVE_USER_DELETE, new
				ListenerAdaptor()
				{
					public void mouseClicked(MouseEvent e)
					{
						JTable table = (JTable)e.getSource();
						final int row = table.rowAtPoint(e.getPoint());
						
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
							Runnable()
							{
								public void run()
								{
									m_model.removeFromRemoveUserList(row);
								}
							}));
					}
				});
		
		// listener for clearing the remove user list
		addListener(REMOVE_USER_CLEAR, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
							Runnable()
							{
								public void run()
								{
									m_model.clearRemoveUserList();
								}
							}));
					}
				});
		
		// listener for removing a user from the system
		addListener(REMOVE_USER_ACCEPT, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
							Runnable()
							{
								public void run()
								{
									int result = JOptionPane.showConfirmDialog(null, "Remove users from the system?", "Remove Users From Database", JOptionPane.YES_NO_OPTION);
									if (result == JOptionPane.YES_OPTION)
									{
										for (int i = 0; i < m_model.getRemoveUserList().size(); i++)
										{
											m_model.addLog(LogItemFactory.createLogItem(LogItem.USER, m_model.getRemoveUserList().get(i).getUsername()+" removed from the database."));
											User user = m_model.getRemoveUserList().get(i);
											m_model.addLog(LogItemFactory.createLogItem(LogItem.USER, user.getUsername()+" removed from the system."));
											m_model.removeUserFromDB(user);
										}
										
									}
									m_model.clearRemoveUserList();
								}
							}));
					}
				});
		
		// listener for accepting sql config changes
		addListener(SQL_CONFIG_ACCEPT, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
							Runnable()
							{
								public void run()
								{
									String[] params = m_view.getSQLParams();
									Configurations config = IMS.getInstance().getInstance().getConfigurations();
									config.setNewConfiguration("DatabaseDriver", params[0]);
									config.setNewConfiguration("BooksDBUrl", params[1]);
									config.setNewConfiguration("UsersDBUrl", params[2]);
									config.setNewConfiguration("LogsDBUrl", params[3]);
									config.setNewConfiguration("ServerMode", params[4]);
								}
							}));
					}
				});
		
		// listener for exporting the books database
		addListener(EXPORT_BOOKS, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
							Runnable()
							{
								public void run()
								{
									JFileChooser dialog = new JFileChooser();
									FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL Files", "xls");
									dialog.setFileFilter(filter);
									int returnVal = dialog.showSaveDialog(null);
									
									if (m_view.getExportType() == ExportPanel.EXCEL_EXP)
										m_model.setExporter(new ExcelExporter());
									else
										m_model.setExporter(new PlainTextExporter());
								
									if (returnVal == JFileChooser.APPROVE_OPTION)
									{
										m_model.export(DatabaseAdaptor.BOOK_DB, dialog.getSelectedFile().getAbsolutePath()+".xls");
									}
								}
							}));
					}
				});
		
		// listener for exporting the users database
		addListener(EXPORT_USERS, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
							Runnable()
							{
								public void run()
								{
									JFileChooser dialog = new JFileChooser();
									FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL Files", "xls");
									dialog.setFileFilter(filter);
									int returnVal = dialog.showSaveDialog(null);
									
									if (m_view.getExportType() == ExportPanel.EXCEL_EXP)
										m_model.setExporter(new ExcelExporter());
									else
										m_model.setExporter(new PlainTextExporter());
								
									if (returnVal == JFileChooser.APPROVE_OPTION)
									{
										m_model.export(DatabaseAdaptor.USERS_DB, dialog.getSelectedFile().getAbsolutePath()+".xls");
									}
								}
							}));
					}
				});
		
		// listener for exporting the logs database
		addListener(EXPORT_LOGS, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
							Runnable()
							{
								public void run()
								{
									JFileChooser dialog = new JFileChooser();
									FileNameExtensionFilter filter = new FileNameExtensionFilter("Supported Files", "xls");
									dialog.setFileFilter(filter);
									int returnVal = dialog.showSaveDialog(null);
									
									if (m_view.getExportType() == ExportPanel.EXCEL_EXP)
										m_model.setExporter(new ExcelExporter());
									else
										m_model.setExporter(new PlainTextExporter());
								
									if (returnVal == JFileChooser.APPROVE_OPTION)
									{
										m_model.export(DatabaseAdaptor.LOGS_DB, dialog.getSelectedFile().getAbsolutePath());
									}
								}
							}));
					}
				});
		
		// listener for dialogs on or off
		addListener(ERROR_DIALOG_TOGGLE, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
							Runnable()
							{
								public void run()
								{
									m_model.toggleDialog(m_view.isDialogCheckSelected());
									m_model.getConfigurations().setNewConfiguration("ErrorDialog", ""+m_view.isDialogCheckSelected());
								}
								
							}));
					}
				});
		
		// listener for server mode notification on or off
		addListener(SERVER_MODE_NOTIFY_TOGGLE, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
							Runnable()
							{
								public void run()
								{
									m_model.toggleNotifySMode(m_view.isNotifyCheckSelected());
									m_model.getConfigurations().setNewConfiguration("NotifyOfSMode", ""+m_view.isNotifyCheckSelected());
								}
								
							}));
					}
				});
		
		// listener for selecting main color of windows
		addListener(SELECT_MAIN_COLOR, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
							Runnable()
							{
								public void run()
								{
									Theme theme = m_model.getTheme();
									Color bgColor = JColorChooser.showDialog(null, "Choose Main Color", null);
									m_model.setTheme(new Theme(bgColor, theme.getSecondaryColor()));
									m_model.getConfigurations().setNewConfiguration("MainColor", ""+bgColor.getRGB());
								}
								
							}));
					}
				});
		
		// listener for selecting the secondary color of the windows
		addListener(SELECT_SEC_COLOR, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
							Runnable()
							{
								public void run()
								{
									Theme theme = m_model.getTheme();
									Color color = JColorChooser.showDialog(null, "Choose Secondary Color", null);
									m_model.setTheme(new Theme(theme.getMainColor(), color));
									m_model.getConfigurations().setNewConfiguration("SecColor", ""+color.getRGB());
								}
								
							}));
					}
				});
		
		// listener for accepting the receipt changes
		addListener(ACCEPT_RECEIPT_CHANGES, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.MANAGER, new 
							Runnable()
							{
								public void run()
								{
									Receipt r = m_view.getReceipt();
									m_model.setReceipt(r);
									m_model.getConfigurations().setNewConfiguration("BusinessName", r.getStoreName());
									m_model.getConfigurations().setNewConfiguration("BusinessPhoneNumber", r.getStorePhoneNumber());
									m_model.getConfigurations().setNewConfiguration("BusinessMessage", r.getMessageToRecipient());
									m_model.getConfigurations().setNewConfiguration("BusinessSlogan", r.getSlogan());
								}
								
							}));
					}
				});
	}

	@Override
	public void registerView(Object view)
	{
		m_view = (ConfigManagePanel)view;
	}

	

}
