package controller;

import db.CustomerDB;
import db.CustomerIF;
import db.DataAccessException;
import model.Address;
import model.Customer;

import java.util.List;

public class CustomerController {
    private final CustomerIF customerDB;
    private final AddressController addressController;

    public CustomerController() {
        customerDB = new CustomerDB();
        addressController = new AddressController();
    }

    public boolean createCustomer(Customer customer) throws DataAccessException {
        if (findCustomerByEmail(customer.getEmail(), false) == null) {
            addressController.createAddress(customer.getAddress());
            return customerDB.insert(customer);
        }
        return false;
    }

    public Customer findCustomerByID(int ID, boolean fullAssociation) throws DataAccessException {
        return customerDB.findByID(ID, fullAssociation);
    }

    public Customer findCustomerByEmail(String email, boolean fullAssociation) throws DataAccessException {
        return customerDB.findByEmail(email, fullAssociation);
    }

    public List<Customer> getAllCustomers(boolean fullAssociation) throws DataAccessException {
        return customerDB.getAll(fullAssociation);
    }

    public boolean updateCustomer(Customer customerToUpdate, String newFirstName, String newLastName,
            String newPhoneNumber, String newEmail, String newCustomerType, String newCompanyName,
            Address addressToUpdate) throws DataAccessException {
        if (customerDB.findByEmail(newEmail, false) == null) {
            customerToUpdate
                    .setFirstName(newFirstName)
                    .setLastName(newLastName)
                    .setPhoneNumber(newPhoneNumber)
                    .setEmail(newEmail);

            customerToUpdate
                    .setCustomerType(newCustomerType)
                    .setCompanyName(newCompanyName)
                    .setAddress(addressToUpdate);

            return customerDB.update(customerToUpdate);
        }
        return false;
    }

    public boolean deleteCustomer(int ID) throws DataAccessException {
        return customerDB.delete(ID);
    }
}