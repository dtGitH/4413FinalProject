package model;

/**
 * Simulates a payment authorization service. It handles the simulation of
 * payment requests and randomly denies every 3rd payment request for
 * demonstration purposes.
 */
public class PaymentService {

	private static int paymentRequestCount = 0;

	/**
	 * Simulates the authorization of a payment.
	 * 
	 * This method increments the payment request count and denies every 3rd request
	 * to simulate a failed payment. If the request count is divisible by 3, the
	 * payment will be denied; otherwise, it will be accepted.
	 *
	 * @param creditCard The credit card used for payment (can be a dummy value).
	 * @param amount     The amount to be paid.
	 * @return true if the payment is authorized, false if the payment is denied.
	 */
	public static boolean authorizePayment(String creditCard, double amount) {
		paymentRequestCount++;

		// Deny every 3rd payment request
		if (paymentRequestCount % 3 == 0) {
			System.out.println("Payment Denied. Request #" + paymentRequestCount);
			return false;
		}

		// Accept payment for all other requests
		System.out.println("Payment Accepted. Request #" + paymentRequestCount);
		return true;
	}

	/**
	 * Resets the payment request counter. This can be useful for testing or
	 * debugging purposes.
	 */
	public static void resetPaymentCounter() {
		paymentRequestCount = 0;
	}
}
