package observer;

public class EmailNotificationObserver implements OrderObserver {
	@Override
	public void onOrderPlaced(int orderId, String email) {
		System.out.println("Sending email notification " + email + " place new order with ID: " + orderId);
	}
}
