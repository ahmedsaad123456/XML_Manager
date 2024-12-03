package GUI;

import javax.swing.*;
import java.awt.*;
import org.w3c.dom.Document;
import GUI_Logic.MainGUILogic;

public class UpdateStudentWindow extends JFrame {
    private final JTextField idField, firstNameField, lastNameField, gpaField, levelField, genderField, addressField;
    private final JButton updateButton;

    public UpdateStudentWindow(MainGUILogic guiLogic, JTextArea outputArea) {
        setTitle("Update Student");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        // Form fields
        add(new JLabel("Student ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        add(firstNameField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("GPA:"));
        gpaField = new JTextField();
        add(gpaField);

        add(new JLabel("Level:"));
        levelField = new JTextField();
        add(levelField);

        add(new JLabel("Gender:"));
        genderField = new JTextField();
        add(genderField);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        // Update button
        updateButton = new JButton("Update Student");
        add(updateButton);

        updateButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String gpa = gpaField.getText().trim();
            String level = levelField.getText().trim();
            String gender = genderField.getText().trim();
            String address = addressField.getText().trim();

            String result = guiLogic.updateStudent(id, firstName, lastName, gpa, level, gender, address);
            outputArea.setText(result);
            if (!result.toLowerCase().contains("error")) {
                dispose();
            }
        });
    }
}
