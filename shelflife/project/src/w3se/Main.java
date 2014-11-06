package w3se;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import w3se.Model.IMS;
import w3se.View.MainView;

/**
 * 
 * Class  : Main.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Nov 26, 2012
 * Desc   : Main entry point of the software
 */
public class Main
{
	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args)
	{	
		// the only way I could find to make the software a single instance per computer
		try
		{
			// try to create a socket on the localhost
			new ServerSocket(9999, 0, InetAddress.getByAddress(new byte[] {127,0,0,1}));
		}
		catch (BindException e)
		{
			// the software is already running
			System.out.println("Instance already running.");
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("IO error");
			System.exit(-1);
		}
		
		// uncomment to allow the resources.slr to be unpacked and repacked for manual fixes
		
		/*if (args.length > 0)
		{
			if (args[0].equals("Fix"))
				IMS.getInstance(true, Integer.parseInt(args[1]));
		}*/
		
		// get the IMS initialized before the mainview starts
		IMS.getInstance();
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainView frame = new MainView();
					frame.setVisible(true);
				} 
				catch (Exception e)
				{
					System.out.print(e.getMessage());
				}
			}
		});
	}
}
