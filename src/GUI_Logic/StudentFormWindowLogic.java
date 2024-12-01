package GUI_Logic;

import org.w3c.dom.Document;
import student.Student;
import student.StudentXMLParser;
import exception.DuplicateStudentIDException;
import exception.InvalidGPAException;
import exception.InvalidNameOrAddressException;

import java.util.List;

public class StudentFormWindowLogic {
    private final Document newDocument;
    private Student latestStudent;

    public StudentFormWindowLogic(Document newDocument) {
        this.newDocument = newDocument;
    }

    // =================================================================================================================

    public void addStudent(String id, String firstName, String lastName, String gender, String gpaStr, String levelStr, String address)
            throws DuplicateStudentIDException, InvalidNameOrAddressException, InvalidGPAException {
        // Check if any field is empty
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || gpaStr.isEmpty() || levelStr.isEmpty() || address.isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled.");
        }

        // Validate name and address
        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+") || !address.matches("[a-zA-Z]+")) {
            throw new InvalidNameOrAddressException("Name and address must contain only alphabetic characters.");
        }

        // Validate GPA
        double gpa;
        try {
            gpa = Double.parseDouble(gpaStr);
            if (gpa < 0 || gpa > 4) {
                throw new InvalidGPAException("GPA must be between 0 and 4.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidGPAException("GPA must be a valid number between 0 and 4.");
        }

        int level = Integer.parseInt(levelStr);

        Student newStudent = new Student(id, firstName, lastName, gender, gpa, level, address);
        StudentXMLParser.writeStudentsToXML(List.of(newStudent), newDocument , false);
        latestStudent = newStudent;
    }


    // =================================================================================================================
    public Student getLatestStudent() {
        return latestStudent;
    }
}
