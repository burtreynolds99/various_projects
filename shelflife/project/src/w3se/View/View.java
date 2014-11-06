package w3se.View;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * Class  : View.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : 
 */
abstract public class View implements Observer
{

	@Override
	abstract public void update(Observable o, Object arg);
	
}
