package controller;

import db.DBConnection;
import db.DataAccessException;
import db.TicketDB;
import db.TicketIF;
import model.Customer;
import model.Employee;
import model.Ticket;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TicketController {
    private final TicketIF ticketDB;

    public TicketController() {
        ticketDB = new TicketDB();
    }

    public boolean createTicket(Ticket ticket) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = ticketDB.insert(ticket);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while creating a ticket.", e);
        }
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

    public boolean updateVersion(Ticket ticketToUpdate) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = ticketDB.updateVersion(ticketToUpdate);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new DataAccessException("Transaction error while updating a ticket.", e);
        }
    }

    public boolean updateTicket(Ticket ticketToUpdate, String newComplaintStatus, String newPriority,
                                LocalDateTime newStartDate, LocalDateTime newEndDate, Employee newEmployee, Customer newCustomer)
            throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            Ticket ticket = new Ticket();
            ticket.setComplaintStatus(newComplaintStatus)
                    .setPriority(newPriority)
                    .setStartDate(newStartDate)
                    .setEndDate(newEndDate)
                    .setEmployee(newEmployee)
                    .setCustomer(newCustomer)
                    .setVersion(ticketToUpdate.getVersion());
            result = ticketDB.update(ticket);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while updating a ticket.", e);
        }
    }

    public boolean deleteTicket(int ID) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = ticketDB.delete(ID);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while deleting a ticket.", e);
        }
    }

    public boolean checkForUpdates(Ticket currentTicket) {
        boolean currentTicketIsDifferent = false;
        try {
            if (findTicketByID(currentTicket.getTicketID(), false).getVersion() > currentTicket.getVersion()) {
                currentTicketIsDifferent = true;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return currentTicketIsDifferent;
    }
}