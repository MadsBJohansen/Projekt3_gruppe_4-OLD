package com.gruppe4.projekt3_gruppe_4.Service;

import com.gruppe4.projekt3_gruppe_4.Model.Customer;
import com.gruppe4.projekt3_gruppe_4.Model.ZipCode;
import com.gruppe4.projekt3_gruppe_4.Repo.CustomerRepo;
import com.gruppe4.projekt3_gruppe_4.Repo.ZipCodeRepo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final ZipCodeRepo zipCodeRepo;

    public CustomerService(CustomerRepo customerRepo, ZipCodeRepo zipCodeRepo) {
        this.customerRepo = customerRepo;
        this.zipCodeRepo = zipCodeRepo;
    }

    public void createCustomer(Customer customer, String zipCodeInput) {
        if (isBlank(customer.getFirstName()) || isBlank(customer.getLastName())
                || isBlank(customer.getEmail()) || isBlank(customer.getPhoneNumber())
                || isBlank(customer.getStreetAddress()) || isBlank(zipCodeInput)) {
            throw new IllegalArgumentException("All fields need to be filled");
        }

        validateEmail(customer);
        ZipCode zipCode = validateZipCode(zipCodeInput);
        customer.setZipCode(zipCode);
        customerRepo.saveCustomer(customer);
    }


    private ZipCode validateZipCode(String zipCodeInput) {
        ZipCode zipCode = zipCodeRepo.findByZipCode(zipCodeInput.trim());
        if (zipCode == null) {
            throw new IllegalArgumentException("Zip code does not exist");
        }
        return zipCode;
    }

    private void validateEmail(Customer customer) {
        if (!customer.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exist");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public List<ZipCode> getAllZipCodes() {
        return zipCodeRepo.findAll();
    }


}
