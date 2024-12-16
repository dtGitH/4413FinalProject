package observer;

public class EmailNotificationObserver implements OrderObserver {
	@Override
	public void onOrderPlaced(int orderId) {
		System.out.println("Sending email notification for Order ID: " + orderId);
	}
}
