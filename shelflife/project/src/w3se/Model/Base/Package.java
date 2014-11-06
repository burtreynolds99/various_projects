package w3se.Model.Base;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.JOptionPane;

/**
 * 
 * Class  : Package.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to manage package file (encrypted zip file)
 */
public class Package
{
	/**
	 * Stream buffer size
	 */
	public static final int BUFFER_SIZE = 1024;
	private String m_dest;
	private String m_src;
	private String m_key;
	
	/**
	 * constructor
	 * @param src - Source filename
	 * @param dest - Destination filename
	 * @param key - Encryption key
	 * @see Package    
	 */
	public Package(String src, String dest, String key)
	{
		m_dest = dest;
		m_src = src;
		m_key = key;
		File temp = new File(m_dest);
		temp.mkdir();
	}
	
	/**
	 * method to set the key to decrypt the package
	 * @param key The key to be set 
	 * @see setKey    
	 */
	public void setKey(String key)
	{
		m_key = key;
	}
	
	/**
	 * method to unpack the contents of the package
	 * @see unpack    
	 */
	public void unpack()
	{
		try
		{
			// open a stream to the source file
			FileInputStream in = new FileInputStream(m_src);
			// open a stream to the output zip file
			FileOutputStream out = new FileOutputStream("tmp.zip");
			// decrypt the package
			encryptOrDecrypt(m_key, Cipher.DECRYPT_MODE, in, out);
			// now open a stream to read in the newly created temporary zip file
			FileInputStream fis = new FileInputStream("tmp.zip");
			// let zip input stream do the heavy lifting
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			
			// loop until all the contents of the folder are retrieved
			while ((entry = zis.getNextEntry()) != null)
			{
				// small bug with using this on Mac, so this is a simple workaround
				if (entry.getName().contains(".DS_Store") == false)
				{
					
					int size;
					byte[] buff = new byte[BUFFER_SIZE];
					// create a stream to output each file in the package
					FileOutputStream fos = new FileOutputStream(entry.getName());
					BufferedOutputStream bos = new BufferedOutputStream(fos, buff.length);
					
					// read each file from the package
					while ((size = zis.read(buff, 0, buff.length)) != -1)
					{
						// now write it to the buffered stream
						bos.write(buff, 0, size);
					}
					
					// flush the stream to write it out and close the stream
					bos.flush();
					bos.close();
				}
			}
			
			// now close all other streams
			zis.close();
			fis.close();
			
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "System Resources not found!", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * method to pack the contents of the temporary folder 
	 * @see pack   
	 */
	public void pack()
	{
		try
		{
			// create a stream to write out the package
			FileOutputStream out = new FileOutputStream(m_src);
			// create a stream to write out the temporary zip file
			ZipOutputStream zip = null;
			FileOutputStream fos = null;
				
			fos = new FileOutputStream("tmp.zip");
			zip = new ZipOutputStream(fos);
			
			// zip the folder
			zipFolder("", m_dest, zip);
			zip.flush();
			zip.close();
			
			// create a stream to read in the newly created zip folder
			FileInputStream in = new FileInputStream("tmp.zip");
			// encrypt the file 
			encryptOrDecrypt(m_key, Cipher.ENCRYPT_MODE, in, out);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "System Resources could not be packed!", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * method to be called on system close
	 * @see close    
	 */
	public void close()
	{
		deleteDir(new File("tmp"));
		deleteDir(new File("tmp.zip"));
	}

	/**
	 * Encrypt or decrypt
	 *
	 * @param key
	 * @param mode
	 * @param is
	 * @param os
	 * @see encryptOrDecrypt    
	 */
	public void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os)
	{

		try
		{
			// set up the environment to encrypt in DES
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
			SecretKey desKey = skf.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
	
			
			if (mode == Cipher.ENCRYPT_MODE) {
				cipher.init(Cipher.ENCRYPT_MODE, desKey);
				CipherInputStream cis = new CipherInputStream(is, cipher);
				byte[] bytes = new byte[64];
				int numBytes;
				while ((numBytes = is.read(bytes)) != -1) {
					os.write(bytes, 0, numBytes);
				}
				os.flush();
				os.close();
				is.close();
				
			} 
			else if (mode == Cipher.DECRYPT_MODE)
			{
				cipher.init(Cipher.DECRYPT_MODE, desKey);
				CipherOutputStream cos = new CipherOutputStream(os, cipher);
				byte[] bytes = new byte[64];
				int numBytes;
				while ((numBytes = is.read(bytes)) != -1) {
					os.write(bytes, 0, numBytes);
				}
				os.flush();
				os.close();
				is.close();
			}
		}
		catch (Exception e)
		{
			
		}
	}
	
	/**
	 * Deletes a directory
	 *
	 * @param directory The directory to be deleted
	 * @see deleteDir    
	 */
	private void deleteDir(File directory)
	{
		if (!directory.exists())
		{
	           System.out.println("Directory does not exist.");
	           System.exit(0);
	    }
		else
	    {
	           try
	           {
	               delete(directory);
	 
	           }
	           catch(IOException e)
	           {
	               System.exit(0);
	           }
	        }
	}
	
	/**
	 * Deletes a file
	 *
	 * @param file The file to be deleted
	 * @see delete    
	 */
	private void delete(File file) throws IOException
	{

		if(file.isDirectory()){

			//directory is empty, then delete it
			if(file.list().length==0)
			{
				file.delete();

			}
			else
			{
				//list all the directory contents
				String files[] = file.list();

				for (String temp : files) 
				{
					//construct the file structure
					File fileDelete = new File(file, temp);

					//recursive delete
					delete(fileDelete);
				}

				//check the directory again, if empty then delete it
				if (file.list().length==0)
				{
					file.delete();
				}
			}

		}
		else
		{
			//if file, then delete it
			file.delete();
		}
	}
	
	/**
	 * 
	 *
	 * @param path
	 * @param srcFolder
	 * @param zstream
	 * @see zipFolder    
	 */
	private void zipFolder(String path, String srcFolder, ZipOutputStream zstream) throws Exception
	{
		File folder = new File(srcFolder);
		for (int i = 0; i < folder.list().length; i++)
		{
			String filename = folder.list()[i];
			if (path.equals(""))
			{
				addFileToZip(folder.getName(), srcFolder + File.separator + filename, zstream);
			}
			else
			{
				addFileToZip(path+"/"+folder.getName(), srcFolder+File.separator+filename, zstream);
			}
		}
	}

	/**
	 * Adds a file to the zip file. 
	 *
	 * @param path
	 * @param src
	 * @param zstream
	 * @see addFileToZip    
	 */
	private void addFileToZip(String path, String src, ZipOutputStream zstream) throws Exception
	{ 
		File folder = new File(src);
		if (folder.isDirectory())
		{
			zipFolder(path, src, zstream);
		}
		else
		{
			byte[] buff = new byte[BUFFER_SIZE];
			int len;
			FileInputStream in = new FileInputStream(src);
			zstream.putNextEntry(new ZipEntry(path+File.separator+folder.getName()));
			while ((len = in.read(buff)) > 0)
			{
				zstream.write(buff, 0, len);
			}
			
		}
	}
	
}
