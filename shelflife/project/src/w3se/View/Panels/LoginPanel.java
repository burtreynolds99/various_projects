package w3se.View.Panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import w3se.Controller.Controller;
import w3se.Controller.LoginViewController;
import w3se.Model.IMS;
import w3se.Model.Base.States;
import w3se.Model.Base.User;
import w3se.View.MainView;
import w3se.View.ShelfLifeIcon;
import java.util.Observable;
import java.util.Observer;

/**
 * 
 * Class  : LoginPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for logging in to the system
 */
@SuppressWarnings("serial")
public class LoginPanel extends JPanel implements Observer
{
	private JTextField m_userNameField;
	private JPasswordField m_passwordField;
	private Controller m_controller = null;
	private MainView m_parent = null;
	private JButton m_btnLogin = null;
	
	/**
	 * Create the panel.
	 */
	public LoginPanel(MainView mainView, Controller controller)
	{
		m_parent = mainView;
		
		m_controller = controller;
		m_controller.registerView(this);
		
		setBackground(IMS.getInstance().getTheme().getMainColor());
		
		JLabel lblUserName = new JLabel("User Name :");
		lblUserName.setBounds(353, 144, 76, 16);
		
		m_userNameField = new JTextField();
		m_userNameField.setBounds(458, 138, 254, 28);
		m_userNameField.setColumns(20);
		
		JLabel lblPassword = new JLabel("Password : ");
		lblPassword.setBounds(353, 218, 71, 16);
		
		m_passwordField = new JPasswordField("");
		m_passwordField.setBounds(458, 212, 254, 28);
		m_passwordField.setColumns(20);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(540, 269, 79, 29);
		
		
		btnLogin.addActionListener(m_controller.getListener(LoginViewController.LOGIN));
		m_passwordField.addActionListener(m_controller.getListener(LoginViewController.LOGIN));
		m_controller.registerView(this);
		IMS.getInstance().addView(this);
		setLayout(null);
		add(lblUserName);
		add(lblPassword);
		add(m_passwordField);
		add(m_userNameField);
		add(btnLogin);
		
		JLabel lblIcon = new JLabel("");
		lblIcon.setIcon(new ShelfLifeIcon());
		lblIcon.setBounds(75, 409, 167, 64);
		add(lblIcon);
	}

	/**
	 * method to set the username to the panel
	 * @param name
	 */
	public void setUserName(String name)
	{
		m_userNameField.setText(name);
	}
	
	/**
	 * method to set the password to the panel
	 * @param pass
	 */
	public void setPassword(String pass)
	{
		m_passwordField.setText(pass);
	}
	
	/**
	 * method to get the username from the panel
	 * @return
	 */
	public String getUsername()
	{
		return m_userNameField.getText();
	}
	
	/**
	 * method to get the password from the panel
	 * @return
	 */
	public String getPassword()
	{
		return m_passwordField.getText();
	}
	
	/**
	 * method to get the login attempt user of the panel
	 * @return
	 */
	public User getUser()
	{
		User user = new User();
		
		if (!m_userNameField.getText().equals(""))
			user.setUsername(m_userNameField.getText());
		if (!m_passwordField.getText().equals(""))
			user.setPassword(m_passwordField.getText());
		
		return user;
	}
	
	@Override
	public void update(Observable o, Object arg)
	{
		setBackground(IMS.getInstance().getTheme().getMainColor());
		States state = IMS.getInstance().getLoginState();
		m_parent.changeState(state);
	}
}
