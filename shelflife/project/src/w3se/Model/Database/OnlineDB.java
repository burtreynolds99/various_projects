package w3se.Model.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import w3se.Model.Base.Book;

/**
 * 
 * Class  : OnlineDB.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to manage access to isbn.com
 */
@SuppressWarnings("rawtypes")
public class OnlineDB implements Database
{
	private HttpURLConnection connection = null;
	private BufferedReader buffReader = null;
	private StringBuilder strBuilder = null;
	private String line = null;
	private String userIsbn = null;
	private Scanner scanner = new Scanner(System.in);
	private URL serverAddress = null;
	
	/**
	 * default constructor
	 */
	public OnlineDB()
	{
		
	}
	
	/**
	 * method to create a full formed url with API key and search term inserted
	 * @param isbn - search term
	 * @return - full formed url
	 */
	private String createFullformedURL(String isbn)
	{
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("http://isbndb.com/api/books.xml?access_key=J6MLSYDG&index1=isbn&value1=");
		strBuilder.append(isbn);
		
		return strBuilder.toString();
	}
	
	/**
	 * method to parse the XML response from isbndb
	 * @param response - XML response
	 * @return - ArrayList of books
	 */
	private ArrayList<Book> parseResponse(String response)
	{
		ArrayList<Book> bookList = new ArrayList<Book>();
		Book book = null;
		
		try
		{
			InputSource inSource = new InputSource(new StringReader(response));
			Document doc = null;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inSource);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("BookData");
			
			for (int i = 0; i < nList.getLength(); i++)
			{	
				Element e1 = (Element)nList.item(i);
				
				if (e1 != null)
				{
					book = new Book();
					String isbn;
					
					if ((isbn = getAttribute("isbn13", e1)) == null)		// if the book has isbn 13
						isbn = getAttribute("isbn", e1);					// if the book has isbn 10
					
					book.setISBN(isbn);
					book.setTitle(getTagValue("Title", e1));
					book.setAuthor(getTagValue("AuthorsText", e1));
					book.setPublisher(getTagValue("PublisherText", e1));
					
					bookList.add(book);
				}
			}
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return bookList;
	}
	
	/**
	 * method to get the attribute of an XML element
	 * @param attribute
	 * @param element
	 * @return
	 */
	private String getAttribute(String attribute, Element element)
	{
		return element.getAttribute(attribute);
	}
	
	/**
	 * method to get the tag value from a tag
	 * @param tag
	 * @param element
	 * @return
	 */
	private String getTagValue(String tag, Element element)
	{
		NodeList nList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nList.item(0);
		
		if (node != null)
			return node.getNodeValue();
		else
			return "";
	}
	
	
	public void retrieve(Object term) throws Exception
	{
		String searchTerm = (String)term;
		try
		{
			userIsbn = searchTerm;
			serverAddress = new URL(createFullformedURL(userIsbn));
			connection = (HttpURLConnection)serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			connection.connect();
			
			buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

	public void add(Object obj) throws SQLException
	{}

	public Object getResult() throws Exception
	{
		StringBuilder str = new StringBuilder();
		String temp = "";
		while ((temp = buffReader.readLine()) != null)
		{
			str.append(temp);
		}
		
		return parseResponse(str.toString());
	}

	public void shutdown() throws SQLException
	{
		connection.disconnect();
	}

	public void close() throws SQLException
	{}

	public void remove(Object obj) throws Exception
	{}

}
