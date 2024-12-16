package observer;

public interface OrderObserver {
	void onOrderPlaced(int orderId, String email);
}
