package w3se.Model.Base;

import java.awt.Color;

/**
 * 
 * Class  : Theme.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to manage the theme of the application
 */
public class Theme
{
	private Color m_main;
	private Color m_secondary;
	
	/**
	 * 
	 *
	 * @param main 			The main theme color of the window
	 * @param secondary		The secondary theme color of the window
	 * @see Theme    
	 */
	public Theme (Color main, Color secondary)
	{
		m_main = main;
		m_secondary = secondary;
	}
	
	/**
	 * Returns the main theme color 
	 *
	 * @return The main theme color
	 * @see getMainColor    
	 */
	public Color getMainColor()
	{
		return m_main;
	}
	
	/**
	 * Returns the secondary theme color
	 *
	 * @return The secondary theme color
	 * @see getSecondaryColor    
	 */
	public Color getSecondaryColor()
	{
		return m_secondary;
	}
}
