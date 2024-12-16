package observer;

public class AdminNotificationObserver implements OrderObserver{
	@Override
	public void onOrderPlaced(int orderId, String email) {
		System.out.println("Notifying admin about user " + email + " place new order with ID: " + orderId);
		
	}

}
