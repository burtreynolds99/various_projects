package w3se.View.Subpanels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import w3se.Controller.ConfigViewController;
import w3se.Controller.Controller;
import w3se.Model.IMS;
import w3se.Model.Base.User;
import w3se.Model.Database.UsersDB;
import javax.swing.DefaultComboBoxModel;

/**
 * 
 * Class  : AddUserPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for adding users
 */
@SuppressWarnings("serial")
public class AddUserPanel extends JPanel
{
	public static final int WIDTH = 450;
	public static final int HEIGHT = 500;
	public static final int ADD = 0;
	public static final int EDIT = 1;
	
	private JTextField m_usernameField;
	private JPasswordField m_passwordField;
	private JTextField m_uidField;
	private JComboBox m_privileges;
	private Controller m_controller;
	private JTextField m_searchField;
	private JButton m_btnSearch;
	private JLabel m_lblSearch;
	private int m_state = ADD;
	private User m_user = new User();
	private JComboBox m_cbSearchBy;
	
	/**
	 * Create the panel.
	 */
	public AddUserPanel(Controller controller)
	{
		m_controller = controller;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		m_usernameField = new JTextField();
		m_usernameField.setColumns(20);
		
		JLabel lblUsername = new JLabel("Username :");
		
		JLabel lblPassword = new JLabel("Password :");
		
		m_passwordField = new JPasswordField();
		m_passwordField.setColumns(20);
		
		JLabel lblNewLabel = new JLabel("Privilege :");
		String[] privilegesStrs = {"GENERAL", "WORKER", "MANAGER"};
		m_privileges = new JComboBox(privilegesStrs);
		
		m_uidField = new JTextField();
		m_uidField.setColumns(10);
		
		JLabel lblUserId = new JLabel("User ID :");
		
		JLabel lblAddUser = new JLabel("User Configuration");
		
		JButton btnClearFields = new JButton("Clear Fields");
		
		JButton btnAddUser = new JButton("Add User");
		
		btnClearFields.addActionListener(new
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						clear();
					}
				});
		
		m_btnSearch = new JButton("Search");
		m_lblSearch = new JLabel("Find:");
		m_searchField = new JTextField();
		m_searchField.setColumns(10);
		
		m_searchField.addActionListener(m_controller.getListener(ConfigViewController.USER_SEARCH));
		btnAddUser.addActionListener(m_controller.getListener(ConfigViewController.ADD_USER));
		m_btnSearch.addActionListener(m_controller.getListener(ConfigViewController.USER_SEARCH));
		
		m_cbSearchBy = new JComboBox();
		m_cbSearchBy.setModel(new DefaultComboBoxModel(new String[] {"Search by Username", "Search by User ID"}));
		
		m_searchField.setVisible(false);
		m_btnSearch.setVisible(false);
		m_lblSearch.setVisible(false);
		m_cbSearchBy.setVisible(false);
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(27)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(lblPassword)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(m_lblSearch)
										.addComponent(lblUsername)))
								.addComponent(lblNewLabel)
								.addComponent(lblUserId))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(m_usernameField)
										.addComponent(m_uidField)
										.addComponent(m_privileges, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(m_passwordField)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(btnClearFields)
											.addGap(18)
											.addComponent(btnAddUser))
										.addComponent(m_cbSearchBy, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addGap(6)
									.addComponent(m_btnSearch))
								.addComponent(m_searchField, 266, 266, 266)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(151)
							.addComponent(lblAddUser)))
					.addGap(12))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addComponent(lblAddUser)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(m_lblSearch))
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_cbSearchBy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(m_btnSearch))
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUsername))
					.addGap(53)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addGap(54)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(m_privileges, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(55)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_uidField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUserId))
					.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnClearFields)
						.addComponent(btnAddUser))
					.addGap(27))
		);
		setLayout(groupLayout);

	}
	
	/**
	 * method for updating the color of the panel
	 */
	public void updateColor()
	{
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
	}
	
	/**
	 * method to set to edit user mode
	 */
	public void setEditMode()
	{
		m_searchField.setVisible(true);
		m_btnSearch.setVisible(true);
		m_lblSearch.setVisible(true);
		m_cbSearchBy.setVisible(true);
		m_state = EDIT;
	}
	
	/**
	 * method to set to add user mode
	 */
	public void setAddMode()
	{
		m_searchField.setVisible(false);
		m_btnSearch.setVisible(false);
		m_lblSearch.setVisible(false);
		m_cbSearchBy.setVisible(false);
		m_state = ADD;
	}
	
	/**
	 * method to get the search term form the panel
	 * @return
	 */
	public String getSearchTerm()
	{
		return m_searchField.getText();
	}
	
	/**
	 * method to get the selected search by from the panel
	 * @return
	 */
	public String getSearchBy()
	{
		if (m_cbSearchBy.getSelectedIndex() == 0)
			return UsersDB.USERNAME;
		else
			return UsersDB.U_ID;
	}
	
	/**
	 * method to set the current working user for editing
	 * @param user
	 */
	public void setUser(User user)
	{
		m_user = user;
		m_usernameField.setText(user.getUsername());
		m_passwordField.setText("");
		m_privileges.setSelectedIndex(user.getPrivilege());
		m_uidField.setText(""+user.getUID());
	}
	
	/**
	 * method to get the state of the panel
	 * @return
	 */
	public int getState()
	{
		return m_state;
	}
	
	/**
	 * method to get the current edited user
	 * @return
	 */
	public User getUser()
	{
		if (!m_usernameField.getText().equals(""))
			m_user.setUsername(m_usernameField.getText());
		if (!m_passwordField.getText().equals(""))
			m_user.setPassword(m_passwordField.getText());
		m_user.setPrivilege(m_privileges.getSelectedIndex());
		m_user.setUID(Integer.parseInt(m_uidField.getText()));
		
		return m_user;
	}
	
	/**
	 * method to clear the panel
	 */
	public void clear()
	{
		m_searchField.setText("");
		m_usernameField.setText("");
		m_passwordField.setText("");
		m_uidField.setText("");
		m_privileges.setSelectedIndex(0);
	}
	
}
