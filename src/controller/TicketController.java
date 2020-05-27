package controller;

import db.DataAccessException;
import db.TicketDB;
import db.TicketIF;
import model.Customer;
import model.Employee;
import model.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public class TicketController {
    private final TicketIF ticketDB;

    public TicketController() {
        ticketDB = new TicketDB();
    }

    public boolean createTicket(Ticket ticket) throws DataAccessException {
        return ticketDB.insert(ticket);
    }

    public Ticket findTicketByID(int id, boolean fullAssociation) throws DataAccessException {
        return ticketDB.findByID(id, fullAssociation);
    }
    
    public List<Ticket> findTicketsByEmployee(Employee employee, boolean fullAssociation) throws DataAccessException {
    	return ticketDB.findTicketsByEmployee(employee, fullAssociation);
    }
    
    public List<Ticket> findTicketsByCustomer(Customer customer, boolean fullAssociation) throws DataAccessException {
    	return ticketDB.findTicketsByCustomer(customer, fullAssociation);
    }

    public List<Ticket> getAllTickets(boolean fullAssociation) throws DataAccessException {
        return ticketDB.getAll(fullAssociation);
    }

    public boolean updateTicket(Ticket ticketToUpdate, String newComplaintStatus, String newPriority,
            LocalDateTime newStartDate, LocalDateTime newEndDate, Employee newEmployee, Customer newCustomer)
            throws DataAccessException {
        Ticket ticket = new Ticket();
        ticket.setComplaintStatus(newComplaintStatus).setPriority(newPriority).setStartDate(newStartDate)
                .setEndDate(newEndDate).setEmployee(newEmployee).setCustomer(newCustomer);

        return ticketDB.update(ticketToUpdate);
    }

    public boolean deleteTicket(int id) throws DataAccessException {
        return ticketDB.delete(id);
    }
}