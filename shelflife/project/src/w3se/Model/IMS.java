package w3se.Model;


import java.awt.Color;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import w3se.Model.Base.Book;
import w3se.Model.Base.BookGenres;
import w3se.Model.Base.LogItem;
import w3se.Model.Base.LogItemFactory;
import w3se.Model.Base.Package;
import w3se.Model.Base.ProAtCooking;
import w3se.Model.Base.Receipt;
import w3se.Model.Base.States;
import w3se.Model.Base.Theme;
import w3se.Model.Base.User;
import w3se.Model.Database.BookDB;
import w3se.Model.Database.DatabaseAdaptor;
import w3se.Model.Database.LogsDB;
import w3se.Model.Database.UsersDB;
import w3se.View.SplashScreen;

/**
 * 
 * Class  : IMS.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to serve as the main model, the Inventory Management System
 */
public class IMS extends Observable implements Observer
{
	private static IMS m_instance = null;
	private States m_state = States.LOGGED_OUT;
	private User m_currentUser = new User(-1, User.GENERAL, "General", "General");
	private User m_volatileUser = new User(-1, User.GENERAL, "", "");
	private TaskManager m_scheduler = new TaskManager();
	private DatabaseAdaptor m_dbAdaptor = null;
	private Book m_currentBook = new Book();
	private ArrayList<Book> m_foundBookList = new ArrayList<Book>();
	private ArrayList<Book> m_soldBookList = new ArrayList<Book>();
	private ArrayList<Book> m_prevViewedList = new ArrayList<Book>();
	private ArrayList<User> m_removeUserList = new ArrayList<User>();
	private ArrayList<LogItem> m_logList = new ArrayList<LogItem>();
	private Exporter m_exporter;
	private Configurations m_config;
	private BookGenres m_genres = new BookGenres();
	private boolean m_showDialogs = true;
	private boolean m_showNotifySMode = true;
	private Theme m_theme;
	private Receipt m_receipt;
	private Package m_resources;
	
	/**
	 * source location of the resource file
	 */
	public final String RESOURCES_S = "resources.slr";
	
	/**
	 * destination location for the unpacked resource file
	 */
	public final String RESOURCES_D = "tmp";
	
	/**
	 * configuration file's filename
	 */
	public final String CONFIG_F = "config";
	
	/**
	 * ShelfLife's icon image filename
	 */
	public String SL_ICON;
	
	/**
	 * ShelfLife's logo image filename
	 */
	public String SL_LOGO;
	
	/**
	 * We Three Software Engineers logo image filename
	 */
	public String W3SE_LOGO;
	
	
	/**
	 * constructor
	 * @exclude 
	 * @param fixPackage - debugging purposed whether to fix the package
	 * @param fixType - debugging fix type (0 - unpack, 1 - pack)
	 */
	private IMS(boolean fixPackage, int fixType)
	{
		// debugging purposes only
		if (fixPackage)
		{
			fixResource(fixType);
			System.exit(0);
		}
		
		init();
		SL_ICON = m_config.getValue("Icon");
		SL_LOGO = m_config.getValue("Logo");
		W3SE_LOGO = m_config.getValue("W3SE");
	}
	
	/**
	 * constructor
	 */
	private IMS()
	{
		// initialize the system
		init();
		// set global variables for image locations
		SL_ICON = m_config.getValue("Icon");
		SL_LOGO = m_config.getValue("Logo");
		W3SE_LOGO = m_config.getValue("W3SE");
	}
	
	/**
	 * method to set the resource package key
	 */
	private void setResourcePKGKey()
	{
		// create a container string
		String hash = null;
		
		// get a half hearted attempt at not hardcoding a key key
		String key = getClass().getCanonicalName()+getClass().getModifiers();
		// try to create the hash
		try
		{
			// create a new hash with SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(key.getBytes());
			
			// get the hash
			byte hashData[] = md.digest();
			
			// create a new formatter to format the hash
			Formatter formatter = new Formatter();
			
			// format the hash
			for (int i = 0; i < hashData.length; i++)
			{
				formatter.format("%02x", hashData[i]);
			}
			
			// set the hash value in the container string
			hash = formatter.toString();
		} 
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("Password hashing failed");
		}	
		
