package w3se.Model.Database;

import java.io.File;
import java.sql.SQLException;
import w3se.Model.Configurations;
import w3se.Model.Base.Book;
import w3se.Model.Base.LogItem;
import w3se.Model.Base.User;

/**
 * 
 * Class  : DatabaseAdaptor.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to manage the databases of the system
 */
@SuppressWarnings("rawtypes")
public class DatabaseAdaptor implements Database
{
	/**
	 * DatabaseAdaptor State - Books Database
	 */
	public static final int BOOK_DB = 1;
	/**
	 * DatabaseAdaptor State - Online Books Database
	 */
	public static final int ONLINE_DB = 2;
	/**
	 * DatabaseAdaptor State - Users Database
	 */
	public static final int USERS_DB = 4;
	/**
	 * DatabaseAdaptor State - Logs Database
	 */
	public static final int LOGS_DB = 16;
	/**
	 * DatabaseAdaptor State - Server Mode
	 */
	public static final int SERVER = 0;
	/**
	 * DatabaseAdaptor State - Client Mode
	 */
	public static final int CLIENT = 1;
	
	private Object m_returnObj = null;
	private BookDB m_bookDB;
	private UsersDB m_usersDB;
	private OnlineDB m_onlineDB;
	private LogsDB m_logsDB;
	
	private static int m_state = 0;
	private int m_serverState = SERVER;
	private DBServer m_server = null;
	private Configurations m_config;
	
	/**
	 * constructor
	 * @param serverState - SERVER or CLIENT mode
	 * @param config - system configurations
	 */
	public DatabaseAdaptor(String serverState, Configurations config)
	{
		if (serverState.equals("SERVER"))
		{
			m_serverState = SERVER;
			String[] serverProperties = new String[6];
			serverProperties[0] = "file:tmp"+File.separator+"UsersDB";
			serverProperties[1] = "UsersDB";
			serverProperties[2] = "file:tmp"+File.separator+"BooksDB";
			serverProperties[3] = "BooksDB";
			serverProperties[4] = "file:tmp"+File.separator+"LogsDB";
			serverProperties[5] = "LogsDB";
			
			m_server = new DBServer(serverProperties, 9001);
			m_server.start();
		}
		else
		{
			m_serverState = CLIENT;
		}
		
		m_bookDB = new BookDB(config);
		m_usersDB = new UsersDB(config);
		m_onlineDB = new OnlineDB();
		m_logsDB = new LogsDB(config);
		
		m_config = config;
	}
	
	/**
	 * method to get the state of the database adaptor
	 * @return - current state
	 */
	public static int getState()
	{
		return m_state;
	}
	
	/**
	 * method to set the state of the database adaptor
	 * @param state - current state
	 */
	public void setState(int state)
	{
		m_state = state;
	}
	
	public void retrieve(Object term) throws Exception
	{
		
		switch (m_state)
		{
			case BOOK_DB:
				m_bookDB.retrieve(term);
				m_returnObj = m_bookDB.getResult();
				break;
				
			case ONLINE_DB:
				m_onlineDB.retrieve(term);
				m_returnObj = m_onlineDB.getResult();
				break;
				
			case USERS_DB:
				m_usersDB.retrieve(term);
				m_returnObj = m_usersDB.getResult();
				break;
			
			case LOGS_DB:
				m_logsDB.retrieve(term);
				m_returnObj = m_logsDB.getResult();
				break;
		}
		
	}
	
	
	public void add(Object obj) throws Exception
	{
		switch (m_state)
		{
			case BOOK_DB:
				m_bookDB.add((Book)obj);
				break;
				
			case ONLINE_DB:
				break;
				
			case USERS_DB:
				m_usersDB.add((User)obj);
				break;
			
			case LOGS_DB:
				m_logsDB.add((LogItem)obj);
				break;
		}
	}
	
	public void remove(Object obj) throws Exception
	{
		switch (m_state)
		{
			case BOOK_DB:
				m_bookDB.remove((Book)obj);
				break;
				
			case ONLINE_DB:
				break;
				
			case USERS_DB:
				m_usersDB.remove((User)obj);
				break;
			
			case LOGS_DB:
				m_logsDB.remove((LogItem)obj);
				break;
		}
	}
	
	public Object getResult() throws Exception
	{
		return m_returnObj;
	}
	
	public void shutdown()
	{
		try
		{
			m_bookDB.shutdown();
			m_usersDB.shutdown();
			m_logsDB.shutdown();
		} catch (SQLException e)
		{
		
			e.printStackTrace();
		}
		m_server.shutdown();
		
	}

	public void close()
	{
		if (m_serverState == SERVER)
			shutdown();
		
		try
		{
			m_usersDB.close();
			m_bookDB.close();
			m_logsDB.close();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * method to get the database's configuration
	 * @return - array of strings
	 */
	public String[] getDatabaseConfig()
	{
		String[] config = new String[5];
		
		config[0] = m_config.getValue("DatabaseDriver");
		config[1] = m_config.getValue("BooksDBUrl");
		config[2] = m_config.getValue("UsersDBUrl");
		config[3] = m_config.getValue("LogsDBUrl");
		config[4] = m_config.getValue("ServerMode");
		
		return config;
	}
	
}
