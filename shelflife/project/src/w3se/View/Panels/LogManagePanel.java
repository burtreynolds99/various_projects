package w3se.View.Panels;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import w3se.Controller.Controller;
import w3se.Controller.LogViewController;
import w3se.Model.IMS;
import w3se.Model.Base.LogItem;
import w3se.View.ShelfLifeIcon;
import javax.swing.JComboBox;

/**
 * 
 * Class  : LogManagePanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for managing log entries
 */
@SuppressWarnings("serial")
public class LogManagePanel extends JPanel implements Observer
{

	private JComboBox m_cbSearchBy;
	private JComboBox m_cbOrderBy;
	private JLabel m_lblLogo;
	private JTable m_logList;
	private Controller m_controller;
	private JTextField m_searchField;
	
	/**
	 * Create the panel.
	 */
	public LogManagePanel(Controller controller)
	{
		m_controller = controller;
		m_controller.registerView(this);
				
		setBackground(IMS.getInstance().getTheme().getMainColor());
		setBounds(0, 0, 940, 500);
		JLabel lblSearchBy = new JLabel("Search By : ");
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(m_controller.getListener(LogViewController.SEARCH_BY_TERM));
		
		JButton btnShowAllLogs = new JButton("Show All Logs");
		btnShowAllLogs.addActionListener(m_controller.getListener(LogViewController.SEARCH_ALL_LOGS));
				
		// create an anonymous class to set the cells as not editable
			DefaultTableModel tableModel = new DefaultTableModel(new String[] {"ID","Time", "Action", "Description"}, 0)
				{
					public boolean isCellEditable(int row, int height)
					{
						return false;
					}
				};
					
				m_logList = new JTable(tableModel);
				m_logList.setBorder(new EtchedBorder());
				
				JScrollPane resultScroll = new JScrollPane(m_logList);
		
		JButton btnClearAll = new JButton("Remove All Logs");
		btnClearAll.addActionListener(m_controller.getListener(LogViewController.REMOVE_LOGS));
		
		m_lblLogo = new JLabel();
		m_lblLogo.setIcon(new ShelfLifeIcon());
		
		m_cbSearchBy = new JComboBox(new String[] {"Time", "Action", "ID"});
		
		JLabel lblOrderBy = new JLabel("Order By :");
		
		m_cbOrderBy = new JComboBox(new String[] {"Time", "Action", "ID"});
		
		m_searchField = new JTextField();
		m_searchField.setColumns(10);
		
		JButton btnClearList = new JButton("Clear List");
		btnClearList.addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						while(((DefaultTableModel)m_logList.getModel()).getRowCount() > 0)
						{
							((DefaultTableModel)m_logList.getModel()).removeRow(0);
						}
					}
				});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(m_lblLogo)
					.addPreferredGap(ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
					.addComponent(btnClearList)
					.addGap(36)
					.addComponent(btnClearAll)
					.addGap(169))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(resultScroll, GroupLayout.PREFERRED_SIZE, 947, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblSearchBy)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(m_cbSearchBy, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(m_searchField, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(lblOrderBy)
							.addGap(18)
							.addComponent(m_cbOrderBy, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
							.addGap(46)
							.addComponent(btnSearch)
							.addGap(34)
							.addComponent(btnShowAllLogs))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(7)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSearchBy)
						.addComponent(m_cbSearchBy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnShowAllLogs)
						.addComponent(btnSearch)
						.addComponent(m_cbOrderBy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblOrderBy)
						.addComponent(m_searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(resultScroll, GroupLayout.PREFERRED_SIZE, 384, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnClearAll)
								.addComponent(btnClearList))
							.addContainerGap(35, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(m_lblLogo)
							.addGap(19))))
		);
		setLayout(groupLayout);
		
		IMS.getInstance().addView(this);
	}

	/**
	 * method to set the search results
	 * @param list
	 */
	public void setSearchResults(ArrayList<LogItem> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			LogItem log = list.get(i);
			String action = "";
			
			if (log.getAction() == LogItem.INVENTORY)
				action = "INVENTORY";
			else if (log.getAction() == LogItem.LOGIN)
				action = "LOGIN";
			else if (log.getAction() == LogItem.SALES)
				action = "SALES";
			else if (log.getAction() == LogItem.SYSTEM)
				action = "SYSTEM";
			else
				action = "USER";
			
			String[] strings = new String[] {""+log.getID(),log.getTimeStampString(), action,log.getDesc()};
			
			((DefaultTableModel)m_logList.getModel()).addRow(strings);
		}
	}
	
	/**
	 * method to clear the results list
	 */
	public void clearLists()
	{
		while(((DefaultTableModel)m_logList.getModel()).getRowCount() > 0)
		{
			((DefaultTableModel)m_logList.getModel()).removeRow(0);
		}
	}
	
	/**
	 * method to get the selected search by
	 * @return
	 */
	public int getSearchByIndex()
	{
		return m_cbSearchBy.getSelectedIndex();
	}
	
	/**
	 * method to get the search term from the panel
	 * @return
	 */
	public String getSearchTerm()
	{
		// I ran out of time and this is a quick fix
		String str;
		str = m_searchField.getText();
		
		if (m_cbSearchBy.getSelectedIndex() == 1)  // gotta love magic numbers (hint: it is ACTION)
		{
			str = str.toUpperCase();
			
			if (str.contains("INVENTORY"))
				str = ""+LogItem.INVENTORY;
			else if (str.contains("SYSTEM"))
				str = ""+LogItem.SYSTEM;
			else if (str.contains("USER"))
				str = ""+LogItem.USER;
			else if (str.contains("LOGIN"))
				str = ""+LogItem.LOGIN;
			else if (str.contains("SALES"))
				str = ""+LogItem.SALES;
			
		}
		
		return str;
	}
	
	/**
	 * method to get the selected order by from the panel
	 * @return
	 */
	public int getOrderByIndex()
	{
		return m_cbOrderBy.getSelectedIndex();
	}
	
	@Override
	public void update(Observable arg0, Object arg1)
	{
		setBackground(IMS.getInstance().getTheme().getMainColor());
		//ArrayList<LogItem> items = IMS.getInstance().getLogs(LogItem.ALL);
	}
}
