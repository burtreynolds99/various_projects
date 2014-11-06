package w3se.View.Subpanels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import w3se.Controller.ConfigViewController;
import w3se.Controller.Controller;
import w3se.Model.IMS;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
 * 
 * Class  : ConfigSQLPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panle to display sql configurations
 */
@SuppressWarnings("serial")
public class ConfigSQLPanel extends JPanel
{
	public static final int WIDTH = 450;
	public static final int HEIGHT = 500;
	private static final int PARAM_SIZE = 5;
	
	private Controller m_controller = null;
	private JTextField m_bookDBField;
	private JTextField m_userDBField;
	private JTextField m_logDBField;
	private JComboBox m_cbDriver;
	private JComboBox m_cbMode;
	
	/**
	 * Create the panel.
	 */
	public ConfigSQLPanel(Controller controller)
	{
		m_controller = controller;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		
		JLabel lblSqlConfigurations = new JLabel("SQL Configurations");
		
		JLabel lblBookDatabase = new JLabel("Book Database URL :");
		
		m_bookDBField = new JTextField();
		m_bookDBField.setColumns(10);
		
		JLabel lblUserDatabase = new JLabel("User Database URL :");
		
		m_userDBField = new JTextField();
		m_userDBField.setColumns(10);
		
		JLabel lblLogDatabase = new JLabel("Log Database URL :");
		
		m_logDBField = new JTextField();
		m_logDBField.setColumns(10);
		
		JLabel lblDriver = new JLabel("Driver : ");
		
		JLabel lblMode = new JLabel("Mode : ");
		
		m_cbDriver = new JComboBox(new String[] {"HSQL","MySQL"});
		
		m_cbMode = new JComboBox(new String[] {"SERVER","CLIENT"});
		
		JButton btnAcceptChanges = new JButton("Accept Changes");
		btnAcceptChanges.addActionListener(m_controller.getListener(ConfigViewController.SQL_CONFIG_ACCEPT));
		
		JButton btnCancel = new JButton("Revert To Defaults");
		btnCancel.addActionListener(new
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_cbDriver.setSelectedIndex(0);
						m_bookDBField.setText("jdbc:hsqldb:hsql://localhost/BooksDB;user=SA;password=");
						m_userDBField.setText("jdbc:hsqldb:hsql://localhost/UsersDB;user=SA;password=");
						m_logDBField.setText("jdbc:hsqldb:hsql://localhost/LogsDB;user=SA;password=");
						m_cbMode.setSelectedIndex(0);
					}
				});
		
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGap(130)
							.addComponent(lblSqlConfigurations))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGap(25)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblBookDatabase)
								.addComponent(lblUserDatabase)
								.addComponent(lblLogDatabase)
								.addComponent(lblDriver)
								.addComponent(lblMode))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(m_cbMode, 0, 242, Short.MAX_VALUE)
								.addComponent(m_cbDriver, 0, 242, Short.MAX_VALUE)
								.addComponent(m_logDBField, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
								.addComponent(m_bookDBField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
								.addComponent(m_userDBField, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGap(77)
							.addComponent(btnCancel)
							.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
							.addComponent(btnAcceptChanges)))
					.addGap(42))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSqlConfigurations)
					.addGap(47)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDriver)
						.addComponent(m_cbDriver, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_bookDBField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblBookDatabase))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserDatabase)
						.addComponent(m_userDBField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_logDBField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLogDatabase))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMode)
						.addComponent(m_cbMode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(91)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAcceptChanges)
						.addComponent(btnCancel))
					.addContainerGap(123, Short.MAX_VALUE))
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
	 * method to get the sql configurations
	 * @return
	 */
	public String[] getSQLParams()
	{
		String[] params = new String[PARAM_SIZE];
		
		if (m_cbDriver.getSelectedIndex() == 0)
			params[0] = "org.hsqldb.jdbcDriver";
		else
			params[0] = "com.mysql.jdbc.Driver";
		
		params[1] = m_bookDBField.getText();
		params[2] = m_userDBField.getText();
		params[3] = m_logDBField.getText();
		
		if (m_cbMode.getSelectedIndex() == 0)
			params[4] = "SERVER";
		else
			params[4] = "CLIENT";
		
		return params;
	}
	
	/**
	 * method to set the sql configurations
	 * @param params
	 */
	public void setSQLParams(String[] params)
	{
		if (params[0].equals("org.hsqldb.jdbcDriver"))
			m_cbDriver.setSelectedIndex(0);
		else
			m_cbDriver.setSelectedIndex(1);
		
		m_bookDBField.setText(params[1]);
		m_userDBField.setText(params[2]);
		m_logDBField.setText(params[3]);
		
		if (params[4].equals("SERVER"))
			m_cbMode.setSelectedIndex(0);
		else
			m_cbMode.setSelectedIndex(1);
	}
}
