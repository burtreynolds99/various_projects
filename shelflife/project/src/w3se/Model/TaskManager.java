package w3se.Model;

import java.util.Observable;
import java.util.PriorityQueue;
import java.util.Queue;

import w3se.Model.Base.User;

/**
 * 
 * Class  : TaskManager.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Class to execute and manage tasks
 */
public class TaskManager extends Observable
{
	private Queue<Task> m_taskQueue = new PriorityQueue<Task>();
	
	/**
	 * default constructor
	 */
	public TaskManager()
	{
	}
	
	/**
	 * method to add a task to the manager
	 * @param task
	 */
	public void addTask(Task task)
	{
		m_taskQueue.offer(task);
		setChanged();
		notifyObservers("task_added");
	}
	
	/**
	 * method to run the next task
	 */
	public void runTask() throws Exception
	{
		User currentUser = IMS.getInstance().getCurrentUser();
		int privilege = currentUser.getPrivilege();
		Task task = m_taskQueue.poll();
		
		if (task.getPrivilege() <= privilege)
		{
			try
			{
				task.run();
			}
			catch (Exception e)
			{
				throw e;
			}
		}
		else
			throw new Exception("Privileges do not allow this action.\n Changes will not be applied!");
		
		
	}
	
	/**
	 * method to test if the manager has any tasks
	 * @return
	 */
	public boolean hasTask()
	{
		if (m_taskQueue.peek() != null)
			return true;
		else
			return false;
	}
	
	
}
