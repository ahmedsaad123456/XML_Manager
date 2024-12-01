package GUI_Logic;

import exception.DuplicateStudentIDException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import student.Student;
import student.StudentXMLParser;

import java.util.Comparator;
import java.util.List;

public class MainGUILogic {

    private Document newDocument;

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

    public String searchByfName(String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
            List<Student> students = StudentXMLParser.getStudentByFirstName(nodeList, firstName);

            return formatStudentsOutput("Students with First Name " + firstName + ":", students);
        }
        return "No first name provided.";
    }

    // =================================================================================================================

    public String searchByID(String idInput) {
        if (idInput != null && !idInput.isEmpty()) {
                NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
                List<Student> students = StudentXMLParser.getStudentByID(nodeList, idInput);

                return formatStudentsOutput("Students with ID " + idInput + ":", students);
        }
        return "No ID provided.";
    }

    // =================================================================================================================

    public String searchBylName(String lastName) {
        if (lastName != null && !lastName.isEmpty()) {
            NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
            List<Student> students = StudentXMLParser.getStudentByLastName(nodeList, lastName);

            return formatStudentsOutput("Students with Last Name " + lastName + ":", students);
        }
        return "No last name provided.";
    }

    // =================================================================================================================

    public String searchByGender(String gender) {
        if (gender != null && !gender.isEmpty()) {
            NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
            List<Student> students = StudentXMLParser.getStudentByGender(nodeList, gender);

            return formatStudentsOutput("Students with gender " + gender + ":", students);
        }
        return "No gender provided.";
    }


    // =================================================================================================================

        public String searchByAddress(String address) {
            if (address != null && !address.isEmpty()) {
                NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
                List<Student> students = StudentXMLParser.getStudentByAddress(nodeList, address);

                return formatStudentsOutput("Students with address " + address + ":", students);
            }
            return "No address provided.";
        }
    // ================================================================
    public String searchByLevel(String levelStr) {
        if (levelStr != null && !levelStr.isEmpty()) {
            try {
                Integer level = Integer.parseInt(levelStr);
                NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
                List<Student> students = StudentXMLParser.getStudentByLevel(nodeList, level);

                return formatStudentsOutput("Students with Level " + level + ":", students);
            } catch (NumberFormatException e) {
                return "Invalid Level input. Please enter a valid number.";
            }
        }
        return "No level provided.";
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
        int len = students.size();
        output.append("==============================================\n");
        output.append("Total Results: ").append(len).append(" students").append("\n");
        output.append("==============================================\n");
        for (Student student : students) {
            output.append(student).append("\n");
            output.append("==============================================\n");
        }
        return output.toString();
    }

    // =================================================================================================================

    public String sortStudents(String field, boolean isDescending) throws DuplicateStudentIDException {
        NodeList nodeList = newDocument.getDocumentElement().getChildNodes();
        List<Student> students = StudentXMLParser.parseStudents(nodeList);

        Comparator<Student> comparator;

        // Determine the comparator based on the field
        switch (field.toLowerCase()) {
            case "id":
                comparator = Comparator.comparingInt(student -> Integer.parseInt(student.ID()));
                break;
            case "first name":
                comparator = Comparator.comparing(Student::firstName);
                break;
            case "last name":
                comparator = Comparator.comparing(Student::lastName);
                break;
            case "gpa":
                comparator = Comparator.comparingDouble(Student::gpa);
                break;
            case "gender":
                comparator = Comparator.comparing(Student::gender);
                break;
            case "level":
                comparator = Comparator.comparingInt(Student::level);
                break;
            case "address":
                comparator = Comparator.comparing(Student::address);
                break;
            default:
                return "Invalid sort field: " + field;
        }

        // Reverse comparator for descending order if needed
        if (isDescending) {
            comparator = comparator.reversed();
        }

        // Sort the list
        students.sort(comparator);

        newDocument =  StudentXMLParser.writeStudentsToXML(students , newDocument , true);

        return sortOutputBuilder(students);
    }

    // =================================================================================================================
    public String sortOutputBuilder(List<Student> students){
        StringBuilder output = new StringBuilder("All Students:\n");
        output.append("==============================================\n");
        for (Student student : students) {
            output.append(student).append("\n");
            output.append("==============================================\n");
        }
        return output.toString();
    }
}
