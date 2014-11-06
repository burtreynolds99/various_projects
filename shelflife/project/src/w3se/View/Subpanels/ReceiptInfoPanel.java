package w3se.View.Subpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import w3se.Controller.Controller;
import w3se.Controller.SellViewController;
import w3se.Model.IMS;
import w3se.Model.Base.Book;

import javax.swing.JTable;

/**
 * 
 * Class  : ReceiptInfoPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel to display books being sold
 */
@SuppressWarnings("serial")
public class ReceiptInfoPanel extends JPanel
{
	public static final int WIDTH = 450;
	public static final int HEIGHT = 500;
	private Controller m_controller = null;
	private JTable m_receiptList = null;
	private JLabel m_lblTotal = null;
	private double m_total = 0.0;
	private NumberFormat m_numFormat;
	private JButton m_btnSell;
	
	/**
	 * Create the panel.
	 */
	public ReceiptInfoPanel(Controller controller)
	{
		m_controller = controller;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		
		JLabel lblReceipt = new JLabel("Receipt");
	
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.WHITE);
		m_numFormat = NumberFormat.getCurrencyInstance(Locale.US);
		m_lblTotal = new JLabel("Total : " + m_numFormat.format(m_total));
		
		m_btnSell = new JButton("Sell");
		m_btnSell.addActionListener(m_controller.getListener(SellViewController.RECEIPT_SELL));
		
		JButton btnPrint = new JButton("Print Receipt");
		btnPrint.addActionListener(m_controller.getListener(SellViewController.RECEIPT_PRINT));
		
		JButton btnCancel = new JButton("Clear");
		btnCancel.addActionListener(m_controller.getListener(SellViewController.RECEIPT_CANCEL));
		
		DefaultTableModel model = new DefaultTableModel(new String[] {"ISBN", "Title", "Author", "Price"}, 0)
		{
			public boolean isCellEditable(int row, int height)
			{
				return false;
			}
		};
		m_receiptList = new JTable(model);

		m_receiptList.setBorder(new EtchedBorder());
		JScrollPane receiptScroll = new JScrollPane(m_receiptList);
		
		m_receiptList.addMouseListener(m_controller.getListener(SellViewController.RECEIPT_LIST_CLICK));
		
		GroupLayout gl_infoPanel = new GroupLayout(this);
		gl_infoPanel.setHorizontalGroup(
			gl_infoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(receiptScroll, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addComponent(separator, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addComponent(lblReceipt)
							.addGap(193))))
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addGap(48)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(m_lblTotal, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addComponent(m_btnSell)
							.addGap(18)
							.addComponent(btnPrint)
							.addGap(18)
							.addComponent(btnCancel)))
					.addContainerGap(82, Short.MAX_VALUE))
		);
		gl_infoPanel.setVerticalGroup(
			gl_infoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addGap(23)
					.addComponent(lblReceipt)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(receiptScroll, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
					.addGap(9)
					.addComponent(m_lblTotal)
					.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_btnSell)
						.addComponent(btnPrint)
						.addComponent(btnCancel))
					.addGap(38))
		);
		setLayout(gl_infoPanel);
	}
	
	/**
	 * method to toggle the sell button
	 * @param isEnabled
	 */
	public void toggleSellButton(boolean isEnabled)
	{
		m_btnSell.setEnabled(isEnabled);
	}
	
	/**
	 * method to set the list of books in the invoice
	 * @param list
	 */
	public void setReceiptList(ArrayList<Book> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Book book = list.get(i);
			
			String[] strings = new String[] {book.getISBN(), book.getTitle(),book.getAuthor(),""+book.getPrice()};
			
			((DefaultTableModel)m_receiptList.getModel()).addRow(strings);
		}
	}
	
	/**
	 * method to update the color of the panel
	 */
	public void updateColor()
	{
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
	}
	
	/**
	 * method to clear the receipt
	 */
	public void clearReceipt()
	{
		while(((DefaultTableModel)m_receiptList.getModel()).getRowCount() > 0)
		{
			((DefaultTableModel)m_receiptList.getModel()).removeRow(0);
		}
	}
	
	/**
	 * method to set the total price of the receipt
	 * @param total
	 */
	public void setTotalPrice(double total)
	{
		m_total = total;
		m_lblTotal.setText("Total : " + m_numFormat.format(m_total));
	}
	
	/**
	 * method to get the total price of the sell
	 * @return
	 */
	public double getTotalPrice()
	{
		return m_total;
	}
	
}
