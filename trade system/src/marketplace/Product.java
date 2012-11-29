package marketplace;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;

//product class is the general class of each product
public class Product{
	protected Product() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	private int id;
	private String name;
	private int price;
	private String owner;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String ToString(boolean IDincluded)
	{
		String result=this.getId() + "," + this.getName() + "," +this.getPrice() +"," + this.getOwner();
		return result;
	}
	public String ToString()
	{
		String result= this.getName() + "," +this.getPrice() +"," + this.getOwner();
		System.out.println(result);
		return result;
	}
	public void toObject (String str, boolean IDincluded) throws RemoteException
	{
		
		StringTokenizer st = new StringTokenizer(str, ",");
		this.setId(Integer.parseInt(st.nextToken()));
		this.setName(st.nextToken());
		this.setPrice(Integer.parseInt(st.nextToken()));
		this.setOwner(st.nextToken());

	}
	public void toObject (String str) throws RemoteException
	{
		System.out.println(str);
		StringTokenizer st = new StringTokenizer(str, ",");
		this.setName(st.nextToken());
		this.setPrice(Integer.parseInt(st.nextToken()));
		this.setOwner(st.nextToken());
	}
	

}
