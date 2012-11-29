package marketplace;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


import bankrmi.Account;
import bankrmi.AccountImpl;
import bankrmi.Bank;

public class UserImpl extends UnicastRemoteObject implements marketplace.User{
	private Bank bank;
	private final int defaultMoney=1000; 
	
	public UserImpl() throws RemoteException {
		super();

		try {
			bank = (Bank) Naming.lookup("NordBanken");
		} catch (Exception se) {
			System.out.println("The runtime failed: " + se);
			System.exit(0);
		}

	}
//when new user want to open an account this function is called
	public synchronized boolean newUser(String name, String password) throws RemoteException {
		try {
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///tradeSystem", "root","1234");
			Statement stmt = con.createStatement();

			try {
				stmt.executeUpdate("INSERT INTO user(name,password) VALUES ('" + name + "', '"+ password + "')");
			} catch (Exception E) {
				return false;
			}
			//new bank account is also created for a new user
			Account acc = new AccountImpl();
			acc = bank.newAccount(name);
			acc.deposit(defaultMoney); // default money is deposited to new users account

			return true;

		} catch (Exception E) {
			E.printStackTrace();
			return false; 
		}
	}
	
// when a user tries to login this function is called
	public synchronized boolean login(String name, String password) throws RemoteException {

		try {
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///tradeSystem", "root","1234");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT name,balance FROM user WHERE name='"+ name + "' and password='"+password+"'" );
			if (rs.next() == true)
			{
				Account acc = new AccountImpl();
				// if user is not new created user it may not have a bank account for this session
				if(bank.getAccount(name)==null)
					acc = bank.newAccount(name);
				//load latest balance to bank object
				acc.deposit(Integer.parseInt(rs.getString("balance")));
				return true;
			}
			else
				return false;

		} catch (Exception E) {
			E.printStackTrace();
			return false;
		}
	}

// when user try to sell an item this function is called
	public synchronized boolean sell(String product) throws RemoteException {
		Product prod = new Product();
		prod.toObject(product);
		try {
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///tradeSystem", "root","1234");
			Statement stmt = con.createStatement();

			stmt.executeUpdate("INSERT INTO product(seller,product,price,isSold) VALUES ('"+ prod.getOwner()+ "','"
					+ prod.getName()+ "','"	+ prod.getPrice()+ "',0)");

		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
	
//when user try  to list all the items available in the market
	public synchronized ArrayList list(String buyer) throws RemoteException {
		ArrayList products = new ArrayList();
		try {
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///tradeSystem", "root","1234");
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT id ,product , price, seller FROM product WHERE isSold=0 and seller<>'"+ buyer +"'");
			while (rs.next()) {
				Product temp = new Product();
				temp.setId(Integer.parseInt(rs.getString("id")));
				temp.setName(rs.getString("product")); 
				temp.setPrice(Integer.parseInt(rs.getString("price")));
				temp.setOwner(rs.getString("seller")); 
				products.add(temp.ToString(true));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}
	
//statistics of a user as total buy and total sell
	public synchronized String statistics(String username) throws RemoteException {
		String result="";
		try {
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///tradeSystem", "root","1234");
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT id  FROM transactions WHERE buyer='"+ username +"'");
			rs.last();
			int temp= rs.getRow();
			result="Total buy:"+temp+"\n";
			rs = stmt.executeQuery("SELECT id  FROM transactions WHERE seller='"+ username +"'");
			rs.last();
			temp= rs.getRow();
			result+="Total sell:"+temp+"\n";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
//when a user try to buy an item this function is called
	public synchronized int buy(int id,String buyer) throws RemoteException {
		try {
			
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///tradeSystem", "root","1234");
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT id,seller,product,price,isSold FROM product where id='"+ id + "'");

			if (rs.next()) {
				try {
					String seller = rs.getString("seller");
					String product = rs.getString("product");
					int price = Integer.parseInt(rs.getString("price"));
					Account buyerAcc = new AccountImpl();
					buyerAcc = bank.getAccount(buyer);
					
					if (buyerAcc.balance() >= price) {
						Account sellerAcc = new AccountImpl();
						sellerAcc = bank.getAccount(seller);
						if(sellerAcc == null)
						{
							// if seller account is not logged in this session, seller bank account details are loaded from database
							ResultSet rs2 = stmt.executeQuery("SELECT balance FROM user WHERE name='"+ seller + "'" );
							if (rs2.next())
							{
								sellerAcc = bank.newAccount(seller);
								System.out.println("Account created");
								sellerAcc.deposit(Integer.parseInt(rs2.getString("balance")));
								System.out.println("balance loaded");
							}
						}
						//balance updates are done after trade is successful
						stmt.executeUpdate("UPDATE product SET isSold=1 WHERE id=" + id);
						stmt.executeUpdate("INSERT INTO transactions(id,product,price,buyer,seller) VALUES ('"+id+"','"+product+"','" +price+"','"
								+buyer+"','"+seller+"')");
						buyerAcc.withdraw(price);
						sellerAcc.deposit(price);
						System.out.println("Item sold");
						return 1;
					} 
					else {
						return 2;
					}
				}

				catch (Exception e) {
					return 3;
				}

			}
			return 3;
		} catch (Exception e) {
			return 3;
		}

	}

}

