package w3se.Model.Base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import w3se.Model.Exportable;

/**
 * 
 * Class  : User.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to hold information of a user
 */
public class User implements Exportable
{
	/**
	 * User Type - Base privilege
	 */
	public static final int GENERAL = 0;
	/**
	 * User Type - Intermediate privilege
	 */
	public static final int WORKER = 1;
	/**
	 * User Type - Administrator privilege
	 */
	public static final int MANAGER = 2;
	
	private int m_uid = 0;
	private int m_privilege = GENERAL;
	private String m_name = "admin";
	private String m_password = hashPassword("");
	
	/**
	 * default constructor
	 */
	public User()
	{}
	
	/**
	 * 
	 *
	 * @param id 		ID of the user
	 * @param userType	The type of user (manager, worker, or customer)
	 * @return 
	 * @see     
	 */
	public User(int id, int userType)
	{
		m_uid = id;
		m_privilege = userType;
	}
	
	/**
	 * 
	 *
	 * @param id 		ID of the user
	 * @param userType	The type of user (manager, worker, or customer)
	 * @param uName		Username of the user
	 * @param pass		Password of the user 
	 * @see User    
	 */
	public User(int id, int userType, String uName, String pass)
	{
		m_uid = id;
		m_privilege = userType;
		m_name = uName;
		m_password = hashPassword(pass);
	}
	
	/**
	 *  Get a user ID
	 * 
	 * @return The requested user ID
	 * @see getUID    
	 */
	public int getUID()
	{
		return m_uid;
	}
	
	/**
	 *	Sets a user ID 
	 * 
	 * @param id The new user ID to be set
	 * @see setUID    
	 */
	public void setUID(int id)
	{
		m_uid = id;
	}
	
	/**
	 * Get the privilige of the current user 
	 *
	 * @return The privilege of the current user
	 * @see getPrivilege    
	 */
	public int getPrivilege()
	{
		return m_privilege;
	}
	
	/**
	 * Sets the privilege of a specific user
	 *
	 * @param type The type of privilege given to a user (manager, worker, or customer privilege)
	 * @see setPrivilege    
	 */
	public void setPrivilege(int type)
	{
		m_privilege = type;
	}
	
	/**
	 * Get the username of the current user 
	 *
	 * @return The username of the current user
	 * @see getUsername    
	 */
	public String getUsername()
	{
		return m_name;
	}
	
	/**
	 * Set the username of the current user 
	 *
	 * @param uName The username of the current user
	 * @see setUsername    
	 */
	public void setUsername(String uName)
	{
		m_name = uName;
	}
	
	/**
	 * Get the password of the current user 
	 * 
	 * @return The password of the current user
	 * @see getPassword    
	 */
	public String getPassword()
	{
		return m_password;
	}
	
	/**
	 * Set the password of the current user 
	 *
	 * @param password The password of the current user
	 * @see setPassword    
	 */
	public void setPassword(String password)
	{
		m_password = hashPassword(password);
	}
	
	/**
	 * Set the hash password of the current user 
	 *
	 * @param password
	 * @see setHashedPassword    
	 */
	public void setHashedPassword(String password)
	{
		m_password = password;
	}
	
	/**
	 * A method that hashes a provided password.
	 *
	 * @param pass The password to be hashed 
	 * @return The hashed value of the provided password
	 * @see hashPassword    
	 */
	private String hashPassword(String pass)
	{
		String hash = null;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(pass.getBytes());
			
			byte hashData[] = md.digest();
			
			Formatter formatter = new Formatter();
			
			for (int i = 0; i < hashData.length; i++)
			{
				formatter.format("%02x", hashData[i]);
			}
			
			hash = formatter.toString();
		} catch (NoSuchAlgorithmException e)
		{
			System.out.println("Password hashing failed");
		}
		
		return hash;	
	}
	
	/**
	 *  
	 * 
	 * @return 
	 * @see getExportableTitle    
	 */
	public String getExportableTitle()
	{
		return "User Database Export";
	}
	
	/**
	 * 
	 *
	 * @return 
	 * @see getExportableHeadRow
	 */
	public String[] getExportableHeadRow()
	{
		String[] format = new String[3];
		format[0] = "U_ID";
		format[1] = "Privilege";
		format[2] = "Username";
		
		return format;
	}
	
	/**
	 * 
	 *
	 * @return getExportableRow
	 * @see     
	 */
	public String[] getExportableRow()
	{
		String[] format = new String[3];
		format[0] = ""+m_uid;
		format[1] = ""+m_privilege;
		format[2] = m_name;
		
		return format;
	}
}
