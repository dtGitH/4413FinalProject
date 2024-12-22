package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the user's profile information. This class contains details such as
 * the user's personal information (name, email, etc.), contact information, billing
 * and shipping addresses, as well as the user's registration date and last update date.
 */
public class UserProfile {
    private String userUuid; // Unique identifier for the user
    private String firstName; // First name of the user
    private String lastName; // Last name of the user
    private String email; // Email address for contact and login
    private String phone; // Contact number of the user
    private LocalDate dob; // Date of birth of the user
    private String gender; // Gender (male, female, other)
    private String address; // Default shipping address of the user
    private String billingAddress; // Default billing address of the user
    private String creditCard; // Credit card information for payment
    private LocalDateTime registrationDate; // Date of account registration
    private LocalDateTime lastUpdated; // Date of the last profile update

    /**
     * Default constructor for creating an empty user profile.
     */
    public UserProfile() {
    }

    /**
     * Parameterized constructor to initialize a user profile with all relevant details.
     *
     * @param userUuid        The unique identifier for the user.
     * @param firstName       The first name of the user.
     * @param lastName        The last name of the user.
     * @param email           The email address of the user.
     * @param phone           The phone number of the user.
     * @param dob             The date of birth of the user.
     * @param gender          The gender of the user.
     * @param address         The shipping address of the user.
     * @param billingAddress  The billing address of the user.
     * @param creditCard      The credit card information of the user.
     * @param registrationDate The date of account registration.
     * @param lastUpdated     The date when the profile was last updated.
     */
    public UserProfile(String userUuid, String firstName, String lastName, String email, String phone, LocalDate dob,
                       String gender, String address, String billingAddress, String creditCard, LocalDateTime registrationDate,
                       LocalDateTime lastUpdated) {
        this.userUuid = userUuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.billingAddress = billingAddress;
        this.creditCard = creditCard;
        this.registrationDate = registrationDate;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the user.
     *
     * @return The user's UUID.
     */
    public String getUserUuid() {
        return userUuid;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param userUuid The user's UUID.
     */
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The user's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return The user's phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phone The user's phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the date of birth of the user.
     *
     * @return The user's date of birth.
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the user.
     *
     * @param dob The user's date of birth.
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    /**
     * Gets the gender of the user.
     *
     * @return The user's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender The user's gender.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the shipping address of the user.
     *
     * @return The user's shipping address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the shipping address of the user.
     *
     * @param address The user's shipping address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the billing address of the user.
     *
     * @return The user's billing address.
     */
    public String getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the billing address of the user.
     *
     * @param billingAddress The user's billing address.
     */
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Gets the credit card information of the user.
     *
     * @return The user's credit card information.
     */
    public String getCreditCard() {
        return creditCard;
    }

    /**
     * Sets the credit card information of the user.
     *
     * @param creditCard The user's credit card information.
     */
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * Gets the registration date of the user profile.
     *
     * @return The user's registration date.
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the registration date of the user profile.
     *
     * @param registrationDate The user's registration date.
     */
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Gets the last updated date of the user profile.
     *
     * @return The user's last updated date.
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Sets the last updated date of the user profile.
     *
     * @param lastUpdated The user's last updated date.
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
