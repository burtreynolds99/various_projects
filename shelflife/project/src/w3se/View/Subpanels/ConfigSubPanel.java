package w3se.View.Subpanels;

import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EtchedBorder;
import w3se.Controller.Controller;
import w3se.Model.IMS;
import w3se.View.ShelfLifeIcon;
import w3se.View.Panels.ConfigManagePanel;
import javax.swing.JLabel;

/**
 * 
 * Class  : ConfigSubPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel to manage other config views
 */
@SuppressWarnings("serial")
public class ConfigSubPanel extends JPanel
{
	public static final int WIDTH = 450;
	public static final int HEIGHT = 500;
	private ConfigManagePanel m_parent = null;
	private Controller m_controller = null;
	private JToggleButton[] buttons;
	/**
	 * Create the panel.
	 */
	public ConfigSubPanel(ConfigManagePanel parent, Controller controller)
	{
		
		m_parent = parent;
		m_controller = controller;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(IMS.getInstance().getTheme().getMainColor());
		setBorder(new EtchedBorder());
		setLayout(null);
		
		buttons = new JToggleButton[6];
		
		buttons[0] = new JToggleButton("General Settings");
		buttons[0].setBounds(137, 63, 161, 29);
		
		buttons[1] = new JToggleButton("Add User");
		buttons[1].setBounds(137, 116, 161, 29);
		
		buttons[2] = new JToggleButton("Edit User");
		buttons[2].setBounds(137, 177, 161, 29);
		buttons[2].setEnabled(false);
		
		buttons[3]  = new JToggleButton("Remove User");
		buttons[3].setBounds(137, 242, 161, 29);
		buttons[3].setEnabled(false);
		
		buttons[4] = new JToggleButton("SQL Configurations");
		buttons[4].setBounds(137, 301, 161, 29);
		
		buttons[5] = new JToggleButton("Export to File");
		buttons[5].setBounds(137, 363, 161, 29);
		
		add(buttons[0]);
		add(buttons[1]);
		add(buttons[2]);
		add(buttons[3]);
		add(buttons[4]);
		add(buttons[5]);
		
		buttons[0].addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (buttons[0].isSelected())
						{
							m_parent.setInfoPanel(ConfigManagePanel.GENERAL);
							toggleButtons(buttons, 0);
						}
						else
							m_parent.setInfoPanel(-1);
					}
			
				});
		
		buttons[1].addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (buttons[1].isSelected())
						{
							m_parent.setInfoPanel(ConfigManagePanel.USER_ADD);
							toggleButtons(buttons, 1);
						}
						else
							m_parent.setInfoPanel(-1);
					}
			
				});
		
		buttons[2].addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (buttons[2].isSelected())
						{
							m_parent.setInfoPanel(ConfigManagePanel.USER_EDIT);
							toggleButtons(buttons, 2);
						}
						else
							m_parent.setInfoPanel(-1);
					}
			
				});
		
		buttons[3].addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (buttons[3].isSelected())
						{
							m_parent.setInfoPanel(ConfigManagePanel.USER_REMOVE);
							toggleButtons(buttons, 3);
						}
						else
							m_parent.setInfoPanel(-1);
					}
			
				});
		
		buttons[4].addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (buttons[4].isSelected())
						{
							m_parent.setInfoPanel(ConfigManagePanel.SQL_CONFIG);
							toggleButtons(buttons, 4);
						}
						else
							m_parent.setInfoPanel(-1);
					}
			
				});
		
		buttons[5].addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (buttons[5].isSelected())
						{
							m_parent.setInfoPanel(ConfigManagePanel.EXPORT);
							toggleButtons(buttons, 5);
						}
						else
							m_parent.setInfoPanel(-1);
					}
			
				});

		JLabel lblIcon = new JLabel("");
		lblIcon.setIcon(new ShelfLifeIcon());
		lblIcon.setBounds(30, 409, 167, 64);
		add(lblIcon);
		
		
	}
	
	/**
	 * method to update the color of the panel
	 */
	public void updateColor()
	{
		setBackground(IMS.getInstance().getTheme().getMainColor());
	}
	
	/**
	 * method to update the buttons of the panel
	 */
	public void updateButtons()
	{
		boolean b = Boolean.parseBoolean(IMS.getInstance().getConfigurations().getValue("MultiUser"));
		
		buttons[2].setEnabled(b);
		buttons[3].setEnabled(b);
	}
	
	/**
	 * method to toggle a selected button
	 * @param buttons
	 * @param unchangedIndex
	 */
	private void toggleButtons(JToggleButton[] buttons, int unchangedIndex)
	{
		for (int i = 0; i < buttons.length; i++)
		{
			if (i != unchangedIndex)
				buttons[i].setSelected(false);
		}
	}
}
