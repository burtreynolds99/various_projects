package w3se.Model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 
 * Class  : Configurations.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to hold and manage the configurations of the system
 */
public class Configurations
{
	private LinkedHashMap<String, String> m_docMap = new LinkedHashMap<String, String>();
	private static String m_filename;
	
	private Configurations()
	{}
	
	/**
	 * method to obtain the configurations from a file
	 * @param filename - filename of the configuration file
	 * @return - Configurations instance
	 */
	public static Configurations getConfigFromFile(String filename)
	{
		m_filename = filename;
		String readFile;
		try
		{	
			BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(m_filename));
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			
			int result = 0;
			
			while ((result = inStream.read()) > -1)
			{
				byteStream.write(result);
			}
			
			inStream.close();
			
			readFile = byteStream.toString();
			Configurations config = new Configurations();
			config.parseConfigFile(readFile);
			
			return config;	
		} 
		catch (Exception e)
		{
			System.out.println("File not found!");
		} 
		
		return null;
	}
	
	/**
	 * method to set a new configuration to the Configurations object or edit an old configuration
	 * @param key - the name to look up
	 * @param value - the value to be associated with the key
	 */
	public void setNewConfiguration(String key, String value)
	{
		m_docMap.put(key, value);
	}
	
	/**
	 * method to parse the config file
	 * @param readFile
	 */
	private void parseConfigFile(String readFile)
	{
		try
		{
			InputSource inSource = new InputSource(new StringReader(readFile));
			Document doc = null;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inSource);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("Config");
			
			for (int i = 0; i < nList.getLength(); i++)
			{	
				Element e1 = (Element)nList.item(i);
				
				if (e1 != null)
				{
					String name = getAttribute("Name", e1);
					String value = getAttribute("Value", e1);
					m_docMap.put(name, value);
				}
			}
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * method to set the filename to write to 
	 * @param filename - String
	 */
	public void setFilename(String filename)
	{
		m_filename = filename;
	}
	
	/**
	 * method to get the filename of the Configuration's object
	 * @return - String filename
	 */
	public String getFilename()
	{
		return m_filename;
	}
	
	/**
	 * method to write the contents of the Configurations object to the configurations file specified in the getConfigFromFile method or set filename method
	 */
	public void writeToFile()
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
			Element root = doc.createElement("IMS");
			doc.appendChild(root);
			
			Iterator<Entry<String, String>> iter = m_docMap.entrySet().iterator();
			
			while (iter.hasNext())
			{
				Map.Entry<String,String> pair = iter.next();
				Element e = doc.createElement("Config");
				e.setAttribute("Name", pair.getKey());
				e.setAttribute("Value", pair.getValue());
				root.appendChild(e);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(m_filename));
			
			transformer.transform(source, result);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * method to get a value from a key/name
	 * @param name - key associated with the value
	 * @return - String value
	 */
	public String getValue(String name)
	{
		return m_docMap.get(name);
	}
	
	/**
	 * 
	 * @param attribute
	 * @param element
	 * @return
	 */
	private String getAttribute(String attribute, Element element)
	{
		return element.getAttribute(attribute);
	}
	
	/**
	 * 
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
}
