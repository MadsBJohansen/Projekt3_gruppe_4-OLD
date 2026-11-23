package com.gruppe4.projekt3_gruppe_4.Service;

import com.gruppe4.projekt3_gruppe_4.Model.Customer;
import com.gruppe4.projekt3_gruppe_4.Model.ZipCode;
import com.gruppe4.projekt3_gruppe_4.Repo.CustomerRepo;
import com.gruppe4.projekt3_gruppe_4.Repo.ZipCodeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepo customerRepo;   // falsk repo - ingen database

    @Mock
    private ZipCodeRepo zipCodeRepo;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setFirstName("Anna");
        customer.setLastName("Jensen");
        customer.setEmail("anna@example.com");
        customer.setPhoneNumber("+4512345678");
        customer.setStreetAddress("Guldbergsgade 29");

    }

    @Test
    void createCustomer() {

        ZipCode zip = new ZipCode(1, "2200", "København N");
        when(customerRepo.existsByEmail("anna@example.com")).thenReturn(false);
        when(zipCodeRepo.findByZipCode("2200")).thenReturn(zip);
        customerService.createCustomer(customer, "2200");
        assertEquals(zip, customer.getZipCode());
        verify(customerRepo).saveCustomer(customer);
    }

    @Test
    void invalidEmail() {

        customer.setEmail("anna.example.com");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(customer, "2200"));
        assertEquals("Invalid email", e.getMessage());
        verify(customerRepo, never()).saveCustomer(any());

    }

    @Test
    void blankField() {

        customer.setFirstName("");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(customer, "2200"));
        assertEquals("All fields need to be filled", e.getMessage());
        verify(customerRepo, never()).saveCustomer(any());

    }

    @Test
    void alreadyExist() {
        when(customerRepo.existsByEmail("anna@example.com")).thenReturn(true);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(customer,"2200"));
        assertEquals("Email already exist", e.getMessage());
        verify(customerRepo, never()).saveCustomer(any());
    }
}