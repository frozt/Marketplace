package marketplace;

import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JDesktopPane;
import java.awt.Panel;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JPasswordField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TradeClient {

	private JFrame frame;
	private JTextField productTxt;
	private JTextField priceTxt;
	private JTable productList;
	private JTextField nameTxt;
	private static User trader;
	private String username=null;
	private String password=null;
	private JPasswordField passwordField;
	private DefaultTableModel tableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					trader= (User) Naming.lookup("trader");
					System.out.println("Welcome");
					TradeClient window = new TradeClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TradeClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 402, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JPanel LoginPanel = new JPanel();
		
		final JPanel ClientPanel = new JPanel();
		ClientPanel.setVisible(false);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(LoginPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
				.addComponent(ClientPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(LoginPanel, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ClientPanel, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(155, Short.MAX_VALUE))
		);
		
		JLabel lblName = new JLabel("Name:");
		
		JLabel lblPassword = new JLabel("Password:");
		
		nameTxt = new JTextField();
		nameTxt.setColumns(10);
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
				
			public void actionPerformed(ActionEvent e) {
//login button event
				if(passwordField.getText().length()>7)
				{
					if(login(nameTxt.getText(),passwordField.getText()))
					{
						password= passwordField.getText();
						username= nameTxt.getText();
						ClientPanel.setVisible(true);
						LoginPanel.setVisible(false);
					}
					else
						JOptionPane.showMessageDialog(null,"Login failed");
				}
				else
					JOptionPane.showMessageDialog(null,"Password must be 8 char");
			}
		});
		
		JButton btnNewUser = new JButton("New User");
		btnNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//new user button event
				if(passwordField.getText().length()>7)
				{
					if(newUser(nameTxt.getText(),passwordField.getText()))
					{
						username= nameTxt.getText();
						password= passwordField.getText();
						ClientPanel.setVisible(true);
						LoginPanel.setVisible(false);
					}
					else
					{
						JOptionPane.showMessageDialog(null,"User create failed");
					}
				}
				else
					JOptionPane.showMessageDialog(null,"Password must be 8 char");
				
			}
		});
		
		passwordField = new JPasswordField();
		GroupLayout gl_LoginPanel = new GroupLayout(LoginPanel);
		gl_LoginPanel.setHorizontalGroup(
			gl_LoginPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_LoginPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_LoginPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblName)
						.addComponent(lblPassword))
					.addGap(30)
					.addGroup(gl_LoginPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(passwordField)
						.addComponent(nameTxt))
					.addGap(39)
					.addGroup(gl_LoginPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnLogin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewUser, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(92, Short.MAX_VALUE))
		);
		gl_LoginPanel.setVerticalGroup(
			gl_LoginPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_LoginPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_LoginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(nameTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLogin))
					.addGap(9)
					.addGroup(gl_LoginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(btnNewUser)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		LoginPanel.setLayout(gl_LoginPanel);
		
		JLabel lblProduct = new JLabel("Product:");
		
		JLabel lblPrice = new JLabel("Price:");
		
		productTxt = new JTextField();
		productTxt.setColumns(10);
		
		priceTxt = new JTextField();
		priceTxt.setColumns(10);
		
		JButton sellBtn = new JButton("Sell");
		sellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//sell button event
				try {
					if(itemSell(productTxt.getText(),priceTxt.getText()))
					{
						JOptionPane.showMessageDialog(null,"Item listed");
						productTxt.setText("");
						priceTxt.setText("");
					}
					else
						JOptionPane.showMessageDialog(null,"Selling item failed");
				} catch (HeadlessException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JButton btnWish = new JButton("Wish");
		btnWish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//wish button event -- not implemented yet
				itemWish(productTxt.getText(),priceTxt.getText());
			}
		});
		
		JButton ListBtn = new JButton("List");
		ListBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//list button event
				itemList();
			}
		});
		
		JButton buyBtn = new JButton("Buy");
		buyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//buy button event
				if(itemBuy())
					JOptionPane.showMessageDialog(null,"Trade Succesfull");
			}
		});
		
		Object[][] data = new Object[][] { };
		this.tableModel = new  DefaultTableModel(data, new String[] { "Id","Item", "Price" })
		{
			public boolean isCellEditable(int row, int column)
			{
				return false;//This causes all cells to be not editable
			}
		};
		productList = new JTable(this.tableModel);
		
		JButton btnStatistics = new JButton("Statistics");
		btnStatistics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//show statistics button event
				showStatistics();
			}
		});

		GroupLayout gl_ClientPanel = new GroupLayout(ClientPanel);
		gl_ClientPanel.setHorizontalGroup(
			gl_ClientPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ClientPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_ClientPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblProduct)
						.addComponent(lblPrice))
					.addGap(18)
					.addGroup(gl_ClientPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(productList, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_ClientPanel.createSequentialGroup()
							.addGroup(gl_ClientPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_ClientPanel.createSequentialGroup()
									.addGroup(gl_ClientPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_ClientPanel.createSequentialGroup()
											.addComponent(productTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(sellBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(gl_ClientPanel.createSequentialGroup()
											.addComponent(priceTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(btnWish, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)))
									.addPreferredGap(ComponentPlacement.RELATED))
								.addGroup(gl_ClientPanel.createSequentialGroup()
									.addComponent(ListBtn)
									.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
									.addComponent(buyBtn, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
									.addGap(29)))
							.addComponent(btnStatistics)))
					.addContainerGap(68, Short.MAX_VALUE))
		);
		gl_ClientPanel.setVerticalGroup(
			gl_ClientPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ClientPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_ClientPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProduct)
						.addComponent(productTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(sellBtn))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_ClientPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPrice)
						.addComponent(priceTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnWish))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_ClientPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(ListBtn)
						.addComponent(btnStatistics)
						.addComponent(buyBtn))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(productList, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(22, Short.MAX_VALUE))
		);
		ClientPanel.setLayout(gl_ClientPanel);
		frame.getContentPane().setLayout(groupLayout);
	}
	//for new users
	private boolean newUser(String name, String password)
	{
		try {
			if(trader.newUser(name, password))
				return true;
			else
				return false;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	//to login the user
	private boolean login(String name, String password)
	{
		try {
			if(trader.login(name, password))
				return true;
			else
				return false;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	//item sell function
	private boolean itemSell(String itemName, String price) throws RemoteException
	{
		Product prod = new Product();
		prod.setName(itemName);
		prod.setPrice(Integer.parseInt(price));
		prod.setOwner(username);
		System.out.println(prod.ToString());
		if(trader.sell(prod.ToString()))
			return true;
		else
			return false;
	}
	//listing all available items in the market
	private void itemList()
	{
		//remove all previous data
		DefaultTableModel dm = (DefaultTableModel)productList.getModel();
		while(dm.getRowCount() > 0)
		{
		    dm.removeRow(0);
		}
		
		ArrayList products = new ArrayList();
		try {
			products = trader.list(username);
			String [] columnNames = {"Id","Item","Price"};
			Object[][] data = new Object[products.size()][3];
			Product prod=new Product();
			//fill table with new data
			for(int i=0; i<products.size();i++)
			{
				prod.toObject(products.get(i).toString(), true);
				Object[] row = new Object[3];
				row[0] = prod.getId();
				row[1] = prod.getName();
				row[2] = prod.getPrice();
				this.tableModel.addRow(row);
				
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//function for item buy
	private boolean itemBuy()
	{
		int id= Integer.parseInt(productList.getValueAt(productList.getSelectedRow(), 0).toString());
		try {
			int result=trader.buy(id,username);
			//trade is successful
			if(result==1)
			{
				itemList();
				return true;
			}
			//if return is 2 balance is not enough to buy
			else if(result == 2)
			{
				JOptionPane.showMessageDialog(null,"You don't have enough money");
				return false;
			}
			//if return is 3 it means there were an exception
			else
				return false;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	//not implemented yet
	private void itemWish(String product, String price)
	{
		
	}
	
	// statistics of the current user
	private void showStatistics()
	{
		String result="";
		try {
			result = trader.statistics(username);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null,result);
	}
}
