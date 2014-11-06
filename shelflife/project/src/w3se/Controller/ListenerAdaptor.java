package w3se.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * Class  : ListenerAdaptor.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Nov 26, 2012
 * Desc   : Adaptor class to bind ActionListener, ItemListener, ChangeListener, WindowListener, and MouseListener to a single concrete class
 */
public class ListenerAdaptor implements ActionListener, ItemListener, ChangeListener, WindowListener, MouseListener
{

	public void stateChanged(ChangeEvent arg0)
	{
	}

	public void itemStateChanged(ItemEvent arg0)
	{	
	}

	public void actionPerformed(ActionEvent e)
	{
	}

	public void windowActivated(WindowEvent arg0)
	{
	}

	public void windowClosed(WindowEvent arg0)
	{
	}

	public void windowClosing(WindowEvent arg0)
	{
	}

	public void windowDeactivated(WindowEvent arg0)
	{
	}

	public void windowDeiconified(WindowEvent arg0)
	{
	}

	public void windowIconified(WindowEvent arg0)
	{
	}
	
	public void windowOpened(WindowEvent arg0)
	{
	}

	public void mouseClicked(MouseEvent arg0)
	{
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mousePressed(MouseEvent arg0)
	{
	}

	public void mouseReleased(MouseEvent arg0)
	{
	}

}
