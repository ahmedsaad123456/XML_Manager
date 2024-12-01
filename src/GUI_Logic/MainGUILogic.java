package GUI_Logic;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import student.Student;
import student.StudentXMLParser;

import java.util.List;

public class MainGUILogic {

    private final Document newDocument;

    public MainGUILogic() {
        // Initialize the XML document
        Document documentTemp;
        try {
            documentTemp = StudentXMLParser.loadXMLDocument("src/input_university.xml");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load XML document.", e);
        }
        this.newDocument = documentTemp;
    }

    // =================================================================================================================

    public Document getDocument() {
        return newDocument;
    }

    // =================================================================================================================
    public String showAllStudents() {
        NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
        List<Student> students = StudentXMLParser.parseStudents(nodeList);

        StringBuilder output = new StringBuilder("All Students:\n");
        output.append("==============================================\n");
        for (Student student : students) {
            output.append(student).append("\n");
            output.append("==============================================\n");
        }
        return output.toString();
    }

    // =================================================================================================================

    public String searchByGPA(String gpaStr) {
        if (gpaStr != null && !gpaStr.isEmpty()) {
            try {
                double gpa = Double.parseDouble(gpaStr);
                NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
                List<Student> students = StudentXMLParser.getStudentByGPA(nodeList, gpa);

                return formatStudentsOutput("Students with GPA " + gpa + ":", students);
            } catch (NumberFormatException e) {
                return "Invalid GPA input. Please enter a valid number.";
            }
        }
        return "No GPA provided.";
    }

    // =================================================================================================================

    public String searchByName(String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
            List<Student> students = StudentXMLParser.getStudentByFirstName(nodeList, firstName);

            return formatStudentsOutput("Students with First Name " + firstName + ":", students);
        }
        return "No name provided.";
    }

    // =================================================================================================================

    public String removeStudent(String id) {
        if (id != null && !id.isEmpty()) {
            try {
                boolean removed = StudentXMLParser.removeStudentById(newDocument, id);
                return removed ? "Student with ID " + id + " removed." : "Student with ID " + id + " not found.";
            } catch (Exception ex) {
                return "Error removing student: " + ex.getMessage();
            }
        }
        return "No ID provided.";
    }

    // =================================================================================================================

    private String formatStudentsOutput(String title, List<Student> students) {
        StringBuilder output = new StringBuilder(title).append("\n");
        output.append("==============================================\n");
        for (Student student : students) {
            output.append(student).append("\n");
            output.append("==============================================\n");
        }
        return output.toString();
    }
}
