package w3se.Model.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import w3se.Model.Configurations;
import w3se.Model.Base.Book;
import w3se.View.Subpanels.SearchPanel;

/**
 * 
 * Class  : BookDB.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to manage the books database
 */
public class BookDB implements Database<Book, ArrayList<Book>>
{
	private Statement m_statement = null;
	private ResultSet m_resultSet = null;
	private Connection m_connection = null;
	public static final String KEYWORD = "keyword";
	public static final String BROWSE = "browse";
	public static final String ONLINE = "keyword";
	
	
	public BookDB(Configurations config)
	{	
		try
		{
			Class.forName(config.getValue("DatabaseDriver")).newInstance();
			m_connection = DriverManager.getConnection(config.getValue("BooksDBUrl"));
		}
		catch (Exception e)
		{
			System.out.println("Failed to JDBC connection with BooksDB");
		}
	}
	
	public void retrieve(Object term ) throws SQLException
	{		
		String[] searchTerm = (String[])term;
		
		if (searchTerm[0].equals(KEYWORD))
		{
			
			m_statement = m_connection.createStatement();
			m_resultSet = m_statement.executeQuery("SELECT * FROM Books WHERE ISBN LIKE UPPER('%"+searchTerm[1]+
			"%') OR UPPER(Title) LIKE UPPER('%"+searchTerm[1]+"%') OR UPPER(Author) LIKE UPPER('%"+searchTerm[1]+"%')"+
			"OR UPPER(Publisher) LIKE UPPER('%"+searchTerm[1]+"%')");
		}
		
		else if (searchTerm[0].equals(BROWSE))
		{
			String orderBy = "";
			
			if (searchTerm[1].equals("ANY"))
				searchTerm[1] = "";
				
			if (searchTerm[2].equals(""+SearchPanel.ORDER_PRICE_ASCEND))
					orderBy = "ORDER BY Price ASC";
			else if (searchTerm[2].equals(""+SearchPanel.ORDER_PRICE_DESCEND))
					orderBy = "ORDER BY Price DESC";
			else if (searchTerm[2].equals(""+SearchPanel.ORDER_TITLE))
					orderBy = "ORDER BY Title";
			else if (searchTerm[2].equals(""+SearchPanel.ORDER_AUTHOR))
					orderBy = "ORDER BY Author";
			else if (searchTerm[2].equals(""+SearchPanel.ORDER_PUBLISHER))
					orderBy = "ORDER BY Publisher";
			
			m_statement = m_connection.createStatement();
			m_resultSet = m_statement.executeQuery("SELECT * FROM Books WHERE UPPER(GENRES) LIKE UPPER('%"+searchTerm[1]+
			"%')"+orderBy);
			
			
		}
	}
	
	public ArrayList<Book> getResult() throws SQLException
	{
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		while (m_resultSet.next())
		{
			Book book = new Book();
			book.setTitle(m_resultSet.getString("Title"));
			book.setISBN(m_resultSet.getString("ISBN"));
			book.setPrice(m_resultSet.getDouble("Price"));
			book.setAuthor(m_resultSet.getString("Author"));
			book.setPublisher(m_resultSet.getString("Publisher"));
			book.setGenres(m_resultSet.getString("Genres"));
			book.setDescription(m_resultSet.getString("Description"));
			book.setQuantity(m_resultSet.getInt("Quantity"));
			book.setOldQuantity(m_resultSet.getInt("Quantity"));
			bookList.add(book);
		}
				
		return bookList;
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
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private Book adjustStatement(Book obj)
	{
		Book book = new Book();
		book.setTitle(obj.getTitle().replace("'", "''"));
		book.setAuthor(obj.getAuthor().replace("'", "''"));
		book.setPublisher(obj.getPublisher().replace("'", "''"));
		book.setDescription(obj.getDescription().replace("'", "''"));
		book.setISBN(obj.getISBN());
		book.setBookID(obj.getBookID());
		book.setPrice(obj.getPrice());
		book.setQuantity(obj.getQuantity());
		book.setOldQuantity(obj.getOldQuantity());
		book.setGenres(obj.getGenres().replace("'", "''"));
		
		return book;
	}
	
	@Override
	public void add(Book obj) throws Exception
	{
		int quantity = 0;
		
		if (obj.getQuantity() > 0)
			quantity = obj.getQuantity();
		else if (obj.getQuantity() == -911)
		{
			remove(obj);
			return;
		}
		
		String[] term = {KEYWORD, obj.getISBN()};
		retrieve(term);
		//ArrayList<Book> list = getResult();
		Book book = adjustStatement(obj);
	
		// if no book is already there
		if (!m_resultSet.next())
		{
			//*********** come back to this for concurrency issues ***********
			/*Book oldBook = list.get(0);
			if (book.getOldQuantity() != oldBook.getQuantity())
			{
				if (book.getOldQuantity() > oldBook.getQuantity())
				{
					
				}
			}*/
			
			m_statement = m_connection.createStatement();
			m_statement.execute("INSERT INTO Books VALUES ('"+book.getISBN()+"', '"+book.getTitle()+"','"+book.getAuthor()+"','"+book.getPublisher()+"',"+book.getPrice()+",'"+book.getGenres()+"','"+book.getDescription()+"',"+quantity+"); COMMIT");
		}
		// if no book is already there
		else
		{
			m_statement = m_connection.createStatement();
			m_statement.execute("UPDATE Books "+
					"SET ISBN='"+book.getISBN()+"', Title='"+book.getTitle()+"', Author='"+book.getAuthor()+"', Publisher='"
					+book.getPublisher()+"', Price="+book.getPrice()+", Genres='"+book.getGenres()+"', Description='"+book.getDescription()+"', Quantity="+quantity+" " +
					"WHERE ISBN='"+book.getISBN()+"'; COMMIT");
		}
	}

	public void remove(Book obj) throws Exception
	{
		m_statement = m_connection.createStatement();
		m_statement.execute("DELETE FROM Books WHERE ISBN='"+obj.getISBN()+"'; COMMIT");
	}
	
}
