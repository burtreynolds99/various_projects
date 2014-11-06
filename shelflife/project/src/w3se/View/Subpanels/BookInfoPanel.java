package w3se.View.Subpanels;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import w3se.Controller.BookSearchController;
import w3se.Controller.Controller;
import w3se.Model.IMS;

import javax.swing.JButton;

/**
 * 
 * Class  : BookInfoPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for displaying book information
 */
@SuppressWarnings("serial")
public class BookInfoPanel extends JPanel
{
	public static final int WIDTH = 450;
	public static final int HEIGHT = 500;
	private Controller m_controller = null;
	private JTextField m_titleField;
	private JTextField m_ISBNField;
	private JTextField m_priceField;
	private JTextField m_authorField;
	private JTextField m_publisherField;
	private JTextPane m_descTextPane;
	private JTextField m_genreList;
	private JSpinner m_quantity;
	
	/**
	 * Create the panel.
	 */
	public BookInfoPanel(boolean editable, Controller controller)
	{
		m_controller = controller;
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		
		JLabel lblBookInformation = new JLabel("Book Information");
		
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.WHITE);
		
		JLabel lblTitle = new JLabel("Title:");
		
		JLabel lblISBN = new JLabel("ISBN:");
		
		JLabel lblPrice = new JLabel("Price: $");
		
		JLabel lblAuthor = new JLabel("Author:");
		
		JLabel lblPublisher = new JLabel("Publisher:");
		
		JLabel lblCategories = new JLabel("Genres:");
		
		JLabel lblDesc = new JLabel("Description:");
		
		JLabel lblQuantity = new JLabel("Quantity:");
		m_titleField = new JTextField();
		m_titleField.setEditable(editable);
		m_titleField.setColumns(14);
		
		m_ISBNField = new JTextField();
		m_ISBNField.setEditable(editable);
		m_ISBNField.setColumns(14);
		
		m_priceField = new JTextField();
		m_priceField.setEditable(editable);
		m_priceField.setColumns(14);
		
		m_authorField = new JTextField();
		m_authorField.setEditable(editable);
		m_authorField.setColumns(13);
		
		m_publisherField = new JTextField();
		m_publisherField.setEditable(editable);
		m_publisherField.setColumns(11);
		
		m_descTextPane = new JTextPane();
		m_descTextPane.setEditable(editable);
		
		m_genreList = new JTextField();
		m_genreList.setColumns(13);
		m_genreList.setEditable(editable);
		
		m_quantity = new JSpinner();
		m_quantity.setEnabled(editable);
		
		JScrollPane genrePane = new JScrollPane();
		
		genrePane.add(m_genreList);
		
		JButton btnClear = new JButton("Clear");
		
		JButton btnAccept = new JButton("Accept");
		
		JButton btnRemove = new JButton("Remove");
		
		btnClear.addActionListener(m_controller.getListener(BookSearchController.CLEAR_BOOK_INFO));
		btnAccept.addActionListener(m_controller.getListener(BookSearchController.ACCEPT_BOOK_INFO));
		btnRemove.addActionListener(m_controller.getListener(BookSearchController.REMOVE_BOOK));
		
		GroupLayout gl_infoPanel = new GroupLayout(this);
		gl_infoPanel.setHorizontalGroup(
			gl_infoPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addGap(17)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAuthor)
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPrice)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(m_priceField, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblQuantity)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(m_quantity, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addComponent(lblISBN)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(m_ISBNField, GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addComponent(lblTitle)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(m_titleField, GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addGroup(gl_infoPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPublisher)
								.addComponent(lblCategories))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_infoPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_infoPanel.createSequentialGroup()
									.addGap(6)
									.addComponent(m_genreList, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
								.addGroup(gl_infoPanel.createSequentialGroup()
									.addGap(6)
									.addComponent(m_publisherField, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
								.addComponent(m_authorField, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)))
						.addGroup(gl_infoPanel.createSequentialGroup()
							.addComponent(lblDesc)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_infoPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_infoPanel.createSequentialGroup()
									.addComponent(btnClear)
									.addGap(36)
									.addComponent(btnAccept)
									.addGap(36)
									.addComponent(btnRemove))
								.addComponent(m_descTextPane, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))))
					.addContainerGap())
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addContainerGap(186, Short.MAX_VALUE)
					.addComponent(lblBookInformation)
					.addGap(155))
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_infoPanel.setVerticalGroup(
			gl_infoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addGap(17)
					.addComponent(lblBookInformation)
					.addGap(18)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTitle)
						.addComponent(m_titleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblISBN)
						.addComponent(m_ISBNField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_priceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPrice))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_quantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblQuantity))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAuthor)
						.addComponent(m_authorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPublisher)
						.addComponent(m_publisherField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategories)
						.addComponent(m_genreList, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDesc)
						.addComponent(m_descTextPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnClear)
						.addComponent(btnAccept)
						.addComponent(btnRemove))
					.addGap(16))
		);
		setLayout(gl_infoPanel);
		
		if (editable == false)
		{
			btnClear.setEnabled(false);
			btnAccept.setEnabled(false);
			btnClear.setVisible(false);
			btnAccept.setVisible(false);
			btnRemove.setEnabled(false);
			btnRemove.setVisible(false);
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
	 * method to get the title of the book 
	 * @return
	 */
	public String getTitle()
	{
		return m_titleField.getText();
	}

	/**
	 * method to set the title of the book
	 * @param str
	 */
	public void setTitle(String str)
	{
		m_titleField.setText(str);
	}

	/**
	 * method to get the isbn
	 * @return
	 */
	public String getISBN()
	{
		return m_ISBNField.getText();
	}

	/**
	 * method to set the isbn
	 * @param str
	 */
	public void setISBN(String str)
	{
		m_ISBNField.setText(str);
	}

	/**
	 * method to get the price of the book
	 * @return
	 */
	public String getPrice()
	{
		return m_priceField.getText();
	}

	/**
	 * method to set the price of the book
	 * @param str
	 */
	public void setPrice(String str)
	{
		m_priceField.setText(str);
	}

	/**
	 * method to get the author
	 * @return
	 */
	public String getAuthor()
	{
		return m_authorField.getText();
	}

	/**
	 * method to set the author
	 * @param str
	 */
	public void setAuthor(String str)
	{
		m_authorField.setText(str);
	}

	/**
	 * method to get the publisher
	 * @return
	 */
	public String getPublisher()
	{
		return m_publisherField.getText();
	}

	/**
	 * method to set the publisher
	 * @param str
	 */
	public void setPublisher(String str)
	{
		m_publisherField.setText(str);
	}

	/**
	 * method to get the description of the book
	 * @return
	 */
	public String getDesc()
	{
		return m_descTextPane.getText();
	}

	/**
	 * method to set the description of the book
	 * @param str
	 */
	public void setDesc(String str)
	{
		m_descTextPane.setText(str);
	}

	/**
	 * method to get the genres of the book
	 * @return
	 */
	public String getGenreList()
	{
		return m_genreList.getText();
	}

	/**
	 * method to set the genres of the book
	 * @param genreList
	 */
	public void setGenreList(String genreList)
	{
		m_genreList.setText(genreList);
	}
	
	/**
	 * method to get the quantity of the book
	 * @return
	 */
	public int getQuantity()
	{
		return (Integer)m_quantity.getValue();
	}
	
	/**
	 * method to set the quantity of the book
	 * @param value
	 */
	public void setQuantity(int value)
	{
		m_quantity.setValue(value);
	}
}
