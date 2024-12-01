import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;

import java.util.List;
public class GUI extends JFrame {

    // to show the output for each action
    private final JTextArea outputArea;

    // 5 buttons that perform each functionality
    private JButton showAllButton,addStudentButton,updateStudentButton,searchButton,sortButton, removeStudentButton;

    // document of the XML file
    private Document newDocument;

    public GUI() {

        // title of the screen
        setTitle("Student Management");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        // Initialize the XML document
        try {
            newDocument = Main.loadXMLDocument("src/input_university.xml");
        } catch (Exception e) {
            showError("Failed to load XML document.");
        }

        // Panel to hold buttons
        JPanel buttonPanel = createButtonPanel();

        // put the buttons in the top
        add(buttonPanel, BorderLayout.NORTH);

        // Output area panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputArea));

        add(outputPanel);

        addListeners();
    }


    /**
     * create the 5 buttons of the App
     *
     * @return the panel that hold the buttons
     */
    private JPanel createButtonPanel() {

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        showAllButton = new JButton("Show All Students");
        showAllButton.setBackground(Color.CYAN);

        addStudentButton = new JButton("Add New Student");
        addStudentButton.setBackground(Color.GREEN);

        updateStudentButton = new JButton("Update Student");
        updateStudentButton.setBackground(Color.ORANGE);

        searchButton = new JButton("Search");
        searchButton.setBackground(Color.YELLOW);

        sortButton = new JButton("Sort");
        sortButton.setBackground(Color.BLUE);

        removeStudentButton = new JButton("Remove Student");
        removeStudentButton.setBackground(Color.RED);

        buttonPanel.add(showAllButton);
        buttonPanel.add(addStudentButton);
        buttonPanel.add(updateStudentButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(removeStudentButton);
        return buttonPanel;
    }

    private void addListeners() {
        showAllButton.addActionListener(e -> showAllStudents());

        // Open form in a new window for adding student
        addStudentButton.addActionListener(e -> {
            StudentFormWindow formWindow = new StudentFormWindow(outputArea, newDocument);
            formWindow.setVisible(true);
        });

//        updateStudentButton.addActionListener();
//        searchButton.addActionListener();
//        sortButton.addActionListener();
        removeStudentButton.addActionListener(e -> removeStudent());
    }

    private void showAllStudents() {
        NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
        List<Student> students = StudentXMLParser.parseStudents(nodeList);

        StringBuilder output = new StringBuilder("All Students:\n");
        output.append("==============================================").append("\n");
        for (Student student : students) {
            output.append(student.toString()).append("\n");
            output.append("==============================================").append("\n");
        }

        outputArea.setText(output.toString());
    }

    private void searchByGPA() {
        String gpaStr = JOptionPane.showInputDialog("Enter GPA:");
        if(gpaStr!=null && !gpaStr.isEmpty()){
            try {
                double gpa = Double.parseDouble(gpaStr);
                NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
                List<Student> students = StudentXMLParser.getStudentByGPA(nodeList, gpa);
                displayStudents("Students with GPA " + gpa + ":", students);
            } catch (NumberFormatException e) {
                showError("Invalid GPA input. Please enter a valid number.");
            }
        }

    }

    private void searchByName() {
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        if(firstName!=null && !firstName.isEmpty()) {

            NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
            List<Student> students = StudentXMLParser.getStudentByFirstName(nodeList, firstName);
            displayStudents("Students with First Name " + firstName + ":", students);
        }
    }

    private void removeStudent() {
        String id = JOptionPane.showInputDialog("Enter Student ID to remove:");
        boolean removed = false;
        try {
            removed = StudentXMLParser.removeStudentById(newDocument, id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if(id!=null){
            outputArea.setText(removed ? "Student with ID " + id + " removed." : "Student with ID " + id + " not found.");

        }
    }

    private void displayStudents(String title, List<Student> students) {
        StringBuilder output = new StringBuilder(title).append("\n");
        output.append("==============================================").append("\n");


        for (Student student : students) {
            output.append(student).append("\n");
            output.append("==============================================").append("\n");


        }
        outputArea.setText(output.toString());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}
