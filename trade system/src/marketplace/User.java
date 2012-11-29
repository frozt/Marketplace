package marketplace;

import java.rmi.*;
import java.util.ArrayList;

public interface User extends Remote{
	
	public boolean newUser(String name, String password) throws RemoteException;

	public boolean login(String name, String password) throws RemoteException;
	
	public boolean sell(String product) throws RemoteException;
	
	public ArrayList list(String buyer) throws RemoteException;
	
	public int buy(int id,String buyer) throws RemoteException;
	
	public String statistics(String username) throws RemoteException;

}
