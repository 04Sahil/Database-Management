import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MusicConcertRegistrationUI extends JFrame {
    private JTextField nameField, ticketsField, dateField, phoneField, emailField;
    private JTable dataTable;
    private JButton registerButton, clearButton, removeButton, updateButton, displayButton;
    private DefaultTableModel tableModel;

    public MusicConcertRegistrationUI() {
        setTitle("Music Concert Registration");
        setSize(700, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 248, 220)); // Cornsilk

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.setBackground(new Color(255, 228, 196)); // LightGoldenrodYellow
        nameField = new JTextField(20);
        ticketsField = new JTextField(20);
        dateField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("No. of Tickets:"));
        inputPanel.add(ticketsField);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Phone No.:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email ID:"));
        inputPanel.add(emailField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(173, 216, 230)); // LightSkyBlue
        registerButton = new JButton("Register");
        clearButton = new JButton("Clear");
        removeButton = new JButton("Remove");
        updateButton = new JButton("Update");
        displayButton = new JButton("Display Data");

        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(displayButton);

        // Table model and data table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Tickets");
        tableModel.addColumn("Date");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Email");

        dataTable = new JTable(tableModel);
        dataTable.setBackground(new Color(255, 250, 205)); // LemonChiffon
        JScrollPane scrollPane = new JScrollPane(dataTable);

        // Display panel
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(new Color(255, 228, 225)); // MistyRose
        displayPanel.add(buttonPanel, BorderLayout.NORTH);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);

        // Buttons' background colors
        registerButton.setBackground(new Color(50, 205, 50)); // LimeGreen
        clearButton.setBackground(new Color(255, 69, 0)); // Red-Orange
        removeButton.setBackground(new Color(255, 0, 0)); // Red
        updateButton.setBackground(new Color(0, 191, 255)); // DeepSkyBlue
        displayButton.setBackground(new Color(255, 140, 0)); // DarkOrange

        // Action listeners for buttons
        registerButton.addActionListener(e -> register());
        clearButton.addActionListener(e -> clearFields());
        removeButton.addActionListener(e -> removeRecord());
        updateButton.addActionListener(e -> updateRecord());
        displayButton.addActionListener(e -> displayData());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void register() {
        String name = nameField.getText();
        int tickets = Integer.parseInt(ticketsField.getText());
        String date = dateField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        // Database connectivity
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Music", "root", "Sonic#09");

            String query = "INSERT INTO concert_registration (name, tickets, date, phone, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, tickets);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, email);

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!");
            connection.close();

            // Refresh the table
            displayData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in registration: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        ticketsField.setText("");
        dateField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    private void removeRecord() {
        String name = nameField.getText();

        // Database connectivity
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Music", "root", "Sonic#09");

            String query = "DELETE FROM concert_registration WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Record removed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found with the given name.");
            }

            connection.close();

            // Refresh the table
            displayData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in removing record: " + ex.getMessage());
        }
    }

    private void updateRecord() {
        String name = nameField.getText();
        int tickets = Integer.parseInt(ticketsField.getText());
        String date = dateField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        // Database connectivity
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Music", "root", "Sonic#09");

            String query = "UPDATE concert_registration SET tickets = ?, date = ?, phone = ?, email = ? WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, tickets);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, name);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found with the given name.");
            }

            connection.close();

            // Refresh the table
            displayData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in updating record: " + ex.getMessage());
        }
    }

    private void displayData() {
        // Clear the existing data in the table
        tableModel.setRowCount(0);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Music", "root", "Sonic#09");

            String query = "SELECT * FROM concert_registration";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String[] rowData = {
                        resultSet.getString("name"),
                        String.valueOf(resultSet.getInt("tickets")),
                        resultSet.getString("date"),
                        resultSet.getString("phone"),
                        resultSet.getString("email")
                };
                tableModel.addRow(rowData);
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in displaying data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicConcertRegistrationUI::new);
    }
}
