package test.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import controller.InquiryController;
import controller.ResponseController;
import controller.TicketController;
import db.CustomerDB;
import db.DataAccessException;
import db.EmployeeDB;
import model.Customer;
import model.Employee;
import model.Inquiry;
import model.Response;
import model.Ticket;

class IntegrationTesting {
	private static TicketController tCtrl;
	private static InquiryController iCtrl;
	private static ResponseController rCtrl;
	private static Ticket t;
	private static Customer c;
	private static Employee e;
	private static Inquiry i;
	private static Response r;
	private static int tID;
	
	
	
	@BeforeAll
	static void initialize() {
	tCtrl = new TicketController();
	iCtrl = new InquiryController();
	rCtrl = new ResponseController();
	
	
	
	
	}
	
	// createTicket
		@Test
		@Order(1)
		void createTicket() throws DataAccessException {
		//Arrange
		t = new Ticket();
			c = new CustomerDB().findByID( 2, true);
			t.setCustomer(c);
		//Act	
			try {
				
				boolean created = tCtrl.createTicket(t);
				
				
		//Assert	
				assertTrue(created);
				assertEquals(t.getTicketID(), tCtrl.findTicketByID(t.getTicketID(), true).getTicketID());
				assertEquals(c.getFullName(), tCtrl.findTicketByID(t.getTicketID(), true).getCustomer().getFullName());
			}
			catch(DataAccessException e){
				fail(e);
			}
		}
	
	// createInquiry
	@Test
	@Order(2)
	void createInquiry() throws DataAccessException{
		//Arrange
		i = new Inquiry("Test", "This is a test", LocalDateTime.now());
		t = new Ticket();
		c = new CustomerDB().findByID( 2, true);
		t.setCustomer(c);
		tCtrl.createTicket(t);
		
		//Act
		try {
			boolean created = iCtrl.createInquiry(i, t.getTicketID() );
		//Assert	
			assertTrue(created);
			
			
			assertEquals("Test", i.getTitle());
			assertEquals("This is a test", i.getDescription());
			
			Inquiry dbI = tCtrl.findTicketByID(t.getTicketID(), true).getInquiries().get(tCtrl.findTicketByID(t.getTicketID(), true).getInquiries().size()-1);
			assertEquals(i.getDescription(), dbI.getDescription());
		}
		catch(DataAccessException e){
			fail(e);
		}
	}
	

	//Test createResponse
	@Test
	@Order(3)
	void createResponse() throws DataAccessException{
	
		
		//Arrange
		r = new Response("Test Response", "This is a response to the test", LocalDateTime.now());
		i = new Inquiry("Test", "This is a test", LocalDateTime.now());
		t = new Ticket();
		c = new CustomerDB().findByID( 2, true);
		e = new EmployeeDB().findByID(1, true);
		r.setEmployee(e);
		t.setCustomer(c);
		tCtrl.createTicket(t);
		iCtrl.createInquiry(i, t.getTicketID());
		
		//Act
		try {
			boolean created = rCtrl.createResponse(r, i.getInquiryID());
			
			//Assert
			assertTrue(created);
			Response dbR = iCtrl.findInquiryByID(i.getInquiryID(), true).getResponses().get(iCtrl.findInquiryByID(i.getInquiryID(), true).getResponses().size()-1);
			assertEquals(r.getTitle(), dbR.getTitle());
			assertEquals(r.getDescription(), dbR.getDescription());
		}
		
		catch(DataAccessException e) {
			fail(e);
		}
		
	}
	
	//Test update ticket
	@Test
	void updateTicket() throws DataAccessException{
		//Arrange
		r = new Response("Test Response", "This is a response to the test", LocalDateTime.now());
		i = new Inquiry("Test", "This is a test", LocalDateTime.now());
		t = new Ticket();
		c = new CustomerDB().findByID( 2, true);
		e = new EmployeeDB().findByID(1, true);
		r.setEmployee(e);
		t.setCustomer(c);
		tCtrl.createTicket(t);
		iCtrl.createInquiry(i, t.getTicketID());
		rCtrl.createResponse(r, i.getInquiryID());
		
		LocalDateTime d1 = LocalDateTime.now();
		LocalDateTime d2 = LocalDateTime.now();
		
		//act
		try {
			
			boolean created = tCtrl.updateTicket(t, "solved", "low", d1, d2, null, null);
			assertTrue(created);
		
		//assert
			Ticket dbT = tCtrl.findTicketByID(t.getTicketID(), true);
			assertEquals("solved", dbT.getComplaintStatus());
			assertEquals("low", dbT.getPriority());
			assertEquals(d1, dbT.getStartDate());
			assertEquals(d1, dbT.getEndDate());
			assertEquals(e.getFullName(), dbT.getEmployee().getFullName());
			assertEquals(c.getFullName(), dbT.getCustomer().getFullName());
			
			
		}
		
		catch(DataAccessException e) {
			fail(e);
		}
	}
		
			
	@AfterAll
			static void cleanUp() {
            try {
                tCtrl.deleteTicket(t.getTicketID());
            } catch (DataAccessException e) {
                fail(e);
            }
            try {
           	iCtrl.deleteInquiry(i.getInquiryID());
            }
            catch(DataAccessException e) {
            	fail(e);
            }
            try {
               	rCtrl.deleteResponse(r.getResponseID());
                }
                catch(DataAccessException e) {
                	fail(e);
                }
		}
		
	}

	
	

