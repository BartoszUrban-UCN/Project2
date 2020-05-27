package controller;

import db.DataAccessException;
import db.EmployeeDB;
import db.EmployeeIF;
import model.Department;
import model.Employee;

import java.util.List;

public class EmployeeController {
    private final EmployeeIF employeeDB;

    /**
     * Initialize the employee database thru the interface
     */
    public EmployeeController() {
        employeeDB = new EmployeeDB();
    }

    /**
     * Create and insert new employee into database
     * @param employee
     */
    public boolean createEmployee(Employee employee) throws DataAccessException {
        return employeeDB.insert(employee);
    }

    /**
     * Find an employee by ID
     * @param ID
     */
    public Employee findEmployeeByID(int ID, boolean fullAssociation) throws DataAccessException {
        return employeeDB.findByID(ID, fullAssociation);
    }

    public Employee findEmployeeByEmail(String email, boolean fullAssociation) throws DataAccessException {
        return employeeDB.findByEmail(email, fullAssociation);
    }
    /**
     * Get all employees from the database as list
     */
    public List<Employee> getAllEmployees(boolean fullAssociation) throws DataAccessException {
        return employeeDB.getAll(fullAssociation);
    }

    /**
     * Update an employee with all the fields
     */
    public boolean updateEmployee(Employee employeeToUpdate, String newFirstName, String newLastName, String newPhoneNumber,
                                  String newEmail, String newPassword, String newEmployeeType, Department newDepartment) throws DataAccessException {
        if (employeeDB.findByEmail(newEmail, false) == null) {
            employeeToUpdate
                    .setFirstName(newFirstName)
                    .setLastName(newLastName)
                    .setPhoneNumber(newPhoneNumber)
                    .setEmail(newEmail);

            employeeToUpdate
                    .setPassword(newPassword)
                    .setEmployeeType(newEmployeeType)
                    .setDepartment(newDepartment);

            return employeeDB.update(employeeToUpdate);
        }
        return false;
    }

    /**
     * Delete an employee from the database by ID
     */
    public boolean deleteEmployee(int ID) throws DataAccessException {
        return employeeDB.delete(ID);
    }
}
