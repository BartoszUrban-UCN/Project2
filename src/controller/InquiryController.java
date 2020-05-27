package controller;

import java.time.LocalDateTime;
import java.util.List;

import db.DataAccessException;
import db.InquiryDB;
import db.InquiryIF;
import model.Department;
import model.Inquiry;

public class InquiryController {
	private final InquiryIF inquiryDB;

	public InquiryController() {
		inquiryDB = new InquiryDB();
	}

	public boolean createInquiry(Inquiry inquiry, int ticketID) throws DataAccessException {
		return inquiryDB.insert(inquiry, ticketID);
	}

	public Inquiry findInquiryByID(int id, boolean fullAssociation) throws DataAccessException {
		return inquiryDB.findByID(id, fullAssociation);
	}
	
	public List<Inquiry> findInquiriesByTitle(String title, boolean fullAssociation) throws DataAccessException {
		return inquiryDB.findInquiriesByTitle(title, fullAssociation);
	}
	
	public List<Inquiry> findInquiriesByTicketID(int ticketID, boolean fullAssociation) throws DataAccessException {
		return inquiryDB.findInquiriesByTicketID(ticketID, fullAssociation);
	}
	
	public List<Inquiry> findInquiriesByDepartmentID(int departmentID, boolean fullAssociation)
			throws DataAccessException {
		return inquiryDB.findInquiriesByDepartmentID(departmentID, fullAssociation);
	}

	public List<Inquiry> getAllInquiries(boolean fullAssociation) throws DataAccessException {
		return inquiryDB.getAll(fullAssociation);
	}

	public boolean updateInquiry(Inquiry inquiryToUpdate, String newTitle, String newDescription,
			LocalDateTime newTimestamp, Department newDepartment) throws DataAccessException {
		Inquiry inquiry = new Inquiry();
		inquiry.setTitle(newTitle).setDescription(newDescription).setTimestamp(newTimestamp)
				.setDepartment(newDepartment);

		return inquiryDB.update(inquiryToUpdate);
	}

	public boolean deleteInquiry(int id) throws DataAccessException {
		return inquiryDB.delete(id);
	}
}
