package controller;

import java.time.LocalDateTime;
import java.util.List;

import db.DataAccessException;
import db.ResponseDB;
import db.ResponseIF;
import model.Employee;
import model.Response;

public class ResponseController {
	private final ResponseIF responseDB;

	public ResponseController() {
		responseDB = new ResponseDB();
	}

	public boolean createResponse(Response response, int inquiryID) throws DataAccessException {
		return responseDB.insert(response, inquiryID);
	}

	public Response findResponseByID(int id, boolean fullAssociation) throws DataAccessException {
		return responseDB.findByID(id, fullAssociation);
	}
	
	public List<Response> findResponsesByTitle(String title, boolean fullAssociation) throws DataAccessException {
		return responseDB.findResponsesByTitle(title, fullAssociation);
	}
	
	public List<Response> findResponsesByInquiryID(int inquiryID, boolean fullAssociation) throws DataAccessException {
		return responseDB.findResponsesByInquiryID(inquiryID, fullAssociation);
	}
	
	public List<Response> findResponsesByEmployeeID(int employeeID, boolean fullAssociation) throws DataAccessException {
		return responseDB.findResponsesByEmployeeID(employeeID, fullAssociation);
	}

	public List<Response> getAllResponses(boolean fullAssociation) throws DataAccessException {
		return responseDB.getAll(fullAssociation);
	}

	public boolean updateResponse(Response responseToUpdate, String newTitle, String newDescription,
			LocalDateTime newTimestamp, Employee newEmployee) throws DataAccessException {
		Response response = new Response();
		response.setTitle(newTitle).setDescription(newDescription).setTimestamp(newTimestamp)
				.setEmployee(newEmployee);

		return responseDB.update(responseToUpdate);
	}

	public boolean deleteResponse(int id) throws DataAccessException {
		return responseDB.delete(id);
	}
}
