package w3se.Model.Base;

import w3se.Model.Exportable;

/**
 * 
 * Class  : Book.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to hold book information
 */
public class Book implements Exportable
{
	private int m_id = 0;
	private String m_title = "";
	private String m_ISBN = "";
	private double m_price = 0.0f;
	private int m_oldQuantity = 0;
	private String m_author = "";
	private String m_publisher = "";
	private String m_genres = "";
	private String m_description = "";
	private int m_quantity = 0;
	private int m_totalCopies = 0;
	
	/**
	 * default constructor
	 */
	public Book()
	{
		
	}
	
	/**
	 * A book constructor
	 *
	 * @param title			A title for a specific book
	 * @param ISBN			A ISBN for a specific book
	 * @param price			A price for a specific book
	 * @param author		A author for a specific book
	 * @param publisher		A publisher for a specific book
	 * @param genres		A genres for a specific book		
	 * @param description 	A description for a specific book
	 * @see Book    
	 */
	public Book(String title, String ISBN, double price, String author, String publisher, String genres, String description)
	{
		m_title = title;
		m_ISBN = ISBN;
		m_price = price;
		m_author = author;
		m_publisher = publisher;
		m_genres = genres;
		m_description = description;
	}
	
	/**
	 * Returns the Book ID for a specific book
	 *
	 * @return The Book ID for a specific book
	 * @see getBookID    
	 */
	public int getBookID()
	{
		return m_id;
	}
	
	/**
	 * Sets the Book ID for a specific book
	 *
	 * @param id The Book ID for a specific book
	 * @see setBookID    
	 */
	public void setBookID(int id)
	{
		m_id = id;
	}
	
	/**
	 * Returns the title for a specific book
	 *
	 * @return The title for a specific book
	 * @see getTitle    
	 */
	public String getTitle()
	{
		return m_title;
	}

	/**
	 * Sets the title for a specific book
	 *
	 * @param title The title for a specific book
	 * @see setTitle    
	 */
	public void setTitle(String title)
	{
		m_title = title;
	}
	
	/**
	 * Returns the ISBN for a specific book
	 *
	 * @return The ISBN for a specific book
	 * @see getISBN    
	 */
	public String getISBN()
	{
		return m_ISBN;
	}

	/**
	 * Sets the ISBN for a specific book
	 *
	 * @param ISBN The ISBN for a specific book
	 * @see setISBN    
	 */
	public void setISBN(String ISBN)
	{
		m_ISBN = ISBN;
	}

	/**
	 * Returns the price for a specific book
	 *
	 * @return The price for a specific book
	 * @see getPrice    
	 */
	public double getPrice()
	{
		return m_price;
	}

	/**
	 * Sets the price for a specific book
	 *
	 * @param price The price for a specific book
	 * @see setPrice    
	 */
	public void setPrice(double price)
	{
		m_price = price;
	}

	/**
	 * Returns the author for a specific book
	 *
	 * @return The author for a specific book
	 * @see getAuthor    
	 */
	public String getAuthor()
	{
		return m_author;
	}

	/**
	 * Sets the author for a specific book
	 *
	 * @param author The author for a specific book
	 * @see setAuthor    
	 */
	public void setAuthor(String author)
	{
		m_author = author;
	}

	/**
	 * Returns the publisher for a specific book
	 *
	 * @return The publisher for a specific book
	 * @see getPublisher    
	 */
	public String getPublisher()
	{
		return m_publisher;
	}

	/**
	 * Sets the publisher for a specific book
	 *
	 * @param publisher The publisher for a specific book
	 * @see setPublisher    
	 */
	public void setPublisher(String publisher)
	{
		m_publisher = publisher;
	}

	/**
	 * Returns a list of genres for a specific book
	 *
	 * @return A list of genres for a specific book
	 * @see getGenres    
	 */
	public String getGenres()
	{
		return m_genres;
	}

	/**
	 * Sets a list of genres for a specific book
	 *
	 * @param genres A list of genres for a specific book
	 * @see setGenres    
	 */
	public void setGenres(String genres)
	{
		m_genres = genres;
	}

	/**
	 * Returns the description for a specific book
	 *
	 * @return The description for a specific book
	 * @see getDescription    
	 */
	public String getDescription()
	{
		return m_description;
	}

	/**
	 * Sets the description for a specific book
	 *
	 * @param description The description for a specific book
	 * @see setDescription    
	 */
	public void setDescription(String description)
	{
		m_description = description;
	}
	
	/**
	 * Returns the old quantity of a specific book
	 *
	 * @return The old quantity of a specific book
	 * @see getOldQuantity    
	 */
	public int getOldQuantity()
	{
		return m_oldQuantity;
	}
	
	/**
	 * Sets the old quantity of a specific book before an update
	 *
	 * @param quantity The quantity of a specific book before an update happens
	 * @see setOldQuantity    
	 */
	public void setOldQuantity(int quantity)
	{
		m_oldQuantity = quantity;
	}
	
	/**
	 * Returns the quantity of a specific book
	 *
	 * @return The quantity of a specific book
	 * @see getQuantity    
	 */
	public int getQuantity()
	{
		return m_quantity;
	}
	
	/**
	 * Sets the quantity of a specific book
	 *
	 * @param quantity The quantity of a specific book
	 * @see setQuantity    
	 */
	public void setQuantity(int quantity)
	{
		m_quantity = quantity;
	}
	
	/**
	 * Returns the total number of copies of a book
	 *
	 * @return The total number of copies of a book
	 * @see getTotalCopies    
	 */
	public int getTotalCopies()
	{
		return m_totalCopies;
	}
	
	/**
	 * Sets the total number of copies of a book
	 *
	 * @param total The total number of copies of a book
	 * @see setTotalCopies    
	 */
	public void setTotalCopies(int total)
	{
		m_totalCopies = total;
	}

	/**
	 * Returns an exportable title for a book database 
	 *
	 * @return An exportable title for a book database 
	 * @see getExportableTitle    
	 */
	public String getExportableTitle()
	{
		return "Book Database Export";
	}
	
	/**
	 * Returns a formatted, exportable header row for a database
	 *
	 * @return A formatted, exportable header row for a database
	 * @see getExportableHeadRow    
	 */
	public String[] getExportableHeadRow()
	{
		String[] format = new String[8];
		format[0] = "ISBN";
		format[1] = "Title";
		format[2] = "Author";
		format[3] = "Publisher";
		format[4] = "Genres";
		format[5] = "Description";
		format[6] = "Quantity";
		format[7] = "Price";
		
		return format;
	}
	
	/**
	 * Returns a formatted, exportable row for a database
	 * 
	 * @return A formatted, exportable row for a database
	 * @see getExportableRow     
	 */
	public String[] getExportableRow()
	{
		String[] format = new String[8];
		format[0] = m_ISBN;
		format[1] = m_title;
		format[2] = m_author;
		format[3] = m_publisher;
		format[4] = m_genres;
		format[5] = m_description;
		format[6] = ""+m_quantity;
		format[7] = ""+m_price;
		
		return format;
	}
}
