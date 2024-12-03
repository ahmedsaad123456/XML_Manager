package GUI;

import javax.swing.*;
import java.awt.*;
import GUI_Logic.MainGUILogic;

public class UpdateStudentWindow extends JFrame {
    private final JTextField searchField, firstNameField, lastNameField, gpaField, levelField, genderField, addressField;
    private final JButton searchButton, updateFirstNameButton, updateLastNameButton, updateGPAButton, updateLevelButton, updateGenderButton, updateAddressButton;
    private final JTextArea outputArea;

    public UpdateStudentWindow(MainGUILogic guiLogic) {
        setTitle("Update Student");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(9, 3, 5, 5));


        add(new JLabel("Search by Student ID:"));
        searchField = new JTextField();
        add(searchField);
        searchButton = new JButton("Search");
        add(searchButton);

        // Output area for displaying student details
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(600, 150));
        add(scrollPane);
        add(new JLabel());
        add(new JLabel());

        // Form fields and update buttons
        add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        add(firstNameField);
        updateFirstNameButton = new JButton("Update");
        add(updateFirstNameButton);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        add(lastNameField);
        updateLastNameButton = new JButton("Update");
        add(updateLastNameButton);

        add(new JLabel("GPA:"));
        gpaField = new JTextField();
        add(gpaField);
        updateGPAButton = new JButton("Update");
        add(updateGPAButton);

        add(new JLabel("Level:"));
        levelField = new JTextField();
        add(levelField);
        updateLevelButton = new JButton("Update");
        add(updateLevelButton);

        add(new JLabel("Gender:"));
        genderField = new JTextField();
        add(genderField);
        updateGenderButton = new JButton("Update");
        add(updateGenderButton);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);
        updateAddressButton = new JButton("Update");
        add(updateAddressButton);

        // listeners
        searchButton.addActionListener(e -> {
            String id = searchField.getText().trim();
            String details = guiLogic.getStudentDetailsByID(id);
            if (details.isEmpty()) {
                outputArea.setText("Student not found.");
            } else {
                outputArea.setText(details);
            }
        });

        // Update button actions
        updateFirstNameButton.addActionListener(e -> updateAttribute(guiLogic, "FirstName", firstNameField));
        updateLastNameButton.addActionListener(e -> updateAttribute(guiLogic, "LastName", lastNameField));
        updateGPAButton.addActionListener(e -> updateAttribute(guiLogic, "GPA", gpaField));
        updateLevelButton.addActionListener(e -> updateAttribute(guiLogic, "Level", levelField));
        updateGenderButton.addActionListener(e -> updateAttribute(guiLogic, "Gender", genderField));
        updateAddressButton.addActionListener(e -> updateAttribute(guiLogic, "Address", addressField));
    }

    private void updateAttribute(MainGUILogic guiLogic, String attribute, JTextField textField) {
        String id = searchField.getText().trim();
        String newValue = textField.getText().trim();
        if (id.isEmpty() || newValue.isEmpty()) {
            outputArea.setText("Please provide a valid student ID and value.");
            return;
        }
        String result = guiLogic.updateStudent(id, attribute, newValue);
        outputArea.setText(result);
    }
}
