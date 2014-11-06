package w3se.Model.Base;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.imageio.ImageIO;

/**
 * 
 * Class  : Receipt.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to hold information of and manage the receipt of the system
 */
public class Receipt
{
	private String m_name;
	private String m_phoneNumber;
	private String m_message;
	private String m_slogan;
	private ArrayList<Book> m_books;
	private NumberFormat m_dollarFormat = NumberFormat.getCurrencyInstance(Locale.US);
	private Graphics2D m_graphics;
	private BufferedImage m_image;
	private int m_cursor = 40;
	private static final int WIDTH = 600;
	private static final int HEIGHT = 840;
	private int m_offset = 0;
	
	/** 
	 * constructor
	 */
	public Receipt()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		
		m_image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		m_image = gc.createCompatibleImage(WIDTH, HEIGHT, Transparency.OPAQUE);
		
		m_graphics = (Graphics2D)m_image.getGraphics();
		
		m_offset = (int)m_graphics.getFontMetrics().getHeight() + 10;
	}
	
	/**
	 * Sets the name of the store that will apprear on a receipt
	 *
	 * @param name The name of the store that will appear on a receipt
	 * @see setStoreName    
	 */
	public void setStoreName(String name)
	{
		m_name = name;
	}
	
	/**
	 * Sets the phone number of the business
	 *
	 * @param number The store phone number
	 * @see setStorePhoneNumber    
	 */
	public void setStorePhoneNumber(String number)
	{
		m_phoneNumber = number;
	}
	
	/**
	 * Sets the message for the customer that is printed on the receipts
	 *
	 * @param message The message to the customer
	 * @see setMessageToRecipient    
	 */
	public void setMessageToRecipient(String message)
	{
		m_message = message;
	}
	
	/**
	 * Sets the desired slogan for the store
	 *
	 * @param slogan The slogan for the store 
	 * @see setSlogan    
	 */
	public void setSlogan(String slogan)
	{
		m_slogan = slogan;
	}
	
	/**
	 * Returns the store/library name
	 *
	 * @return The store/library name
	 * @see getStoreName    
	 */
	public String getStoreName()
	{
		return m_name;
	}
	
	/**
	 * Returns the store/library phone number
	 *
	 * @return The store/library phone number
	 * @see getStorePhoneNumber    
	 */
	public String getStorePhoneNumber()
	{
		return m_phoneNumber;
	}
	
	/**
	 * Returns the personalized message set by the software owner
	 *
	 * @return The personalized message set by the software owner
	 * @see getMessageToRecipient    
	 */
	public String getMessageToRecipient()
	{
		return m_message;
	}
	
	/**
	 * Returns the slogan of the bookstore/library 
	 *
	 * @return The slogan of the bookstore/library
	 * @see getSlogan     
	 */
	public String getSlogan()
	{
		return m_slogan;
	}
	
	/**
	 * Prints a customer transaction receipt
	 *
	 * @param list The list of books that is printed on a receipt 
	 * @see printReceipt    
	 */
	public void printReceipt(ArrayList<Book> list) throws Exception
	{	
		m_books = list;
		
		fillImage(Color.WHITE);
		
		createHeader();
		createBody();
		createFooter();
		
		File output = new File("receipt.png");
	
		try
		{
			ImageIO.write(m_image, "png", output);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		File file = new File("receipt.png");
		
		if (Desktop.isDesktopSupported())
		{
			Desktop.getDesktop().print(file);
		}
		
		m_cursor = 40;
	}
	
	/**
	 * Creates the header of the receipt
	 *
	 * @see createHeader    
	 */
	private void createHeader()
	{
		drawStringCenterAligned(m_name, 0, m_cursor, Color.BLACK);
		m_cursor += m_offset;
		drawStringCenterAligned(m_phoneNumber, 0, m_cursor, Color.BLACK);
		m_cursor += m_offset;
		drawStringCenterAligned(new Date().toString(), 0, m_cursor, Color.BLACK);
		m_cursor += m_offset;
	}
	
	/**
	 * Creates the body of the receipt
	 *
	 * @see createBody    
	 */
	private void createBody()
	{
		m_cursor += m_offset + 30;
		
		float total = 0.0f;
		
		for (int i = 0; i < m_books.size(); i++)
		{	
			m_graphics.drawString(m_books.get(i).getTitle(), 10, m_cursor);
			m_graphics.drawString(m_dollarFormat.format(m_books.get(i).getPrice()), 450, m_cursor);
			total += m_books.get(i).getPrice();
			m_cursor += m_offset;
		}
		
		m_cursor += m_offset + 40;
		m_graphics.drawString("Sub Total : ", 350, m_cursor);
		m_graphics.drawString(m_dollarFormat.format(total), 450, m_cursor);
		m_cursor += m_offset;
		m_graphics.drawString("Tax : ", 350, m_cursor);
		m_graphics.drawString(m_dollarFormat.format(total*(0.05)), 450, m_cursor);
		m_cursor += m_offset;
		total += total*(0.05);
		m_graphics.drawString("Total :", 350, m_cursor);
		m_graphics.drawString(m_dollarFormat.format(total), 450, m_cursor);
		m_cursor += m_offset + 20;
	}
	
	/**
	 * Creates the footer for the receipt
	 *
	 * @see createFooter    
	 */
	private void createFooter()
	{
		drawStringCenterAligned(m_message, 0, m_cursor, Color.BLACK);
		m_cursor += m_offset;
		drawStringCenterAligned(m_slogan, 0, m_cursor, Color.BLACK);
		m_cursor += m_offset;
	}
	
	/**
	 * Draws a line on the receipt
	 *
	 * @param str
	 * @param startX
	 * @param startY
	 * @param color
	 * @see drawStringCenterAligned    
	 */
	private void drawStringCenterAligned(String str, int startX, int startY, Color color)
	{
		m_graphics.setColor(color);
		int stringLen = (int)m_graphics.getFontMetrics().getStringBounds(str, m_graphics).getWidth();  
	    int start = WIDTH/2 - stringLen/2;  // width/2 - "" 
	    m_graphics.drawString(str, start - startX, startY);
	}
	
	/**
	 * 
	 *
	 * @param color
	 * @see fillImage
	 */
	private void fillImage(Color color)
	{
		m_graphics.setColor(color);
		m_graphics.fillRect(0, 0, WIDTH, HEIGHT);
	}
}
