package w3se.View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

/**
 * 
 * Class  : SplashScreen.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to encapsulate the splash screen
 */
@SuppressWarnings("serial")
public class SplashScreen extends JFrame
{
	public static final int HEIGHT = 450;
	public static final int WIDTH = 450;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @throws InterruptedException 
	 */
	public SplashScreen(final String filename) 
	{
		init(filename);
	}
	
	/**
	 * method to display the splash screen for 2 seconds
	 */
	public void run()
	{
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis()-startTime < 2000)
		{}
		dispose();
	}
	
	/**
	 * method to initialize the splash screen
	 * @param filename
	 */
	private void init(String filename)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setBounds(100, 100, 450, 450);
		// make it appear at the center of the screen
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(d.width/2-getSize().width/2, d.height/2-getSize().height/2);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		BufferedImage img = null;
		
		try
		{
			img = ImageIO.read(new File(filename));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		JLabel lblLogo = new JLabel();
		lblLogo.setIcon(new ImageIcon(img));
		
		contentPane.add(lblLogo, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
