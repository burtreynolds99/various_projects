package w3se.View.Panels;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import w3se.Controller.Controller;
import w3se.Model.IMS;
import w3se.Model.Base.Book;
import w3se.View.Subpanels.BookInfoPanel;
import w3se.View.Subpanels.SearchPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * 
 * Class  : BookSearchPanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel to manage the book searches
 */
@SuppressWarnings("serial")
public class BookSearchPanel extends JPanel implements Observer
{
	public static final int WIDTH = 1020;
	public static final int HEIGHT = 500;
	public static final int GENERAL_SEARCH = 0;
	public static final int ADD_EDIT_SEARCH = 1;
	
	private SearchPanel m_mainPanel;
	private BookInfoPanel m_infoPanel;
	private int m_state = GENERAL_SEARCH;
	
	/**
	 * Create the panel.
	 */
	public BookSearchPanel(int headerType, boolean editable, Controller controller)
	{
		controller.registerView(this);
		
		if (editable == false)
			m_state = GENERAL_SEARCH;
		else
			m_state = ADD_EDIT_SEARCH;
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setEnabled(false);
		add(splitPane, BorderLayout.CENTER);
		
		m_mainPanel = new SearchPanel(headerType, controller);
		m_infoPanel = new BookInfoPanel(editable, controller);
		
		splitPane.setLeftComponent(m_mainPanel);
		splitPane.setRightComponent(m_infoPanel);
		
		IMS.getInstance().addView(this);
	}
	
	/**
	 * method to retrieve the search term from the form
	 * @param searchTerm
	 */
	public void setSearchTerm(String searchTerm)
	{
		
	}
	
	/**
	 * method to retrieve the search term from the form
	 * @param searchTerm
	 * return 
	 */
	public String getSearchTerm()
	{
		return m_mainPanel.getSearchText();
	}

	/**
	 * method to get the from the book info panel
	 * @return
	 */
	public Book getBook()
	{
		Book book = new Book();
		
		book.setTitle(m_infoPanel.getTitle());
		book.setISBN(m_infoPanel.getISBN());
		book.setPrice(Double.parseDouble(m_infoPanel.getPrice()));
		book.setAuthor(m_infoPanel.getAuthor());
		book.setPublisher(m_infoPanel.getPublisher());
		book.setGenres(m_infoPanel.getGenreList());
		book.setDescription(m_infoPanel.getDesc());
		book.setQuantity(m_infoPanel.getQuantity());
		return book;
	}
	
	/**
	 * method to get the current state of the panel
	 * @return
	 */
	public int getState()
	{
		return m_state;
	}
	
	/**
	 * method to get the selected genre for searching
	 * @return
	 */
	public String getGenre()
	{
		return m_mainPanel.getSelectedGenre();
	}
	
	/**
	 * method to get the selected sort order
	 * @return
	 */
	public int getOrder()
	{
		return m_mainPanel.getSelectedOrder();
	}
	
	public void update(Observable sender, Object obj)
	{
		ArrayList<Book> list = IMS.getInstance().getListOfFoundBooks();
		m_mainPanel.clearSearchText();
		m_mainPanel.clearLists();
		Book book = IMS.getInstance().getCurrentBook();	
		m_infoPanel.setTitle(book.getTitle());
		m_infoPanel.setISBN(book.getISBN());
		m_infoPanel.setPrice(""+book.getPrice());
		m_infoPanel.setAuthor(book.getAuthor());
		m_infoPanel.setPublisher(book.getPublisher());
		m_infoPanel.setDesc(book.getDescription());
		m_infoPanel.setGenreList(book.getGenres());
		m_infoPanel.setQuantity(book.getQuantity());
		m_mainPanel.setSearchResults(list);
		m_mainPanel.setGenres(IMS.getInstance().getGenres().toStringArray());
		//m_mainPanel.setPrevViewedList(IMS.getInstance().getPrevViewedList());
		
		m_mainPanel.updateColor();
		m_infoPanel.updateColor();
	}
	
}
