package model;

public class PaymentService {
    private static int paymentRequestCount = 0; // Count of all payment requests
    
    /**
     * Simulate payment authorization.
     * This method increments the request count and denies every 3rd payment request.
     * 
     * @param creditCard The credit card used for payment (can be a dummy value)
     * @param amount The amount to be paid
     * @return true if payment is successful, false if payment is denied
     */
    public static boolean authorizePayment(String creditCard, double amount) {
        paymentRequestCount++; // Increment the request count

        // Simple logic to deny every 3rd request
        if (paymentRequestCount % 3 == 0) {
            System.out.println("Payment Denied. Request #" + paymentRequestCount);
            return false; // Deny every 3rd request
        }

        System.out.println("Payment Accepted. Request #" + paymentRequestCount);
        return true; // Accept other payments
    }

    /**
     * Resets the request counter for testing or debugging purposes.
     */
    public static void resetPaymentCounter() {
        paymentRequestCount = 0;
    }
}
