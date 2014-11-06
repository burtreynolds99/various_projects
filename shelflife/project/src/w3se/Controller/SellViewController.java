package w3se.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JTable;
import w3se.Model.IMS;
import w3se.Model.Task;
import w3se.Model.Base.Book;
import w3se.Model.Base.LogItem;
import w3se.Model.Base.LogItemFactory;
import w3se.Model.Base.User;
import w3se.Model.Database.BookDB;
import w3se.View.Panels.SellManagePanel;

/**
 * 
 * Class  : SellViewController.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to define listeners for the sell book view
 */
public class SellViewController extends AbstractController
{
	public static final String SEARCH_BY_TERM = "search_by_term";
	public static final String CLEAR_RESULTS = "clear_results";
	public static final String RESULTS_LIST_CLICK = "results_list_click";
	public static final String RECEIPT_LIST_CLICK = "receipt_list_click";
	public static final String RECEIPT_SELL = "receipt_sell";
	public static final String RECEIPT_CANCEL = "receipt_cancel";
	public static final String RECEIPT_PRINT = "receipt_print";
	
	private IMS m_model = null;
	private SellManagePanel m_view = null;
	
	/**
	 * constructor
	 * @param model - main model of the system (IMS)
	 */
	public SellViewController(IMS model)
	{
		m_model = model;
		propagateMap();
	}
	
	// fill the map with listener-key pairs
	protected void propagateMap()
	{
		// listener for keyword search
		addListener(SEARCH_BY_TERM, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						final String search = m_view.getSearchTerm();
						
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{	
										String[] term = new String[2];
										term[0] = BookDB.KEYWORD;
										term[1] = search;
										m_model.findBook(term);						// find the book
										ArrayList<Book> books = m_model.getListOfFoundBooks();
										
										if (books.size() > 0)
											m_model.addToSoldList(books.get(0));	// then add it to the sold list
									}
								}));
					}
				});
		
		// listener to clear the results list
		addListener(CLEAR_RESULTS, new
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
										m_model.setListOfFoundBooks(new ArrayList<Book>());
									}
								}));
					}
				});
		
		
		// listener to watch for a results list item being clicked
		addListener(RESULTS_LIST_CLICK, new
				ListenerAdaptor()
				{
					public void mouseClicked(MouseEvent e)
					{
						JTable table = (JTable)e.getSource();
						final int row = table.rowAtPoint(e.getPoint());
						
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{
										Book book = m_model.getListOfFoundBooks().get(row);
										m_model.addToSoldList(book);
									}
								}));
					}
				});
		
		// listener to add books to the receipt
		addListener(RECEIPT_LIST_CLICK, new
				ListenerAdaptor()
				{
					public void mouseClicked(MouseEvent e)
					{
						JTable table = (JTable)e.getSource();
						final int row = table.rowAtPoint(e.getPoint());
						
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{
										m_model.removeFromSoldList(row);
									}
								}));
					}
				});
		
		// listener to purge the receipt
		addListener(RECEIPT_CANCEL, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{
										m_model.setListOfSoldBooks(new ArrayList<Book>());
										m_view.toggleSellButton(true);
									}
								}));
					}
					
				});
		
		// listener to finalize the sell 		
		addListener(RECEIPT_SELL, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{
										ArrayList<Book> books = m_model.getListofSoldBooks();
										
										for (int i = 0; i < books.size(); i++)
										{
											m_model.addLog(LogItemFactory.createLogItem(LogItem.SALES, books.get(i).getTitle()+" sold for "+books.get(i).getPrice()+"."));
										}
										m_model.finalizeSell();
										m_view.toggleSellButton(false);
									}
								}));
					}
				});
		
		// listener to print the receipt
		addListener(RECEIPT_PRINT, new
				ListenerAdaptor()
				{
					public void actionPerformed(ActionEvent e)
					{
						m_model.getTaskManager().addTask(new Task(User.WORKER, new 
								Runnable()
								{
									public void run()
									{
										m_model.printReceipt();
										m_model.setListOfSoldBooks(new ArrayList<Book>());
										m_view.toggleSellButton(true);
									}
								}));
					}
				});
	}

	
	@Override
	public void registerView(Object view)
	{
		m_view = (SellManagePanel)view;
		m_model.addObserver(m_view);
	}

}
