package gui;

import controller.CustomerController;
import controller.EmployeeController;
import controller.TicketController;
import db.DBConnection;
import db.DataAccessException;
import model.Employee;
import model.Ticket;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MainMenu extends JFrame {

    /**
     * Reference variables
     */
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTable ticketsTable;

    private TicketController ticketController;
    private CustomerController customerController;
    private EmployeeController employeeController;

    private DefaultTableModel ticketTableModel;

    private JLabel priorityLabel;
    private JLabel statusLabel;
    private JLabel customerLabel;
    private JLabel employeeLabel;

    private JComboBox<String> priorityDropdown;
    private JComboBox<String> statusDropdown;
    private JComboBox<String> customerDropdown;
    private JComboBox<String> employeeDropdown;

    private JButton filterButton;

    boolean justMyTickets = false;

    {
        DBConnection.connect();
        ticketController = new TicketController();
        customerController = new CustomerController();
        employeeController = new EmployeeController();

        ticketTableModel = new DefaultTableModel(new Object[][]{},
                new String[]{"TicketID", "Title", "Customer", "Employee", "Priority", "Status"}) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = {false, false, false, false, false, false};

            @Override
            public boolean isCellEditable(int row, int column) {
                return canEdit[column];
            }
        };

        ticketsTable = new JTable(ticketTableModel);
    }

    /**
     * Launch the application.
     */
    public static void start(boolean justMyTickets) {
        EventQueue.invokeLater(() -> {
            try {
                MainMenu frame = new MainMenu(justMyTickets);
                LoginMenu.setCurrentMenu(null);
                frame.setTitle("Main Menu");
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainMenu(boolean justMyTickets) {
        this.justMyTickets = justMyTickets;
        init();
        initTicketTable();
        createGUI();
    }

    private void init() {
        mainPanel = new JPanel();
        scrollPane = new JScrollPane(ticketsTable);
        ticketsTable.getTableHeader().setReorderingAllowed(false);

        priorityLabel = new JLabel("Priority:");
        statusLabel = new JLabel("Status:");
        customerLabel = new JLabel("Customer:");
        employeeLabel = new JLabel("Employee:");

        priorityDropdown = new JComboBox<>(Arrays.asList(Ticket.Priority.values()).stream().map(e -> e.getTitle()).collect(Collectors.toCollection(ArrayList::new)).toArray(String[]::new));
        priorityDropdown.addItem("none");
        priorityDropdown.setSelectedItem("none");

        statusDropdown = new JComboBox<>(Arrays.asList(Ticket.TicketComplaintStatus.values()).stream().map(e -> e.getTitle()).collect(Collectors.toCollection(ArrayList::new)).toArray(String[]::new));
        statusDropdown.addItem("none");
        statusDropdown.setSelectedItem("none");

        try {
            if (LoginMenu.getLoggedEmployee().getEmployeeType() == Employee.EmployeeType.MANAGER && !justMyTickets) {
                employeeDropdown = new JComboBox<>(employeeController.getAllEmployees(false).stream().map(e -> e.getEmail()).toArray(String[]::new));
            } else
                employeeDropdown = new JComboBox<>(new String[]{LoginMenu.getLoggedEmployee().getEmail()});
            employeeDropdown.addItem("none");
            employeeDropdown.setSelectedItem("none");

            customerDropdown = new JComboBox<>(customerController.getAllCustomers(false).stream().map(e -> e.getEmail()).toArray(String[]::new));
            customerDropdown.addItem("none");
            customerDropdown.setSelectedItem("none");
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            clearTicketTable();
            ArrayList<Ticket> currentTickets = new ArrayList<>();
            try {
                if (LoginMenu.getLoggedEmployee().getEmployeeType() == Employee.EmployeeType.MANAGER && !justMyTickets)
                    currentTickets = new ArrayList<>(ticketController.getAllTickets(true));
                else
                    currentTickets = new ArrayList<>(ticketController.findTicketsByEmployee(LoginMenu.getLoggedEmployee(), true));
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }

            if (!priorityDropdown.getSelectedItem().equals("none")) {
                currentTickets = new ArrayList<>(currentTickets.stream()
                        .filter(t -> t.getPriority().getTitle().equals(priorityDropdown.getSelectedItem()))
                        .collect(Collectors.toCollection(ArrayList::new)));
            }

            if (!statusDropdown.getSelectedItem().equals("none")) {
                currentTickets = new ArrayList<>(currentTickets.stream()
                        .filter(t -> t.getComplaintStatus().getTitle().equals(statusDropdown.getSelectedItem()))
                        .collect(Collectors.toCollection(ArrayList::new)));
            }

            if (!employeeDropdown.getSelectedItem().equals("none")) {
                currentTickets = new ArrayList<>(currentTickets.stream()
                        .filter(t -> t.getEmployee().getEmail().equals(priorityDropdown.getSelectedItem()))
                        .collect(Collectors.toCollection(ArrayList::new)));
            }

            if (!customerDropdown.getSelectedItem().equals("none")) {
                currentTickets = new ArrayList<>(currentTickets.stream()
                        .filter(t -> t.getCustomer().getEmail().equals(customerDropdown.getSelectedItem()))
                        .collect(Collectors.toCollection(ArrayList::new)));
            }

            currentTickets.forEach(x -> ticketTableModel.addRow(new String[]{Integer.toString(x.getTicketID()),
                    x.getInquiries().size() > 0 ? x.getInquiries().get(x.getInquiries().size() - 1).getTitle() : "Ticket can't exist without at least one inquiry!!!",
                    x.getCustomer() != null ? x.getCustomer().getEmail() : "unknown",
                    x.getEmployee() != null ? x.getEmployee().getEmail() : "unknown",
                    x.getPriority().getTitle(),
                    x.getComplaintStatus().getTitle()}));

            System.out.println("Filter used.");
            System.out.println(currentTickets.size());
        });

        ticketsTable.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Ticket ticket = getSelectedTicket();
                if (ticket != null) {
                    TicketMenu.start(ticket, true);
                }
            }
        });
    }

    private void setWindowSizeAndLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 1.5;
        double height = screenSize.getHeight() / 1.5;
        screenSize.setSize(width, height);

        setPreferredSize(screenSize);
        setLocation((int) width / 4, (int) height / 4);
    }

    private void createGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        MenuUtil.setWindowSizeAndLocation(this);

        mainPanel.setLayout(new MigLayout());
        mainPanel.add(MenuBar.getInstance().getMainPanel(), "wrap, span, grow");

        mainPanel.add(priorityLabel, "split 2");
        mainPanel.add(priorityDropdown);
        mainPanel.add(employeeLabel, "split 2");
        mainPanel.add(employeeDropdown, "wrap");
        mainPanel.add(statusLabel, "split 2");
        mainPanel.add(statusDropdown);
        mainPanel.add(customerLabel, "split 2");
        mainPanel.add(customerDropdown);
        mainPanel.add(filterButton, "wrap");

        mainPanel.add(scrollPane, "grow, push, span");

        pack();
    }

    private void initTicketTable() {
        clearTicketTable();
        ArrayList<Ticket> tickets = new ArrayList<>();
        try {
            if (LoginMenu.getLoggedEmployee().getEmployeeType() == Employee.EmployeeType.MANAGER && !justMyTickets) {
                tickets = new ArrayList<>(ticketController.getAllTickets(true));
            } else
                tickets = new ArrayList<>(ticketController.findTicketsByEmployee(LoginMenu.getLoggedEmployee(), true));
            tickets.forEach((x) -> ticketTableModel.addRow(new String[]
                    {
                            Integer.toString(x.getTicketID()),
                            x.getInquiries().size() > 0 ? x.getInquiries().get(x.getInquiries().size() - 1).getTitle() : "Ticket can't exist without at least one inquiry!!!",
                            x.getCustomer() != null ? x.getCustomer().getEmail() : "unknown",
                            x.getEmployee() != null ? x.getEmployee().getEmail() : "unknown", x.getPriority().getTitle(),
                            x.getComplaintStatus().getTitle()
                    }));
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void clearTicketTable() {
        ticketTableModel.setRowCount(0);
//        ticketTableModel.getDataVector().removeAllElements();
    }

    private Ticket getSelectedTicket() {
        Ticket ticket = null;
        int row = ticketsTable.getSelectedRow();
        if (row > -1) {
            int ticketID = Integer.parseInt((String) ticketsTable.getValueAt(row, 0));
            try {
                ticket = ticketController.findTicketByID(ticketID, true);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return ticket;
    }
}
