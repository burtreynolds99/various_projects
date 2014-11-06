package w3se.View.Subpanels;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import w3se.Controller.ConfigViewController;
import w3se.Controller.Controller;
import w3se.Model.IMS;
import w3se.Model.Base.User;
import w3se.Model.Database.UsersDB;
import java.util.ArrayList;

/**
 * 
 * Class  : RemoveUserPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for removing users
 */
@SuppressWarnings("serial")
public class RemoveUserPanel extends JPanel
{
	public static final int WIDTH = 450;
	public static final int HEIGHT = 500;
	public static final int SEARCH_BY_USERNAME = 0;
	public static final int SEARCH_BY_USER_ID = 1;
	
	private JTextField m_searchField;
	private JTable m_userList;
	private Controller m_controller;
	private JComboBox cbSearchBy;
	
	/**
	 * Create the panel.
	 */
	public RemoveUserPanel(Controller controller)
	{
		m_controller = controller;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		
		JLabel lblTitle = new JLabel("Remove User");
		
		m_searchField = new JTextField();
		m_searchField.setColumns(10);
		m_searchField.addActionListener(m_controller.getListener(ConfigViewController.REMOVE_USER_SEARCH));
		
		JLabel lblFind = new JLabel("Find :");
		JButton btnSearch = new JButton("Search");
		JButton btnCancel = new JButton("Clear");
		
		btnSearch.addActionListener(m_controller.getListener(ConfigViewController.REMOVE_USER_SEARCH));
		btnCancel.addActionListener(m_controller.getListener(ConfigViewController.REMOVE_USER_CLEAR));
		
		DefaultTableModel model = new DefaultTableModel(new String[] {"User ID", "Username"}, 0)
		{
			public boolean isCellEditable(int row, int height)
			{
				return false;
			}
		};
		
		m_userList = new JTable(model);
		m_userList.setBorder(new EtchedBorder());
		
		m_userList.addMouseListener(m_controller.getListener(ConfigViewController.REMOVE_USER_DELETE));
		JScrollPane userListScroll = new JScrollPane(m_userList);
		
		cbSearchBy = new JComboBox(new String[] {"Search by Username", "Search by User ID"});
		
		JButton btnRemoveUsers = new JButton("Remove Users");
		btnRemoveUsers.addActionListener(m_controller.getListener(ConfigViewController.REMOVE_USER_ACCEPT));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(166)
					.addComponent(lblTitle)
					.addContainerGap(63, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(32)
					.addComponent(lblFind)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(cbSearchBy, 0, 237, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(btnSearch))
						.addComponent(m_searchField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
					.addGap(31))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(55)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCancel)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnRemoveUsers))
						.addComponent(userListScroll, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(59, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(17)
					.addComponent(lblTitle)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFind))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(5)
							.addComponent(btnSearch))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cbSearchBy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(38)
					.addComponent(userListScroll, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnRemoveUsers))
					.addGap(32))
		);
		setLayout(groupLayout);
	}
	
	/**
	 * method to update the color of the panel
	 */
	public void updateColor()
	{
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
	}
	
	/**
	 * method to get the search term
	 * @return
	 */
	public String getSearchTerm()
	{
		return m_searchField.getText();
	}
	
	/**
	 * method to get the selected search by
	 * @return
	 */
	public String getSearchBy()
	{
		if (cbSearchBy.getSelectedIndex() == 0)
			return UsersDB.USERNAME;
		else
			return UsersDB.U_ID;
	}
	
	/**
	 * method to clear the remove user list
	 */
	public void clearList()
	{
		while(((DefaultTableModel)m_userList.getModel()).getRowCount() > 0)
		{
			((DefaultTableModel)m_userList.getModel()).removeRow(0);
		}
	}
	
	/**
	 * method to set the remove user list
	 * @param list
	 */
	public void setRemoveList(ArrayList<User> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			User user = list.get(i);
			
			String[] strings = new String[] {""+user.getUID(),user.getUsername()};
			
			((DefaultTableModel)m_userList.getModel()).addRow(strings);
		}
	}
	
}