		// set the key for the resources
		m_resources.setKey(hash);
	}
	
	/**
	 * method to fix the package file (debug purposes)
	 * @exclude 
	 * @param type
	 */
	public void fixResource(int type)
	{
		// unpack the resources first and foremost 
		m_resources = new Package(RESOURCES_S, RESOURCES_D, "");
		setResourcePKGKey();
		
		if (type == 0)
			m_resources.unpack();
		
		else
			m_resources.pack();
	}
	
	/**
	 * method to initialize the system and setup the environment
	 */
	public void init()
	{
		// unpack the resources first and foremost 
		m_resources = new Package(RESOURCES_S, RESOURCES_D, "");
		setResourcePKGKey();
		m_resources.unpack();

		// get the system configurations 
		m_config = Configurations.getConfigFromFile(RESOURCES_D+File.separator+CONFIG_F);
		
		// create a new splash screen
		SplashScreen splash = new SplashScreen(RESOURCES_D+File.separator+m_config.getValue("W3SELogo"));
		splash.run();
		
		// set the configurations to IMS
		m_showDialogs = Boolean.parseBoolean(m_config.getValue("ErrorDialog"));
		m_showNotifySMode = Boolean.parseBoolean(m_config.getValue("NotifyOfSMode"));

		if (m_showNotifySMode)
			JOptionPane.showMessageDialog(null, "Running in "+ m_config.getValue("ServerMode")+ " mode.", "Mode of Operation", JOptionPane.INFORMATION_MESSAGE);

		// get the theme from the configurations
		m_theme = new Theme(new Color(Integer.parseInt(m_config.getValue("MainColor"))), new Color(Integer.parseInt(m_config.getValue("SecColor"))));
		
		// create a new receipt object and set it's values
		m_receipt = new Receipt();
		m_receipt.setStoreName(m_config.getValue("BusinessName"));
		m_receipt.setStorePhoneNumber(m_config.getValue("BusinessPhoneNumber"));
		m_receipt.setMessageToRecipient(m_config.getValue("BusinessMessage"));
		m_receipt.setSlogan(m_config.getValue("BusinessSlogan"));

		// get the genre list from a file
		m_genres.mergeFromFile(RESOURCES_D+File.separator+m_config.getValue("GenreList"));
		
		// register this to the TaskManager
		m_scheduler.addObserver(this);
		// create a new database adaptor to work with
		m_dbAdaptor = new DatabaseAdaptor(m_config.getValue("ServerMode"), m_config);
	}
	
	/**
	 * gotta love singletons (GLOBAL CHAOS!)
	 * @return - IMS instance
	 */
	public static IMS getInstance()
	{
		if (m_instance == null)	
			m_instance = new IMS(false, 0);
		
		return m_instance;
	}
	
	/**
	 * singleton which will terminate after the package is unpacked or packed
	 * @param fixPackage - if you want to fix the resource file
	 * @param fixType - what type of fix is it ('0' - Unpack, '1' - Pack);
	 * @exclude
	 * @return
	 */
	public static IMS getInstance(boolean fixPackage, int fixType)
	{
		if (m_instance == null)
			m_instance = new IMS(fixPackage, fixType);
		
		return m_instance;
	}
	
	/**
	 * method to set the current user of the system
	 * @param user - active user
	 */
	public void setUser(User user)
	{
		m_currentUser = user;
	}
	
	
	/**
	 * method to login to the system
	 * @throws Exception - if the login fails
	 */
	@SuppressWarnings("unchecked")
	public void login() throws Exception
	{
		// set the state of the database adaptor
		m_dbAdaptor.setState(DatabaseAdaptor.USERS_DB);
		try
		{
			// retrieve the active user from the users database
			m_dbAdaptor.retrieve(new String[] {UsersDB.USERNAME, m_currentUser.getUsername()});
		}
		catch (Exception e)
		{
			addLog(LogItemFactory.createLogItem(LogItem.LOGIN, "Login failure."));
			throw new Exception("Login Failed: Username or password incorrect");
		}
		
		// if the retrieval worked get the results from the database adaptor
		ArrayList<User> userList = (ArrayList<User>)m_dbAdaptor.getResult();
		
		// if the user list has users
		if (userList.size() > 0)
		{
			// get the first from the first
			User user = userList.get(0);
			
			// if the active user's username and password match the one in the database
			if (m_currentUser.getUsername().equals(user.getUsername()) && m_currentUser.getPassword().equals(user.getPassword()))
			{
				// change the state to logged in
				m_state = States.LOGGED_IN;
				// set the active user
				m_currentUser = user;
			}
			// if authentication failed
			else
			{
				// change the state to logged out
				m_state = States.LOGGED_OUT;
				// add an error log entry
				addLog(LogItemFactory.createLogItem(LogItem.LOGIN, "Login failure."));
				throw new Exception("Login Failed: Username or password incorrect");
			}
		}
		// if the user list had no objects
		else
			throw new Exception("Username or password incorrect");
	}
	
	/**
	 * method to logout
	 */
	public void logout()
	{
		// simply change the state to logged out
		m_state = States.LOGGED_OUT;
	}
	
	/**
	 * method to add a user to the system
	 * @param user
	 * @return - boolean success or failure
	 */
	public boolean createUser(User user)
	{
		// set the database adaptor state to users db
		m_dbAdaptor.setState(DatabaseAdaptor.USERS_DB);
		
		// try to add it to the database
		try
		{
			m_dbAdaptor.add(user);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Add user to the system", JOptionPane.ERROR_MESSAGE);
			addLog(LogItemFactory.createLogItem(LogItem.SYSTEM, "Unable to create user."));
			return false;
		}
		return true;
	}
	
	/**
	 * method to call when the system is going to shutdown
	 * @throws Exception - if any errors occur during clean up
	 */
	public void shutdownSystem() throws Exception
	{
		System.out.println("Exiting.....");
		logout();
		addLog(LogItemFactory.createLogItem(LogItem.LOGIN, getCurrentUser().getUsername()+" logged out of the system."));
		m_dbAdaptor.close();
		m_genres.writeToFile();
		m_config.writeToFile();
		m_resources.pack();
		m_resources.close();
	}
	
	/**
	 * method to get the current state of the system
	 * @return - LOGGED_IN, or LOGGED_OUT
	 */
	public States getLoginState()
	{
		return m_state;
	}
	
	/**
	 * method to get the active user of the system
	 * @return - active user
	 */
	public User getCurrentUser()
	{
		User copy = new User(m_currentUser.getUID(), m_currentUser.getPrivilege(), m_currentUser.getUsername(), m_currentUser.getPassword());
		return copy;
	}
	
	/**
	 * method to retreive a user 
	 * @param term String[] - first index is the query type (UserDB.U_ID or UserDB.USERNAME) second index holds the id 
	 * or the username
	 * @return ArrayList<User> - found users matching the criteria
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<User> retreiveUser(String[] term)
	{
		// set the state of the database adaptor to users db
		m_dbAdaptor.setState(DatabaseAdaptor.USERS_DB);
		// container list for retrieved users
		ArrayList<User> user = new ArrayList<User>();
		
		// try to find user/s
		try
		{
			m_dbAdaptor.retrieve(term);
			user = (ArrayList<User>)m_dbAdaptor.getResult();
		} 
		catch (Exception e)
		{
			addLog(LogItemFactory.createLogItem(LogItem.SYSTEM, "Unable to find user."));
		}
		
		return user;
	}
	
	/**
	 * method to register a view to this
	 * @param obs - Observer
	 */
	public void addView(Observer obs)
	{
		addObserver(obs);
		setChanged();
	}
	
	/**
	 * method to get the task manager
	 * @return TaskManager - reference to the task manager of the system
	 */
	public TaskManager getTaskManager()
	{
		return m_scheduler;
	}
	
	/**
	 * method to update
	 */
	@Override
	public void update(Observable sender, Object obj)
	{
		// if the task manager fired the event
		if (obj != null && obj.equals("task_added"))
		{
			// try to run the task
			try
			{
				m_scheduler.runTask();
			}
			catch (Exception e)
			{
				// if the task failed 
				if (m_showDialogs)
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				addLog(LogItemFactory.createLogItem(LogItem.SYSTEM, "Privilege Authenication Failure."));
			}
			// flag as changed then fire to all observers
			setChanged();
			notifyObservers();
		}
	}
	
	/**
	 * method to find a requested book
	 * @param searchTerm String[] - first index is query type (BookDB.KEYWORD or BookDB.BROWSE) second index is the actual
	 * searchable term
	 */
	@SuppressWarnings("unchecked")
	public void findBook(final String[] searchTerm)
	{
		// set the database adaptor to the books database
		m_dbAdaptor.setState(DatabaseAdaptor.BOOK_DB);
		// try to find the book/s
		try
		{
			m_dbAdaptor.retrieve(searchTerm);
			m_foundBookList = (ArrayList<Book>)m_dbAdaptor.getResult();
			
			// if the first search term is ONLINE and the no books were found then allow online lookup
			if (searchTerm[0] == BookDB.ONLINE && m_foundBookList.size() <= 0)
			{
				// if the active user has high enough privilege
				if (m_currentUser.getPrivilege() > User.GENERAL)
				{
					// create a new task
					m_scheduler.addTask(new Task(User.WORKER, new 
							Runnable()
							{	
								public void run()
								{
									// set the database adaptor to the online database
									m_dbAdaptor.setState(DatabaseAdaptor.ONLINE_DB);
									// try to find the book
									try
									{
										m_dbAdaptor.retrieve(searchTerm[1]);
										m_foundBookList = (ArrayList<Book>)m_dbAdaptor.getResult();
										if (m_foundBookList.size() > 0)
											m_currentBook = m_foundBookList.get(0);
									} 
									// if no book was found
									catch (Exception e)
									{
										if (m_showDialogs)
											JOptionPane.showMessageDialog(null, e.getMessage(), "Book not found.", JOptionPane.ERROR_MESSAGE);
										
										addLog(LogItemFactory.createLogItem(LogItem.INVENTORY, "Unable to find book."));
									}
									
								}
							}));
				}
			}
			// if no book was found
			if (m_foundBookList.size() <= 0)
			{
				if (m_showDialogs)
					JOptionPane.showMessageDialog(null, "Book not in inventory.", "Book not found.", JOptionPane.ERROR_MESSAGE);
				addLog(LogItemFactory.createLogItem(LogItem.INVENTORY, "Unable to find book."));
		
			}
		}
		catch (Exception e)
		{
			if (m_showDialogs)
				JOptionPane.showMessageDialog(null, e.getMessage(), "Book not found.", JOptionPane.ERROR_MESSAGE);
			addLog(LogItemFactory.createLogItem(LogItem.INVENTORY, "Unable to find book."));
		}
	}
	
	public void findBookOnline(final String isbn)
	{
		if (m_currentUser.getPrivilege() > User.GENERAL)
		{
			m_scheduler.addTask(new Task(User.WORKER, new 
					Runnable()
					{	
						public void run()
						{
							m_dbAdaptor.setState(DatabaseAdaptor.ONLINE_DB);
							try
							{
								m_dbAdaptor.retrieve(isbn);
								m_foundBookList = (ArrayList<Book>)m_dbAdaptor.getResult();
								if (m_foundBookList.size() > 0)
									m_currentBook = m_foundBookList.get(0);
							} 
							catch (Exception e)
							{
								if (m_showDialogs)
									JOptionPane.showMessageDialog(null, e.getMessage(), "Book not found.", JOptionPane.ERROR_MESSAGE);
								
								addLog(LogItemFactory.createLogItem(LogItem.INVENTORY, "Unable to find book."));
							}
							
						}
					}));
		}
	}
	
	/**
	 * method to remove a book from the books db
	 * @param book
	 */
	public void removeBookFromDB(Book book)
	{
		// try to remove the book
		try
		{
			m_dbAdaptor.remove(book);
		} catch (Exception e)
		{
			if (m_showDialogs)
				JOptionPane.showMessageDialog(null, "Book removal error", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * method to get the list of found books from the system (found books are the result of findBook(String[] searchTerm)
	 * @return ArrayList<Book> - list of found books from the system
	 */
	public ArrayList<Book> getListOfFoundBooks()
	{
		return m_foundBookList;
	}
	
	/**
	 * method to get the current book of the system
	 * @return Book - get the current active book in the system
	 */
	public Book getCurrentBook()
	{
		return m_currentBook;
	}
	
	/**
	 * method to set the list of found books
	 * @param books
	 */
	public void setListOfFoundBooks(ArrayList<Book> books)
	{
		m_foundBookList = books;
	}
	
	/**
	 * method to add a book to the sold list of the system
	 * @param book
	 */
	public void addToSoldList(Book book)
	{
		m_soldBookList.add(book);
	}
	
	/**
	 * method to get the list of sold books from the system
	 * @return
	 */
	public ArrayList<Book> getListofSoldBooks()
	{
		return m_soldBookList;
	}
	
	/**
	 * method to remove a particular book from the sold list of a system
	 * @param index
	 */
	public void removeFromSoldList(int index)
	{
		m_soldBookList.remove(index);
	}
	
	/**
	 * method to set the list of sold books
	 */
	public void setListOfSoldBooks(ArrayList<Book> list)
	{
		m_soldBookList = list;
	}
	
	/**
	 * method to finalize the sell of all the books in the sold list (decrement the quantity of books and log the sell)
	 */
	public void finalizeSell()
	{
		for (int i = 0; i < m_soldBookList.size(); i++)
		{
			Book book = m_soldBookList.get(i);
			book.setQuantity(book.getQuantity() - 1);
			
			setCurrentBook(book);
			addCurrentBookToDB();
		}
	}
	
	/**
	 * method to set the current book of the system
	 * @param book
	 */
	public void setCurrentBook(Book book)
	{
		//m_prevViewedList.add(book);
		m_currentBook = book;
	}
	
	/**
	 * method to select a particular book from the found book list
	 * @param index
	 * @return
	 */
	public Book selectBook(int index)
	{
		return m_foundBookList.get(index);
	}
	
	/**
	 * method to insert the current active book of the system into the book database
	 */
	public void addCurrentBookToDB()
	{
		// try to add the book to the database
		try
		{
			// get the genres from the current book
			m_genres.mergeFromBook(m_currentBook);
			// set state of the database adaptor to the books db
			m_dbAdaptor.setState(DatabaseAdaptor.BOOK_DB);
			// add the book
			m_dbAdaptor.add(m_currentBook);
		} catch (Exception e)
		{
			addLog(LogItemFactory.createLogItem(LogItem.SYSTEM, "Unable to add book to database."));
		}
	}
	
	
	// uncomment later
	/*public ArrayList<Book> getPrevViewedList()
	{
		return m_prevViewedList;
	}*/
	
	/**
	 * method to set the Receipt object of the system
	 * @param receipt
	 */
	public void setReceipt(Receipt receipt)
	{
		m_receipt = receipt;
	}
	
	/**
	 * method to get the Receipt object of the system
	 * @return
	 */
	public Receipt getReceipt()
	{
		return m_receipt;
	}
	
	/**
	 * method to merge the genres from a book with the main genres list of the system
	 * @param book
	 */
	public void addNewGenresFromBook(Book book)
	{
		m_genres.mergeFromBook(book);
	}
	
	/**
	 * method to get the genres list of the system
	 * @return BookGenres
	 */
	public BookGenres getGenres()
	{
		return m_genres;
	}
	
	/**
	 * method to set the mutable user of the system (used for editing a previous user) 
	 * @param user
	 */
	public void setVolatileUser(User user)
	{
		m_volatileUser = user;
	}
	
	/**
	 * method to get the current mutable user of the system
	 * @return
	 */
	public User getVolatileUser()
	{
		return m_volatileUser;
	}
	
	/**
	 * method to print the receipt of from the sold book list
	 */
	public void printReceipt()
	{
		try
		{
			m_receipt.printReceipt(m_soldBookList);
		}
		catch (Exception e)
		{
			addLog(LogItemFactory.createLogItem(LogItem.SYSTEM, "Error printing receipt"));
			if (m_showDialogs)
				JOptionPane.showMessageDialog(null, "Printing receipt error", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * method to add a user to the dispose list of the system
	 * @param user
	 */
	public void addToRemoveUserList(User user)
	{
		m_removeUserList.add(user);
	}
	
	/**
	 * method to purge the dispose list without removing user from the database
	 */
	public void clearRemoveUserList()
	{
		m_removeUserList = new ArrayList<User>();
	}
	
	/**
	 * method to get the dispose list of the system
	 * @return
	 */
	public ArrayList<User> getRemoveUserList()
	{
		return m_removeUserList;
	}
	
	/**
	 * method to remove a particular user from the dispose list
	 * @param index
	 */
	public void removeFromRemoveUserList(int index)
	{
		m_removeUserList.remove(index);
	}
	
	/**
	 * method to remove a particular user from the user database
	 * @param user
	 */
	public void removeUserFromDB(User user)
	{
		m_dbAdaptor.setState(DatabaseAdaptor.USERS_DB);
		try
		{
			m_dbAdaptor.remove(user);
		} 
		catch (Exception e)
		{
			addLog(LogItemFactory.createLogItem(LogItem.SYSTEM, "Unable to remove user from database."));
		}
	}

	/**
	 * method to get the database adaptor of the system
	 * @return DatabaseAdaptor
	 */
	public DatabaseAdaptor getDatabaseAdaptor()
	{
		return m_dbAdaptor;
	}
	
	/**
	 * method to get the system configurations
	 * @return Configurations
	 */
	public Configurations getConfigurations()
	{
		return m_config;
	}
	
	/**
	 * method to see if the system is set to show dialogs
	 * @return
	 */
	public boolean showDialogs()
	{
		return m_showDialogs;
	}
	
	/**
	 * method to see if the system is to show server mode notification
	 * @return
	 */
	public boolean showNotifyOfServerMode()
	{
		return m_showNotifySMode;
	}
	
	/**
	 * method to set the visibility of the error dialogs
	 * @param isEnabled
	 */
	public void toggleDialog(boolean isEnabled)
	{
		m_showDialogs = isEnabled;
	}
	
	/**
	 * method to set the visibility of the server mode dialog
	 * @param isEnabled
	 */
	public void toggleNotifySMode(boolean isEnabled)
	{
		m_showNotifySMode = isEnabled;
	}
	
	/**
	 * method to add a log entry to the log database
	 * @param logItem
	 */
	public void addLog(LogItem logItem)
	{
		// set the state of the database adaptor to the logs db
		m_dbAdaptor.setState(DatabaseAdaptor.LOGS_DB);
		try
		{
			m_dbAdaptor.add(logItem);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * method to get the log entries from the system
	 * @param actionType - LogItem.Action
	 * @return - list of log items
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<LogItem> getLogs(String[] term)
	{
		// set the state of the database adaptor to logs db
		m_dbAdaptor.setState(DatabaseAdaptor.LOGS_DB);
		ArrayList<LogItem> items = new ArrayList<LogItem>();
		
		// if the easter egg was found
		if (term[2].equals("ProAtCooking-Dave"))
		{
			ProAtCooking pac = new ProAtCooking();
			pac.playSound(RESOURCES_D+File.separator+m_config.getValue("ProAtSecrets"));	
		}
		
		// try to find the log/s
		try
		{
			m_dbAdaptor.retrieve(term);
			items = (ArrayList<LogItem>)m_dbAdaptor.getResult();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return items;
	}
	
	/**
	 * method to remove all the logs of the system
	 */
	public void removeLogs()
	{
		m_dbAdaptor.setState(DatabaseAdaptor.LOGS_DB);
		LogItem item = new LogItem();
		item.setAction(LogItem.ALL);
		try
		{
			m_dbAdaptor.remove(item);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * set the exporter for the system
	 * @param exporter
	 */
	public void setExporter(Exporter exporter)
	{
		m_exporter = exporter;
	}
	
	/**
	 * method to set the theme of the system
	 * @param theme
	 */
	public void setTheme(Theme theme)
	{
		m_theme = theme;
	}
	
	/**
	 * method to get the theme of the system
	 * @return
	 */
	public Theme getTheme()
	{
		return m_theme;
	}
	
	/**
	 * method to export a database to a particular file
	 * @param dbType - DatabaseAdaptor.BOOK_DB, DatabaseAdaptor.USERS_DB, DatabaseAdaptor.ONLINE_DB, DatabaseAdaptor.LOGS_DB
	 * @param filename
	 */
	@SuppressWarnings("rawtypes")
	public void export(int dbType, String filename)
	{
		m_exporter.setFilename(filename);
		ArrayList list;
		
		switch (dbType)
		{
			case DatabaseAdaptor.BOOK_DB:
				findBook(new String[] {BookDB.KEYWORD, ""});	// find all the books
				list = m_foundBookList;
				break;
			case DatabaseAdaptor.USERS_DB:
				list = (ArrayList<User>)retreiveUser(new String[] {UsersDB.USERNAME, ""});
				break;
			case DatabaseAdaptor.LOGS_DB:
				 list = getLogs(new String[] {LogsDB.ALL, LogsDB.TIME,""});
				break;
			default:
				list = new ArrayList();
				break;
		}
		
		try
		{
			m_exporter.export(list);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
