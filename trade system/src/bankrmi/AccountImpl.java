package bankrmi;
import java.rmi.*;
import java.rmi.server.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import bankrmi.*;
public class AccountImpl extends UnicastRemoteObject
  implements bankrmi.Account {
  private float _balance = 0;
  private String _name = "noname";

  /** Construct a persistently named object. */
  public AccountImpl(java.lang.String name) throws RemoteException {
    super();
    _name = name;
  }

  /** Construct a transient object. */
  public AccountImpl() throws RemoteException  {
	  super();
  }

  public synchronized void deposit(float value)
	  throws RemoteException, Rejected {
	  if (value < 0)
	    throw new Rejected("Rejected: Account " +
						   _name +
						   ": Illegal value: " +
						   value);

	  _balance += value;
	  System.out.println("Transaction: Account " +
			   _name + ": deposit: $" +
			   value + ", balance: $" +
			   _balance);
	  
		try {
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///tradeSystem", "root","1234");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE user SET balance="+ _balance + " WHERE name='" + _name + "'");


		} catch (Exception E) {
			E.printStackTrace();
			
		}
  }

  public synchronized void withdraw(float value)
	  throws RemoteException, Rejected {
	  if (value < 0)
	    throw new Rejected("Rejected: Account " +
						   _name +
						   ": Illegal value: " +
						   value);
    if ((_balance - value) < 0)
	    throw new Rejected("Rejected: Account "	+
						   _name +
						   ": Negative balance " +
						   "on withdraw: " +
						   (_balance - value));
    _balance -= value;
	  System.out.println("Transaction: Account " +
			   _name + ": withdraw: $" +
			   value + ", balance: $" +
			   _balance);
	  
	  try {
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///tradeSystem", "root","1234");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE user SET balance=" + _balance + " WHERE name='" + _name + "'");


		} catch (Exception E) {
			E.printStackTrace();
			
		}
  }
  public synchronized float balance()
    throws RemoteException {
	  return _balance;
  }
} 