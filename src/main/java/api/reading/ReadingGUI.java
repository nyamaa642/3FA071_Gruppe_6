package api.reading;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ReadingGUI extends JFrame {
    private final ReadingDAO readingDao = new ReadingDAO();
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable readingTable = new JTable(tableModel);

    private JTextField customerIdField;
    private JComboBox<TypeOfMeter> typeCombo;
    private JTextField readingField;
    private UUID selectedReadingId;

    public ReadingGUI() {
        initializeUI();
        setupTable();
        loadReadings();
    }

    private void initializeUI() {
        setTitle("Reading Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = createInputPanel();

        // Action Buttons
        JPanel buttonPanel = createButtonPanel();

        // Table setup
        readingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        readingTable.getSelectionModel().addListSelectionListener(this::handleTableSelection);

        // Layout organization
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(readingTable), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        customerIdField = new JTextField();
        typeCombo = new JComboBox<>(TypeOfMeter.values());
        readingField = new JTextField();

        panel.add(new JLabel("Customer UUID:"));
        panel.add(customerIdField);
        panel.add(new JLabel("Meter Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Reading Value:"));
        panel.add(readingField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));

        JButton addButton = createStyledButton("Add", Color.GREEN, this:: addReading);
        JButton updateButton = createStyledButton("Update", Color.ORANGE, this:: updateReading);
        JButton deleteButton = createStyledButton("Delete", Color.RED, this:: deleteReading);
        JButton refreshButton = createStyledButton("Refresh", Color.BLUE, this:: loadReadings);


        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(refreshButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color color, Runnable action) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void setupTable() {
        tableModel.addColumn("Reading ID");
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Meter Type");
        tableModel.addColumn("Value");
        readingTable.setAutoCreateRowSorter(true);
    }

    private void loadReadings() {
        try {
            List<Reading> readings = readingDao.getAllReadings();
            tableModel.setRowCount(0);

            for (Reading reading : readings) {
                tableModel.addRow(new Object[]{
                        reading.getId(),
                        reading.getCustomerID(),
                        reading.getType(),
                        String.format("%.2f", reading.getReading())
                });
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private void handleTableSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && readingTable.getSelectedRow() != -1) {
            int row = readingTable.convertRowIndexToModel(readingTable.getSelectedRow());
            selectedReadingId = (UUID) tableModel.getValueAt(row, 0);
            customerIdField.setText(tableModel.getValueAt(row, 1).toString());
            typeCombo.setSelectedItem(tableModel.getValueAt(row, 2));
            readingField.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    private void addReading() {
        try {
            Reading reading = validateAndCreateReading();
            readingDao.addReading(reading);
            loadReadings();
            clearFields();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateReading() {
        if (selectedReadingId == null) {
            showError("No reading selected!");
            return;
        }

        try {
            Reading reading = validateAndCreateReading();
            reading.setId(selectedReadingId);
            readingDao.updateReading(reading);
            loadReadings();
            clearFields();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteReading() {
        if (selectedReadingId == null) {
            showError("No reading selected!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected reading?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                readingDao.deleteReading(selectedReadingId);
                loadReadings();
                clearFields();
            } catch (SQLException e) {
                showError("Database error: " + e.getMessage());
            }
        }
    }

    private Reading validateAndCreateReading() throws IllegalArgumentException {
        String customerId = customerIdField.getText().trim();
        String readingValue = readingField.getText().trim();

        if (customerId.isEmpty() || readingValue.isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled!");
        }

        try {
            UUID.fromString(customerId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for Customer ID");
        }

        try {
            Reading reading = new Reading();
            reading.setCustomerID(UUID.fromString(customerId));
            reading.setType((TypeOfMeter) typeCombo.getSelectedItem());
            reading.setReading(Double.parseDouble(readingValue));
            return reading;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for reading value");
        }
    }

    private void clearFields() {
        selectedReadingId = null;
        customerIdField.setText("");
        typeCombo.setSelectedIndex(0);
        readingField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
