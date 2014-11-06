package w3se.Model.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import w3se.Model.Configurations;
import w3se.Model.Base.User;

/**
 * 
 * Class  : UsersDB.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to manage access to the users database
 */
public class UsersDB implements Database<User, ArrayList<User>>
{
	private Statement m_statement = null;
	private ResultSet m_resultSet = null;
	private Connection m_connection = null;
	/**
	 * UsersDB Type - search by U_ID
	 */
	public static final String U_ID = "U_id";
	/**
	 * UsersDB Type - search by user name
	 */
	public static final String USERNAME = "Username";
	/**
	 * UsersDB Type - Add a user
	 */
	public static final String ADD = "add";
	/**
	 * UsersDB Type - Edit a user
	 */
	public static final String EDIT = "edit";
	/**
	 * UsersDB Type - Only applicable in searches
	 */
	public static final String ALL = "";
	
	/**
	 * constructor
	 * @param config - system configurations
	 */
	public UsersDB(Configurations config)
	{
		try
		{
			Class.forName(config.getValue("DatabaseDriver")).newInstance();
			m_connection = DriverManager.getConnection(config.getValue("UsersDBUrl"));
		}
		catch (Exception e)
		{
			System.out.println("Failed to JDBC connection with UsersDB");
		}
	}

	@Override
	public void retrieve(Object term) throws Exception
	{
		String[] searchTerm = (String[])term;
		
		m_statement = m_connection.createStatement();
		
		if (searchTerm[0].equals(U_ID))
		{
			searchTerm[1] = Integer.parseInt(searchTerm[1])+"";
			m_resultSet = m_statement.executeQuery("SELECT * FROM Users WHERE "+searchTerm[0]+" = "+searchTerm[1]);
		}
		else if (searchTerm[0].equals(USERNAME))
		{
			searchTerm[1] = "'%"+searchTerm[1]+"%'";
			m_resultSet = m_statement.executeQuery("SELECT * FROM Users WHERE UPPER("+searchTerm[0]+") LIKE UPPER("+searchTerm[1]+")");
		}
	}

	@Override
	public ArrayList<User> getResult() throws Exception
	{
		ArrayList<User> list = new ArrayList<User>();
		
		while (m_resultSet.next())
		{
			User user = new User();
			user.setUID(m_resultSet.getInt("U_id"));
			user.setPrivilege(m_resultSet.getInt("Privilege"));
			user.setUsername(m_resultSet.getString("Username"));
			user.setHashedPassword(m_resultSet.getString("Password"));
			list.add(user);
		}
		return list;	
	}
	
	public void add(User user) throws Exception
	{
		if (user.getUsername() == null || user.getUsername() == "" || user.getPassword() == null || user.getPassword() == "")
			throw new Exception("Error username/password was blank");
		
		if (user.getUsername().equals("admin"))
			throw new Exception("Error username 'admin' is not permitted");
		
		try
		{
			// see if the default user is still in the system
			retrieve(new String[]{USERNAME, "admin"});
			
			// if the default user is still in the system
			if (m_resultSet.next())
			{
				// if you're creating a user that is a manager remove the default user
				if (user.getPrivilege() == User.MANAGER)
					remove(new User(0, User.MANAGER, "admin",""));	// remove the default user
				// if it was found and the user to be made's privilege is too low
				else
					throw new Exception("Error you must create a new manager user first");
			}
			
			retrieve(new String[]{USERNAME, user.getUsername()});
		} 
		catch (Exception e)
		{
			throw e;
		}
		
		if (!m_resultSet.next())
		{
			m_statement = m_connection.createStatement();
			m_statement.execute("INSERT INTO Users VALUES ("+user.getUID()+", "+user.getPrivilege()+", '"+user.getUsername()+"','"+user.getPassword()+"'); COMMIT");	
		}
		else
		{
			m_statement = m_connection.createStatement();
			m_statement.execute("UPDATE Users "+
					"SET Username='"+user.getUsername()+"', Password='"+user.getPassword()+"', Privilege='"+user.getPrivilege()+"', U_id='"+user.getUID()+"'" +
							"WHERE Username = '"+user.getUsername()+"'; COMMIT");
		}
	}
	
	public void shutdown() throws SQLException
	{
		try
		{
			m_statement = m_connection.createStatement();
			m_statement.execute("SHUTDOWN");
			m_statement.close();
		}
		catch (Exception e)
		{}
	}
	
	public void close() throws SQLException
	{
		try
		{
			m_connection.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}

	public void remove(User obj) throws Exception
	{
		m_statement = m_connection.createStatement();
		m_statement.execute("DELETE FROM Users WHERE Username ='"+obj.getUsername()+"'; COMMIT");
	}
	
}
