package com.busarrival.app.utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * ValidationHelper Class
 * Provides input validation methods for the application
 */
public class ValidationHelper {

    /**
     * Validates if a string is empty or null
     * @param text The text to validate
     * @return true if empty, false otherwise
     */
    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || text.trim().isEmpty();
    }

    /**
     * Validates email format
     * @param email The email address to validate
     * @return true if valid email format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validates password length
     * @param password The password to validate
     * @param minLength Minimum required length
     * @return true if password meets minimum length, false otherwise
     */
    public static boolean isValidPassword(String password, int minLength) {
        if (isEmpty(password)) {
            return false;
        }
        return password.length() >= minLength;
    }

    /**
     * Validates if two passwords match
     * @param password The original password
     * @param confirmPassword The confirmation password
     * @return true if passwords match, false otherwise
     */
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        if (isEmpty(password) || isEmpty(confirmPassword)) {
            return false;
        }
        return password.equals(confirmPassword);
    }

    /**
     * Validates phone number format
     * @param phone The phone number to validate
     * @return true if valid phone format, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        // Basic phone validation - at least 8 digits
        String phonePattern = "^[+]?[0-9]{8,15}$";
        return phone.matches(phonePattern);
    }

    /**
     * Validates name (should contain only letters and spaces)
     * @param name The name to validate
     * @return true if valid name, false otherwise
     */
    public static boolean isValidName(String name) {
        if (isEmpty(name)) {
            return false;
        }
        // Name should contain at least 2 characters and only letters and spaces
        String namePattern = "^[a-zA-Z\\s]{2,}$";
        return name.matches(namePattern);
    }

    /**
     * Validates time format (HH:mm)
     * @param time The time string to validate
     * @return true if valid time format, false otherwise
     */
    public static boolean isValidTimeFormat(String time) {
        if (isEmpty(time)) {
            return false;
        }
        String timePattern = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
        return time.matches(timePattern);
    }

    /**
     * Validates if a number is positive
     * @param number The number to validate
     * @return true if positive, false otherwise
     */
    public static boolean isPositiveNumber(int number) {
        return number > 0;
    }
}
