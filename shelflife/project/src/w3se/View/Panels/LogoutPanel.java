package w3se.View.Panels;

import javax.swing.JPanel;
import javax.swing.JButton;
import w3se.Controller.Controller;
import w3se.Controller.LoginViewController;
import w3se.Model.IMS;
import w3se.Model.Base.States;
import w3se.View.MainView;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SpringLayout;

/**
 * 
 * Class  : LogoutPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for logging out of the system
 */
@SuppressWarnings("serial")
public class LogoutPanel extends JPanel implements Observer
{
	private Controller m_controller = null;
	private MainView m_parent = null;
	private JButton m_btnLogout;
	
	/**
	 * Create the panel.
	 */
	public LogoutPanel(MainView mainView, Object object)
	{
		m_controller = (Controller)object;
		m_parent = mainView;
		
		setBackground(IMS.getInstance().getTheme().getMainColor());
		setBounds(0, 0, 940, 500);
		m_btnLogout = new JButton("Logout");
		m_btnLogout.addActionListener(m_controller.getListener(LoginViewController.LOGOUT));
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, m_btnLogout, 203, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, m_btnLogout, 356, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, m_btnLogout, -109, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, m_btnLogout, -314, SpringLayout.EAST, this);
		setLayout(springLayout);
		add(m_btnLogout);
	}

	public void update(Observable o, Object arg)
	{
		States state = (States)arg;
		m_parent.changeState(state);
		setBackground(IMS.getInstance().getTheme().getMainColor());
	}
	

}
