package w3se.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import w3se.Model.IMS;
import w3se.Model.Task;
import w3se.Model.Base.Book;
import w3se.Model.Base.LogItem;
import w3se.Model.Base.LogItemFactory;
import w3se.Model.Base.User;
import w3se.Model.Database.BookDB;
import w3se.View.Panels.BookSearchPanel;

/**
 * 
 * Class  : BookSearchController.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Nov 26, 2012
 * Desc   : Class to create controllers for the BookSearch View
 */
public class BookSearchController extends AbstractController
{
	public static final String SEARCH_BY_TERM = "search_by_term";
	public static final String CLEAR_RESULTS = "clear_results";
	public static final String CLEAR_BOOK_INFO = "clear_book_info";
	public static final String ACCEPT_BOOK_INFO = "accept_book_info";
	public static final String RESULTS_LIST_CLICK = "results_list_click";
	public static final String TAB_CHANGED = "tab_changed";
	public static final String SEARCH_BY_BROWSE = "search_by_browse";
	public static final String REMOVE_BOOK = "remove_book";
	
	private IMS m_model = null;
	private BookSearchPanel m_view = null;
	
	/**
	 * constructor
	 * @param model - main model of the system (IMS)
	 */
	public BookSearchController(IMS model)
	{
		m_model = model;
		propagateMap();
	}
	
	// fill the hash map listener-key pairs
	protected void propagateMap()
	{
		// listener for keyword search
		addListener(SEARCH_BY_TERM, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						final String search = m_view.getSearchTerm();
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
										String[] term = new String[2];
										
										if (m_view.getState() == BookSearchPanel.GENERAL_SEARCH)
											term[0] = BookDB.KEYWORD;
										else
											term[0] = BookDB.ONLINE;
										
										term[1] = search;
										m_model.findBook(term);
										ArrayList<Book> books = m_model.getListOfFoundBooks();
										
										if (books.size() > 0)
											m_model.setCurrentBook(books.get(0));
									}
								}));
					}
				});
		
		// listener for clearing the results list
		addListener(CLEAR_RESULTS, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
										m_model.setCurrentBook(new Book());
										m_model.setListOfFoundBooks(new ArrayList<Book>());
									}
								}));
					}
				});
		
		// listener for clearing the book info panel
		addListener(CLEAR_BOOK_INFO, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{
										m_model.setCurrentBook(new Book());
									}
								}));
					}
				});
		
		// listener for accepting the changes to a book object
		addListener(ACCEPT_BOOK_INFO, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{
										m_model.setCurrentBook(m_view.getBook());
										m_model.addCurrentBookToDB();
										m_model.addLog(LogItemFactory.createLogItem(LogItem.INVENTORY, m_view.getBook().getTitle()+" added to the database."));
										m_model.setCurrentBook(new Book());
										
									}
								}));
					}
				});
		
		// listener for clicking on a results list item
		addListener(RESULTS_LIST_CLICK, new
				ListenerAdaptor()
				{
					public void mouseClicked(MouseEvent e)
					{
						JTable table = (JTable)e.getSource();
						final int row = table.rowAtPoint(e.getPoint());
						
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
										Book book = m_model.selectBook(row);
										m_model.setCurrentBook(book);
									}
								}));
					}
				});
		
		// listener to tell when the tab has changed
		addListener(TAB_CHANGED, new
				ListenerAdaptor()
				{
					public void stateChanged(ChangeEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
									}
								}));
					}
				});
		
		// listener for browse search
		addListener(SEARCH_BY_BROWSE, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.GENERAL, new 
								Runnable()
								{
									public void run()
									{
										String[] term = new String[3];
										term[0] = BookDB.BROWSE;
										term[1] = m_view.getGenre();
										term[2] = ""+m_view.getOrder();
										m_model.findBook(term);
									}
								}));
					}
				});
		
		// listener for removing a book
		addListener(REMOVE_BOOK, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{
										Book book = m_view.getBook();
										int result = JOptionPane.showConfirmDialog(null, "Remove book "+book.getTitle()+"?", "Remove Book From Database", JOptionPane.YES_NO_OPTION);
										
										if (result == JOptionPane.YES_OPTION)
											m_model.removeBookFromDB(book);
									}
								}));
					}
				});
		
	}


	@Override
	public void registerView(Object view)
	{
		m_view = (BookSearchPanel)view;
	}


}
