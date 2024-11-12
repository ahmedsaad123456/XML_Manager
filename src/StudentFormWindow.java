import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.w3c.dom.Document;
import java.util.List;

public class StudentFormWindow extends JFrame {
    private JTextField idField, firstNameField, lastNameField, genderField, gpaField, levelField, addressField;
    private final JTextArea outputArea;
    private Document newDocument;

    public StudentFormWindow(JTextArea outputArea, Document newDocument) {
        this.outputArea = outputArea;
        this.newDocument = newDocument;

        setTitle("Add New Student");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window only
        setLocationRelativeTo(null); // Center window
        setLayout(new BorderLayout());

        // Add the form to take the input
        add(createFormPanel());
        // Add submit button
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        // Initialize form fields and apply custom styling
        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        customizeTextField(idField);
        formPanel.add(idField);

        formPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        customizeTextField(firstNameField);
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        customizeTextField(lastNameField);
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("Gender:"));
        genderField = new JTextField();
        customizeTextField(genderField);
        formPanel.add(genderField);

        formPanel.add(new JLabel("GPA:"));
        gpaField = new JTextField();
        customizeTextField(gpaField);
        formPanel.add(gpaField);

        formPanel.add(new JLabel("Level:"));
        levelField = new JTextField();
        customizeTextField(levelField);
        formPanel.add(levelField);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        customizeTextField(addressField);
        formPanel.add(addressField);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.green);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        buttonPanel.add(submitButton);
        return buttonPanel;
    }

    private void addStudent() {
        try {
            String id = idField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String gender = genderField.getText();
            double gpa = Double.parseDouble(gpaField.getText());
            int level = Integer.parseInt(levelField.getText());
            String address = addressField.getText();

            Student newStudent = new Student(id, firstName, lastName, gender, gpa, level, address);
            StudentXMLParser.writeStudentsToXML(List.of(newStudent), newDocument);

            newDocument = Main.loadXMLDocument("src/input_university.xml");
            outputArea.setText("Added new student:\n" + newStudent);
            dispose();  // Close the form window after submission
        }
        catch (DuplicateStudentIDException e) {

            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Failed to add student. Please ensure all inputs are valid.", "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void customizeTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.DARK_GRAY);
        textField.setBackground(Color.WHITE);

        // Set padding inside the text field
        Border outerBorder = BorderFactory.createLineBorder( Color.green.darker(), 2);
        Border innerBorder = new EmptyBorder(5, 10, 5, 10);
        textField.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
    }
}
