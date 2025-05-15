package api.customer;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CustomerGUI extends JFrame {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final JTable customerTable;
    private final DefaultTableModel tableModel;

    public CustomerGUI() {
        setTitle("Customer Management");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tabelle initialisieren
        String[] columnNames = {"ID", "Vorname", "Nachname", "Geburtsdatum", "Geschlecht"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        JButton addButton = new JButton("Hinzufügen");
        JButton updateButton = new JButton("Aktualisieren");
        JButton deleteButton = new JButton("Löschen");
        JButton refreshButton = new JButton("Aktualisieren");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button-Aktionen
        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        refreshButton.addActionListener(e -> loadCustomers());

        loadCustomers();
        setVisible(true);
    }

    private void loadCustomers() {
        try {
            tableModel.setRowCount(0);
            List<Customer> customers = customerDAO.getAllCustomers();
            for (Customer c : customers) {
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getBirthDate(),
                        c.getGender()
                });
            }
        } catch (Exception ex) {
            showError("Fehler beim Laden der Kunden: " + ex.getMessage());
        }
    }

    private void addCustomer() {
        String firstName = JOptionPane.showInputDialog(this, "Vorname:");
        String lastName = JOptionPane.showInputDialog(this, "Nachname:");
        String birthDateStr = JOptionPane.showInputDialog(this, "Geburtsdatum (YYYY-MM-DD):");
        String[] options = {"MALE", "FEMALE", "OTHER"};
        String genderStr = (String) JOptionPane.showInputDialog(this, "Geschlecht:", "Geschlecht",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr);
            Gender gender = Gender.valueOf(genderStr);
            Customer customer = new Customer(null, lastName, firstName, birthDate, gender);
            customerDAO.addCustomer(customer);
            loadCustomers();
        } catch (Exception ex) {
            showError("Fehler beim Hinzufügen: " + ex.getMessage());
        }
    }

    private void updateCustomer() {
        int selected = customerTable.getSelectedRow();
        if (selected == -1) {
            showError("Bitte wähle einen Kunden aus.");
            return;
        }

        try {
            UUID id = UUID.fromString(tableModel.getValueAt(selected, 0).toString());
            Customer customer = customerDAO.getCustomerById(id);

            String newFirstName = JOptionPane.showInputDialog(this, "Vorname:", customer.getFirstName());
            String newLastName = JOptionPane.showInputDialog(this, "Nachname:", customer.getLastName());
            String newBirthDate = JOptionPane.showInputDialog(this, "Geburtsdatum (YYYY-MM-DD):", customer.getBirthDate());
            String[] options = {"MALE", "FEMALE", "OTHER"};
            String newGender = (String) JOptionPane.showInputDialog(this, "Geschlecht:",
                    "Geschlecht", JOptionPane.QUESTION_MESSAGE, null, options, customer.getGender().toString());

            customer.setFirstName(newFirstName);
            customer.setLastName(newLastName);
            customer.setBirthDate(LocalDate.parse(newBirthDate));
            customer.setGender(Gender.valueOf(newGender));
            customerDAO.updateCustomer(customer);
            loadCustomers();
        } catch (Exception ex) {
            showError("Fehler beim Aktualisieren: " + ex.getMessage());
        }
    }

    private void deleteCustomer() {
        int selected = customerTable.getSelectedRow();
        if (selected == -1) {
            showError("Bitte wähle einen Kunden aus.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            UUID id = UUID.fromString(tableModel.getValueAt(selected, 0).toString());
            customerDAO.deleteCustomer(id);
            loadCustomers();
        } catch (Exception ex) {
            showError("Fehler beim Löschen: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}