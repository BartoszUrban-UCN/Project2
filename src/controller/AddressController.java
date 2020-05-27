package controller;

import db.AddressDB;
import db.AddressIF;
import db.DataAccessException;
import model.Address;
import model.Department;

import java.util.ArrayList;
import java.util.List;

public class AddressController {
    private final AddressIF addressDB;

    /**
     * Initialize the address database thru the interface
     */
    public AddressController() {
        addressDB = new AddressDB();
    }

    /**
     * Create and insert new address into database
     * 
     * @param address
     */
    public boolean createAddress(Address address) throws DataAccessException {
        if (addressDB.findEquals(address) == null)
            return addressDB.insert(address);
        return false;
    }

    /**
     * Find an address by ID
     * 
     * @param ID
     */
    public Address findAddressByID(int ID, boolean fullAssociation) throws DataAccessException {
        return addressDB.findByID(ID, fullAssociation);
    }

    /**
     * Get all addresses from the database as list
     */
    public List<Address> getAllAddresses(boolean fullAssociation) throws DataAccessException {
        return addressDB.getAll(fullAssociation);
    }

    /**
     * Update an address with all the fields
     */
    public boolean updateAddress(Address addressToUpdate, String country, String zipCode,
            String streetName, int streetNumber, ArrayList<Department> departments) throws DataAccessException {

        addressToUpdate
                .setCountry(country)
                .setZipCode(zipCode)
                .setStreetName(streetName);
        addressToUpdate
                .setDepartments(departments);

        if (addressDB.findEquals(addressToUpdate) == null)
            return addressDB.update(addressToUpdate);
        return false;
    }

    /**
     * Delete an address from the database by ID
     */
    public boolean deleteAddress(int ID) throws DataAccessException {
        return addressDB.delete(ID);
    }

    public Address findEquals(Address address) throws DataAccessException {
        return addressDB.findEquals(address);
    }
}
