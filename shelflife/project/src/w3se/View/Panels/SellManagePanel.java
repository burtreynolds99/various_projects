package w3se.View.Panels;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import w3se.Controller.Controller;
import w3se.Model.IMS;
import w3se.Model.Base.Book;
import w3se.View.Subpanels.ReceiptInfoPanel;
import w3se.View.Subpanels.SearchPanel;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * 
 * Class  : SellManagePanel.java
 * Author : Larry "Bucky" Kittinger
 * Date   : Dec 1, 2012
 * Desc   : Panel for managing the sell of books
 */
@SuppressWarnings("serial")
public class SellManagePanel extends JPanel implements Observer
{
	private Controller m_controller;
	private SearchPanel m_mainPanel;
	private ReceiptInfoPanel m_infoPanel;
	
	/**
	 * Create the panel.
	 */
	public SellManagePanel(Controller controller)
	{
		m_controller = controller;
		m_controller.registerView(this);
		
		setBounds(0, 0, 940, 500);
		setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		add(splitPane, BorderLayout.CENTER);
		
		m_mainPanel = new SearchPanel(SearchPanel.KEYWORD_HEADER, controller);
		m_infoPanel = new ReceiptInfoPanel(controller);
		
		splitPane.setLeftComponent(m_mainPanel);
		splitPane.setRightComponent(m_infoPanel);
		splitPane.setEnabled(false);
		IMS.getInstance().addView(this);
	}
	
	/**
	 * method to retrieve the search term from the form
	 * @param searchTerm
	 */
	public void setSearchTerm(String searchTerm)
	{
		
	}
	
	/**
	 * method to retrieve the search term from the form
	 * @param searchTerm
	 * return 
	 */
	public String getSearchTerm()
	{
		return m_mainPanel.getSearchText();
	}

	/**
	 * method to clear the receipt
	 */
	public void clearReceipt()
	{
		m_infoPanel.clearReceipt();
	}
	
	/**
	 * method to toggle the sell button 
	 * @param isEnabled
	 */
	public void toggleSellButton(boolean isEnabled)
	{
		m_infoPanel.toggleSellButton(isEnabled);
	}
	
	public void update(Observable sender, Object obj)
	{
		ArrayList<Book> list = IMS.getInstance().getListOfFoundBooks();
		ArrayList<Book> soldList = IMS.getInstance().getListofSoldBooks();
		
		m_mainPanel.clearSearchText();
		m_mainPanel.clearLists();
		m_mainPanel.setSearchResults(list);
		m_mainPanel.updateColor();
		
		m_infoPanel.clearReceipt();
		m_infoPanel.setReceiptList(soldList);
		m_infoPanel.updateColor();
		
		double total = 0.0;
		for (int i = 0; i < soldList.size(); i++)
		{
			total += soldList.get(i).getPrice();
		}
		m_infoPanel.setTotalPrice(total);
	}
}