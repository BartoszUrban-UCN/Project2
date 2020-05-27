package controller;

import java.util.List;

import db.DataAccessException;
import db.FieldDB;
import db.FieldIF;
import model.FormField;

public class FieldController {
	private final FieldIF fieldDB;

	public FieldController() {
		fieldDB = new FieldDB();
	}

	public boolean createField(FormField field, int inquiryID) throws DataAccessException {
		return fieldDB.insert(field, inquiryID);
	}

	public FormField findFieldByID(int id, boolean fullAssociation) throws DataAccessException {
		return fieldDB.findByID(id, fullAssociation);
	}

	public List<FormField> findFieldsByName(String name, boolean fullAssociation) throws DataAccessException {
		return fieldDB.findFieldsByName(name, fullAssociation);
	}

	public List<FormField> findFieldsByInquiryID(int inquiryID, boolean fullAssociation) throws DataAccessException {
		return fieldDB.findFieldsByInquiryID(inquiryID, fullAssociation);
	}

	public List<FormField> getAllFields(boolean fullAssociation) throws DataAccessException {
		return fieldDB.getAll(fullAssociation);
	}

	public boolean updateField(FormField fieldToUpdate, String newName, String newContent) throws DataAccessException {
		FormField field = new FormField();
		field.setName(newName).setContent(newContent);

		return fieldDB.update(fieldToUpdate);
	}

	public boolean deleteField(int id) throws DataAccessException {
		return fieldDB.delete(id);
	}
}
