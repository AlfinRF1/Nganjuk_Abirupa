package com.example.nganjukabirupa;

public class RegisterResponse {
    private boolean success;
    private String message;
    private Customer customer;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Customer getCustomer() { return customer; }
}