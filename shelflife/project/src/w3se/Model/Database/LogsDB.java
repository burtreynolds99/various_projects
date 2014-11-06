package w3se.Model.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import w3se.Model.Configurations;
import w3se.Model.Base.LogItem;

public class LogsDB implements Database<LogItem, ArrayList<LogItem>>
{
	private Statement m_statement = null;
	private ResultSet m_resultSet = null;
	private Connection m_connection = null;
	public static final String ALL = "ALL";
	public static final String ACTION = "Action";
	public static final String TIME = "Timestamp";
	public static final String ID = "L_id";
	
	public LogsDB(Configurations config)
	{
		try
		{
			Class.forName(config.getValue("DatabaseDriver")).newInstance();
			m_connection = DriverManager.getConnection(config.getValue("LogsDBUrl"));
		} catch (Exception e)
		{
			System.out.println("Failed to JDBC connection with LogsDB");
		}
		
	}

	@Override
	public void retrieve(Object term) throws Exception
	{
		m_statement = m_connection.createStatement();
		
		String[] searchTerm = (String[])term;
		
		if (searchTerm[0].equals(ALL))
			m_resultSet = m_statement.executeQuery("SELECT * FROM Logs ORDER BY "+searchTerm[1]);
		else
		{
			m_resultSet = m_statement.executeQuery("SELECT * FROM Logs WHERE "+searchTerm[0]+" LIKE '%"+searchTerm[2]+"%' ORDER BY "+searchTerm[1]);
		}
		
		
		
		
	}

	@Override
	public ArrayList<LogItem> getResult() throws Exception
	{
		ArrayList<LogItem> items = new ArrayList<LogItem>();
		
		while (m_resultSet.next())
		{
			LogItem log = new LogItem();
		
			log.setID(m_resultSet.getLong("L_id"));
			log.setTimeStamp(m_resultSet.getTimestamp("Timestamp"));
			log.setAction(m_resultSet.getInt("Action"));
			log.setDesc(m_resultSet.getString("Description"));
			
			items.add(log);
		}
		return items;	
	}
	
	public void add(LogItem log) throws SQLException
	{
		log.setDesc(log.getDesc().replace("'", "''"));
		
		m_statement = m_connection.createStatement();
		m_statement.execute("INSERT INTO Logs VALUES ("+log.getID()+", NOW(), "+log.getAction()+",'"+log.getDesc()+"'); COMMIT");	
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

	public void remove(LogItem log) throws Exception
	{
		m_statement = m_connection.createStatement();
		
		if (log.getAction() == LogItem.ALL)
			m_statement.execute("DELETE FROM Logs; COMMIT");
		
		else
			m_statement.execute("DELETE FROM Logs WHERE L_id = "+log.getID()+"; COMMIT");
	}

	
}

