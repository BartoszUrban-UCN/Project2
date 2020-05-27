package controller;

import db.DataAccessException;
import db.DepartmentDB;
import db.DepartmentIF;
import model.Department;

import java.util.List;

public class DepartmentController {
    private final DepartmentIF departmentDB;

    /**
     * Initialize the department database thru the interface
     */
    public DepartmentController() {
        departmentDB = new DepartmentDB();
    }

    /**
     * Create and insert new department into database
     * @param department
     */
    public boolean createDepartment(Department department) throws DataAccessException {
        if (departmentDB.findByID(department.getDepartmentID(), false) == null)
            return departmentDB.insert(department);
        return false;
    }

    /**
     * Find an department by ID
     * @param ID
     */
    public Department findDepartmentByID(int ID, boolean fullAssociation) throws DataAccessException {
        return departmentDB.findByID(ID, fullAssociation);
    }

    /**
     * Get all departmentes from the database as list
     */
    public List<Department> getAllDepartments(boolean fullAssociation) throws DataAccessException {
        return departmentDB.getAll(fullAssociation);
    }

    /**
     * Update an department with all the fields
     */
    public boolean updateDepartment(Department departmentToUpdate, String name) throws DataAccessException {

        departmentToUpdate
                .setName(name);

        return departmentDB.update(departmentToUpdate);
    }

    /**
     * Delete an department from the database by ID
     */
    public boolean deleteDepartment(int ID) throws DataAccessException {
        return departmentDB.delete(ID);
    }
}
