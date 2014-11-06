package w3se.View.Subpanels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import w3se.Controller.BookSearchController;
import w3se.Controller.Controller;
import w3se.Model.IMS;
import w3se.Model.Base.Book;
import w3se.View.ShelfLifeIcon;

/**
 * 
 * Class  : SearchPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for searching for a book
 */
@SuppressWarnings("serial")
public class SearchPanel extends JPanel
{
	/**
	 * 
	 */
	public static final int WIDTH = 540;
	public static final int HEIGHT = 500;
	public static final int HEADER_HEIGHT = 80;
	public static final int KEYWORD_HEADER = 0;
	public static final int BROWSE_HEADER = 1;
	public static final int ORDER_PRICE_ASCEND = 0;
	public static final int ORDER_PRICE_DESCEND= 1;
	public static final int ORDER_TITLE = 2;
	public static final int ORDER_AUTHOR = 3;
	public static final int ORDER_PUBLISHER =4;
	
	private Controller m_controller = null;
	
	private int m_headerType = KEYWORD_HEADER;
	
	private JTextField m_searchField;
	private JTable m_resultsList;
	private	JTable m_prevViewedList;
	private JPanel m_header;
	private JPanel m_body;
	private JLabel m_lblRecentlyViewed;
	private JLabel m_lblResults;
	private JLabel m_lblLogo;
	private JComboBox m_cbGenres = new JComboBox(new DefaultComboBoxModel());
	private JComboBox m_cbOrder = new JComboBox(new DefaultComboBoxModel());
	private int m_genreSelection = 0;
	
