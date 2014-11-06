package w3se.Model.Database;

/**
 * 
 * Class  : Database.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Interface for databases
 * @param <T> Type that the database works with (e.g. UsersDB uses User)
 * @param <R> Results type (e.g. UsersDB uses ArrayList<User>)
 */
public interface Database<T, R>
{
	/**
	 * method to retrieve an object from the database
	 * @param obj - search term to look for
	 * @throws Exception
	 */
	public void retrieve(Object obj) throws Exception;
	
	/**
	 * method to add an object to the database
	 * @param obj - 
	 * @throws Exception
	 */
	public void add(T obj) throws Exception;
	
	/**
	 * method to remove an object from the database
	 * @param obj
	 * @throws Exception
	 */
	public void remove(T obj) throws Exception;
	
	/**
	 * method to get the results of retrieve(Object obj)
	 * @return
	 * @throws Exception
	 */
	public R getResult() throws Exception;

	/**
	 * method to shutdown the database
	 * @throws Exception
	 */
	public void shutdown() throws Exception;
	
	/**
	 * method to close the connection to the database
	 * @throws Exception
	 */
	public void close() throws Exception;

}
