
package main.controllers;

import java.util.Collections;
import java.util.List;
import main.models.Rental;
import main.services.CustomerService;


public class CustomerController {
    private final CustomerService customerService = new CustomerService();

    public boolean addCustomer(CustomerProfile customer) {
        if (customer == null) {
            System.out.println("Customer profile is null. Please provide valid customer details.");
            return false;
        }
        try {
            return customerService.addCustomer(customer);
        } catch (Exception e) {
            System.out.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCustomer(int profileId) {
        if (profileId <= 0) {
            System.out.println("Invalid profile ID. Please provide a valid profile ID greater than 0.");
            return false;
        }
        try {
            return customerService.deleteCustomer(profileId);
        } catch (Exception e) {
            System.out.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCustomer(CustomerProfile customer) {
        if (customer == null || customer.getProfileId() <= 0) {
            System.out.println("Invalid customer details. Please provide a valid customer profile with an ID.");
            return false;
        }
        try {
            return customerService.updateCustomer(customer);
        } catch (Exception e) {
            System.out.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    public List<CustomerProfile> getAllCustomers() {
        try {
            List<CustomerProfile> customers = customerService.getAllCustomers();
            return customers != null ? customers : Collections.emptyList();
        } catch (Exception e) {
            System.out.println("Error retrieving customers: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Rental> getRentalHistoryByCustomer(int customerId) {
        if (customerId <= 0) {
            System.out.println("Invalid customer ID. Please provide a valid customer ID greater than 0.");
            return Collections.emptyList();
        }
        try {
            List<Rental> rentalHistory = customerService.getRentalHistoryByCustomer(customerId);
            return rentalHistory != null ? rentalHistory : Collections.emptyList();
        } catch (Exception e) {
            System.out.println("Error retrieving rental history: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