	/**
	 * Create the panel.
	 */
	public SearchPanel(int headerType, Controller controller)
	{
		m_controller = controller;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		m_headerType = headerType;
		
		createHeader(m_headerType);
		createBody();
		
		setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, BorderLayout.CENTER);
		splitPane.setEnabled(false);
		splitPane.setTopComponent(m_header); // set the top panel
		splitPane.setBottomComponent(m_body);
		
	}
	
	/**
	 * method to create the top of the panel
	 * @param headerType
	 */
	private void createHeader(int headerType)
	{
		m_header = new JPanel();
		m_header.setPreferredSize(new Dimension(WIDTH, HEADER_HEIGHT));
		m_header.setBackground(IMS.getInstance().getTheme().getSecondaryColor());
		
		JButton searchBtn = new JButton("Search");
		
		if (headerType == KEYWORD_HEADER)
		{
			m_header.setLayout(new FlowLayout());
			JLabel kwSearchLbl = new JLabel("Keyword Search");
			
			m_searchField = new JTextField();
			m_searchField.setColumns(20);
			
			m_header.add(kwSearchLbl);
			m_header.add(m_searchField);
			m_header.add(searchBtn);
			
			searchBtn.addActionListener(m_controller.getListener(BookSearchController.SEARCH_BY_TERM));
			m_searchField.addActionListener(m_controller.getListener(BookSearchController.SEARCH_BY_TERM));
		}
		else if (headerType == BROWSE_HEADER)
		{
			JLabel lblGenres =  new JLabel("Genres");
			JLabel lblOrder = new JLabel("Order");
			m_cbGenres.setSize(70, 10);
			
			searchBtn.addActionListener(m_controller.getListener(BookSearchController.SEARCH_BY_BROWSE));
			m_cbOrder.setModel(new DefaultComboBoxModel(new String[] {"Price Ascending", "Price Descending", "Title","Author", "Publisher"}));
			GroupLayout gl_m_header = new GroupLayout(m_header);
			gl_m_header.setHorizontalGroup(
				gl_m_header.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_m_header.createSequentialGroup()
						.addGap(22)
						.addGroup(gl_m_header.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_m_header.createSequentialGroup()
								.addComponent(lblOrder)
								.addGap(18)
								.addComponent(m_cbOrder, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(searchBtn))
							.addGroup(gl_m_header.createSequentialGroup()
								.addComponent(lblGenres)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(m_cbGenres, GroupLayout.PREFERRED_SIZE, 413, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(227, Short.MAX_VALUE))
			);
			gl_m_header.setVerticalGroup(
				gl_m_header.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_m_header.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_m_header.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblGenres)
							.addComponent(m_cbGenres, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_m_header.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblOrder)
							.addComponent(m_cbOrder, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(searchBtn)))
			);
			m_header.setLayout(gl_m_header);
		}
	}
	
	/**
	 * method to create the body of the panel
	 */
	@SuppressWarnings("serial")
	private void createBody()
	{
		m_body = new JPanel();
		m_body.setBackground(IMS.getInstance().getTheme().getMainColor());
		m_body.setPreferredSize(new Dimension(WIDTH, (HEIGHT-HEADER_HEIGHT)));
		
		// create an anonymous class to set the cells as not editable
		DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Title", "Price", "Author", "Genres"}, 0)
			{
				public boolean isCellEditable(int row, int height)
				{
					return false;
				}
			};
			
		m_resultsList = new JTable(tableModel);
		m_resultsList.setBorder(new EtchedBorder());
		JScrollPane resultScroll = new JScrollPane(m_resultsList);
		
		m_resultsList.addMouseListener(m_controller.getListener(BookSearchController.RESULTS_LIST_CLICK));
	
		m_prevViewedList = new JTable(tableModel);
		m_prevViewedList.setBorder(new EtchedBorder());
		
		JScrollPane prevViewScroll = new JScrollPane(m_prevViewedList);
		
		m_prevViewedList.addMouseListener(m_controller.getListener("prev_viewed_list"));
		
		
		m_lblRecentlyViewed = new JLabel("Recently Viewed Books");
		
		m_lblResults = new JLabel("Search Results");
		
		
		m_lblLogo = new JLabel();
		m_lblLogo.setIcon(new ShelfLifeIcon());
		
		JButton btnClearResults = new JButton("Clear Results");
		btnClearResults.addActionListener(m_controller.getListener(BookSearchController.CLEAR_RESULTS));
		JButton btnClearRecentlyViewed = new JButton("Clear Recently Viewed");
		btnClearRecentlyViewed.addActionListener(m_controller.getListener("recently_clear"));
		
		btnClearRecentlyViewed.setVisible(false);
		m_lblRecentlyViewed.setVisible(false);
		m_lblRecentlyViewed.setVisible(false);
		prevViewScroll.setVisible(false);
		
		GroupLayout gl_m_body = new GroupLayout(m_body);
		gl_m_body.setHorizontalGroup(
			gl_m_body.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_m_body.createSequentialGroup()
					.addGap(16)
					.addGroup(gl_m_body.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_m_body.createSequentialGroup()
							.addComponent(resultScroll, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_m_body.createSequentialGroup()
							.addComponent(m_lblResults)
							.addGap(215)
							.addComponent(btnClearResults)
							.addGap(88))
						.addGroup(gl_m_body.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(m_lblLogo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_m_body.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_m_body.createSequentialGroup()
									.addComponent(prevViewScroll, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
									.addContainerGap())
								.addGroup(gl_m_body.createSequentialGroup()
									.addComponent(m_lblRecentlyViewed)
									.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
									.addComponent(btnClearRecentlyViewed)
									.addGap(51))))))
		);
		gl_m_body.setVerticalGroup(
			gl_m_body.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_m_body.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_m_body.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_lblResults)
						.addComponent(btnClearResults))
					.addGap(12)
					.addGroup(gl_m_body.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_m_body.createSequentialGroup()
							.addGroup(gl_m_body.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnClearRecentlyViewed)
								.addComponent(m_lblRecentlyViewed))
							.addGap(18)
							.addComponent(prevViewScroll, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
						.addGroup(gl_m_body.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(resultScroll, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(m_lblLogo)))
					.addGap(105))
		);
		
		m_body.setLayout(gl_m_body);
	}
	
	/**
	 * method to update the color of the panel
	 */
	public void updateColor()
	{
		m_body.setBackground(IMS.getInstance().getTheme().getMainColor());
		m_header.setBackground(IMS.getInstance().getTheme().getSecondaryColor());
	}
	
	
	/**
	 * method to return the text in the search field
	 * @return
	 */
	public String getSearchText()
	{
		return m_searchField.getText();
	}
	
	/**
	 * method to clear the search bar
	 */
	public void clearSearchText()
	{
		if (m_headerType == KEYWORD_HEADER)
			m_searchField.setText("");
	}
	
	/**
	 * method to set the search results list
	 * @param list
	 */
	public void setSearchResults(ArrayList<Book> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Book book = list.get(i);
			
			String[] strings = new String[] {book.getTitle(),""+book.getPrice(),book.getAuthor(),book.getGenres()};
			
			
			((DefaultTableModel)m_resultsList.getModel()).addRow(strings);
		}
	}
	
	/**
	 * @ignore
	 * @param list
	 */
	public void setPrevViewedList(ArrayList<Book> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Book book = list.get(i);
			
			String[] strings = new String[] {book.getTitle(),""+book.getPrice(),book.getAuthor(),book.getGenres()};
			
			
			((DefaultTableModel)m_prevViewedList.getModel()).addRow(strings);
		}
	}
	
	/**
	 * method to clear the results list
	 */
	public void clearLists()
	{
		while(((DefaultTableModel)m_resultsList.getModel()).getRowCount() > 0)
		{
			((DefaultTableModel)m_resultsList.getModel()).removeRow(0);
		}
		
		while(((DefaultTableModel)m_prevViewedList.getModel()).getRowCount() > 0)
		{
			((DefaultTableModel)m_prevViewedList.getModel()).removeRow(0);
		}
	}
	
	/**
	 * method to set the genres
	 * @param genres
	 */
	public void setGenres(String[] genres)
	{	
		m_cbGenres.setModel(new DefaultComboBoxModel(genres));
		m_cbGenres.setSelectedIndex(m_genreSelection);
	}
	
	/**
	 * method to get the selected genre
	 * @return
	 */
	public String getSelectedGenre()
	{
		m_genreSelection = m_cbGenres.getSelectedIndex();
		return (String)m_cbGenres.getModel().getSelectedItem();
	}
	
	/**
	 * method to get the selected search order
	 * @return
	 */
	public int getSelectedOrder()
	{
		return m_cbOrder.getSelectedIndex();
	}
	
}
