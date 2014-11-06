package w3se.Model;

import w3se.Model.Base.User;


/**
 * 
 * Class  : Task.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to carry a task to be executed
 */
public class Task implements Comparable<Task>
{
	private int m_privilege = User.GENERAL;
	private Runnable m_task = null;
	
	/**
	 * constructor
	 * @param privilege - privilege level of the task
	 * @param task - executable block of code
	 */
	public Task(int privilege, Runnable task)
	{
		m_privilege = privilege;
		m_task = task;
	}
	
	/**
	 * method to set the privilege of the task
	 * @param privilege
	 */
	public void setPrivilege(int privilege)
	{
		m_privilege = privilege;
	}
	
	/**
	 * method to get the privilege from the task
	 * @return
	 */
	public int getPrivilege()
	{
		return m_privilege;
	}
	
	/**
	 * method to run the task
	 */
	public void run()
	{
		m_task.run();
	}

	@Override
	public int compareTo(Task t)
	{
		if (m_privilege > t.getPrivilege())
			return 1;
		else if (m_privilege < t.getPrivilege())
			return -1;
		
		return 0;
	}
	
	
}
