package w3se.View.Subpanels;

import java.awt.Dimension;
import javax.swing.JPanel;

import w3se.Controller.ConfigViewController;
import w3se.Controller.Controller;
import w3se.Model.IMS;
import w3se.Model.Base.Receipt;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * 
 * Class  : GeneralSettingsPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel to hold general settings of the system
 */
@SuppressWarnings("serial")
public class GeneralSettingsPanel extends JPanel
{

	public static final int WIDTH = 450;
	public static final int HEIGHT = 500;
	
	private Controller m_controller;
	private JCheckBox chcDisplayErrorDialogs;
	private JCheckBox chckbxNotify;
	private JTextField m_txtBusinessName;
	private JTextField m_txtPhoneNum;
	private JTextField m_txtMessage;
	private JTextField m_txtSlogan;
	
	/**
	 * Create the panel.
	 */
	public GeneralSettingsPanel(Controller controller)
	{
		m_controller = controller;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		JLabel lblGeneralSettings = new JLabel("General Settings");
		
		chcDisplayErrorDialogs = new JCheckBox("Display Error Dialogs");
		chcDisplayErrorDialogs.addActionListener(m_controller.getListener(ConfigViewController.ERROR_DIALOG_TOGGLE));
		chckbxNotify = new JCheckBox("Notify when in server or client mode");
		chckbxNotify.addActionListener(m_controller.getListener(ConfigViewController.SERVER_MODE_NOTIFY_TOGGLE));
		
		JButton btnSelectMainColor = new JButton("Select Main Color");
		btnSelectMainColor.addActionListener(m_controller.getListener(ConfigViewController.SELECT_MAIN_COLOR));
		
		JButton btnSelectSecondaryColor = new JButton("Select Secondary Color");
		btnSelectSecondaryColor.addActionListener(m_controller.getListener(ConfigViewController.SELECT_SEC_COLOR));
		
		JLabel lblLookAndFeel = new JLabel("Look and Feel");
		
		JLabel lblReceipt = new JLabel("Receipt Template");
		
		m_txtBusinessName = new JTextField();
		m_txtBusinessName.setColumns(10);
		
		m_txtPhoneNum = new JTextField();
		m_txtPhoneNum.setColumns(10);
		
		m_txtMessage = new JTextField();
		m_txtMessage.setColumns(10);
		
		m_txtSlogan = new JTextField();
		m_txtSlogan.setColumns(10);
		
		JLabel lblBusinessName = new JLabel("Business Name :");
		
		JLabel lblAddress = new JLabel("Phone Number :");
		
		JLabel lblMessage = new JLabel("Message :");
		
		JLabel lblSlogan = new JLabel("Slogan :");
		
		JButton btnAcceptChanges = new JButton("Accept Changes To Receipt");
		btnAcceptChanges.addActionListener(m_controller.getListener(ConfigViewController.ACCEPT_RECEIPT_CHANGES));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(165)
					.addComponent(lblGeneralSettings)
					.addContainerGap(183, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(112)
					.addComponent(lblLookAndFeel)
					.addContainerGap(251, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(80, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSelectSecondaryColor)
						.addComponent(btnSelectMainColor)
						.addComponent(chckbxNotify)
						.addComponent(chcDisplayErrorDialogs))
					.addGap(109))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(107)
					.addComponent(lblReceipt)
					.addContainerGap(235, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(52)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBusinessName)
						.addComponent(lblAddress)
						.addComponent(lblMessage)
						.addComponent(lblSlogan))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(42)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(m_txtPhoneNum, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
								.addComponent(m_txtBusinessName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(m_txtSlogan, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
								.addComponent(m_txtMessage, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
								.addComponent(btnAcceptChanges))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(14))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addComponent(lblGeneralSettings)
					.addGap(29)
					.addComponent(chcDisplayErrorDialogs)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxNotify)
					.addGap(33)
					.addComponent(lblLookAndFeel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSelectMainColor)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSelectSecondaryColor)
					.addGap(40)
					.addComponent(lblReceipt)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_txtBusinessName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblBusinessName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_txtPhoneNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAddress))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMessage)
						.addComponent(m_txtMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_txtSlogan, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSlogan))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAcceptChanges)
					.addContainerGap(15, Short.MAX_VALUE))
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
	 * method to set the show error dialog checkbox
	 * @param value
	 */
	public void setDialogCheckBox(boolean value)
	{
		chcDisplayErrorDialogs.setSelected(value);
	}
	
	/**
	 * method to see if the error dialog checkbox is selected
	 * @return
	 */
	public boolean getDialogCBSelection()
	{
		return chcDisplayErrorDialogs.isSelected();
	}
	
	/**
	 * method the set the server mode notify checkbox
	 * @param value
	 */
	public void setNotifyCheckBox(boolean value)
	{
		chckbxNotify.setSelected(value);
	}
	
	/**
	 * method to see if the server mode notify checkbox is set
	 * @return
	 */
	public boolean getNotifyUserSelection()
	{
		return chckbxNotify.isSelected();
	}
	
	/**
	 * method to get the receipt parameters
	 * @return
	 */
	public Receipt getReceiptTemplate()
	{
		Receipt receipt = new Receipt();
		receipt.setStoreName(m_txtBusinessName.getText());
		receipt.setStorePhoneNumber(m_txtPhoneNum.getText());
		receipt.setMessageToRecipient(m_txtMessage.getText());
		receipt.setSlogan(m_txtSlogan.getText());
		
		return receipt;
	}
	
	/**
	 * method to set the receipt parameters
	 * @param rec
	 */
	public void updateReceipt(Receipt rec)
	{
		m_txtBusinessName.setText(rec.getStoreName());
		m_txtPhoneNum.setText(rec.getStorePhoneNumber());
		m_txtMessage.setText(rec.getMessageToRecipient());
		m_txtSlogan.setText(rec.getSlogan());
	}
}
