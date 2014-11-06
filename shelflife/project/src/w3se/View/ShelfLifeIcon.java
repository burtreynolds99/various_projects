package w3se.View;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import w3se.Model.IMS;

/**
 * 
 * Class  : ShelfLifeIcon.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to encapsulate the ShelfLife icon for panels
 */
public class ShelfLifeIcon implements Icon
{
	private static final int HEIGHT = 64;
	private static final int WIDTH = 167;
	private BufferedImage m_img = null;
	
	/**
	 * constructor
	 */
	public ShelfLifeIcon()
	{
		try
		{
			m_img = ImageIO.read(new File(IMS.getInstance().RESOURCES_D+File.separator+IMS.getInstance().SL_ICON));
		} 
		catch (IOException e)									// error
		{
			System.out.println("Error opening image");
		}
	}
	
	/**
	 * method to get the icon's height
	 * @return - height
	 */
	public int getIconHeight()
	{
		return HEIGHT;
	}

	/**
	 * method to get the icon's width
	 */
	public int getIconWidth()
	{
		return WIDTH;
	}

	
	/**
	 * method to paint the icon
	 */
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.drawImage(m_img, null, y, y);
	}

}
