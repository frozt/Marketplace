package marketplace;

import java.rmi.*;



public class MarketServer extends Thread{
	
	public MarketServer() {

		try {
			String seller = "trader";
			User userobj = new UserImpl();
			// Register the newly created object at rmiregistry.
			Naming.rebind(seller, userobj);
			System.out.println(" Server is ready.");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MarketServer().run();

	}
	public void run() {
		// Wait for incoming requests
		try {
			while (true) {
				try {
					;
					sleep(0, 1);
				} catch (InterruptedException e) {
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
