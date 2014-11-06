package w3se.Model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 
 * Class  : FileTokenizer.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to tokenize a file of comma delimited strings
 */
public class FileTokenizer
{
	/**
	 * tokenize and turn to all upper case
	 */
	public static final int UPPER_CASE = 0;
	/**
	 * tokenize and turn to all lower case
	 */
	public static final int LOWER_CASE = 1;
	/**
	 * tokenize but leave the case untouched
	 */
	public static final int UNTOUCHED = 2;
	
	private int m_tokenType = UPPER_CASE;
	private String m_readFile = null;
	
	/**
	 * constructor
	 * @param tokenType - the caseness of the tokens
	 */
	public FileTokenizer(int tokenType)
	{
		m_tokenType = tokenType;
	}
	
	/**
	 * method to read from a file
	 * @param filename
	 */
	public void readFile(String filename)
	{
		try
		{	
			BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(filename));
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			
			int result = 0;
			
			while ((result = inStream.read()) > -1)
			{
				byteStream.write(result);
			}
			
			inStream.close();
			
			m_readFile = byteStream.toString();
			
		} 
		catch (Exception e)
		{
			System.out.println("File not found!");
		} 
	}
	
	/**
	 * method to tokenize a read in file
	 * @pre must run readFile(String filename) first
	 * @return - ArrayList<String> tokenized strings
	 */
	public ArrayList<String> tokenize()
	{
		StringTokenizer tokenizer = new StringTokenizer(m_readFile, ",");
		ArrayList<String> list = new ArrayList<String>();
		
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken().replace(" ", "");
			if (m_tokenType == UPPER_CASE)
				list.add(token.toUpperCase());
			else if (m_tokenType == LOWER_CASE)
				list.add(token.toLowerCase());
			else
				list.add(token);
		}
		
		return list;
	}
	
}
