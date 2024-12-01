import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;


import exception.DuplicateStudentIDException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import student.Student;
import student.StudentXMLParser;

public class Main {

    public static void main(String[] args) {
        final String inputFilePath = "src/input_university.xml";
        Scanner scanner = new Scanner(System.in);

        try {
            // Load the real XML document
            Document newDocument = StudentXMLParser.loadXMLDocument(inputFilePath);


            List<Student> students = new ArrayList<>();
            // Allow user to add students
            System.out.print("Enter number of students to add: ");
            int count = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            for (int i = 0; i < count; i++) {
                students.add(getStudents(scanner));
            }

            StudentXMLParser.writeStudentsToXML(students, newDocument , false);

            // Main menu loop
            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1- Show all students");
                System.out.println("2- Get student by GPA");
                System.out.println("3- Get student by first name");
                System.out.println("4- Remove student by ID");
                System.out.println("5- search by id");
                System.out.println("6- search by last name");
                System.out.println("7- search by gender");
                System.out.println("8- search by level");
                System.out.println("9- search by address");
                System.out.println("10- Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        showAllStudents(newDocument);
                        break;
                    case 2:
                        System.out.print("Enter GPA : ");
                        double gpa = scanner.nextDouble();
                        scanner.nextLine();
                        searchByGPA(newDocument, gpa);
                        break;
                    case 3:
                        System.out.print("Enter first name: ");
                        String firstName = scanner.nextLine();
                        searchByFirstName(newDocument, firstName);
                        break;
                    case 4:
                        System.out.print("Enter student ID: ");
                        String id = scanner.nextLine();
                        removeStudentById(newDocument, id);
                        break;
                    case 5:
                        System.out.println("enter id ");
                        String idd = scanner.nextLine();
                        searchByID(newDocument, idd);
                        break;
                    case 6:
                        System.out.println("enter last name ");
                        String last = scanner.nextLine();
                        searchByLastName(newDocument, last);
                        break;
                    case 7:
                        System.out.println("enter gender ");
                        String gen = scanner.nextLine();
                        searchByGender(newDocument, gen);
                        break;
                    case 8:
                        System.out.println("enter level ");
                        int lev = scanner.nextInt();
                        searchByLevel(newDocument, lev);
                        break;
                    case 9:
                        System.out.println("enter address ");
                        String add = scanner.nextLine();
                        searchByAddress(newDocument, add);
                        break;
                    case 10:
                        System.out.println("Exiting program.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        catch (DuplicateStudentIDException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Method to get students from the user
    private static Student getStudents(Scanner scanner) {
        System.out.print("Enter ID: ");
        String ID = scanner.nextLine();
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter GPA: ");
        double gpa = scanner.nextDouble();
        System.out.print("Enter Level: ");
        int level = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        return new Student(ID, firstName, lastName, gender, gpa, level, address);
    }

    // Method to show all students
    private static void showAllStudents(Document document) throws Exception {
        List<Student> students = StudentXMLParser.parseStudents(document.getDocumentElement().getChildNodes());
        System.out.println("=======================================");
        for (Student student : students) {
            System.out.println(student.toString());
        }
        System.out.println("=======================================");

    }

    // Method to search students by GPA
    private static void searchByGPA(Document document, double gpa) throws Exception {
        NodeList nodeList = document.getElementsByTagName("Student");
        List<Student> students = StudentXMLParser.getStudentByGPA(nodeList, gpa);
        int len= students.size();
        System.out.println("found "+ len + " students");
        System.out.println("=======================================");
        for (Student student : students) {
            System.out.println(student.toString());
        }

        // show message if there is no students returned
        if(students.isEmpty()){
            System.out.println("Student with GPA " + gpa + " not found.");
        }
        System.out.println("=======================================");

    }

    // Method to search students by first name
    private static void searchByFirstName(Document document, String firstName) throws Exception {
        NodeList nodeList = document.getElementsByTagName("Student");
        List<Student> students = StudentXMLParser.getStudentByFirstName(nodeList, firstName);
        System.out.println("=======================================");
        for (Student student : students) {
            System.out.println(student);
        }
        // show message if there is no students returned
        if(students.isEmpty()){
            System.out.println("Student with first name " + firstName + " not found.");
        }
        System.out.println("=======================================");

    }

    private static void searchByLastName(Document document, String lastName) throws Exception {
        NodeList nodeList = document.getElementsByTagName("Student");
        List<Student> students = StudentXMLParser.getStudentByLastName(nodeList, lastName);
        int len= students.size();
        System.out.println("found "+ len + " students");
        System.out.println("=======================================");
        for (Student student : students) {
            System.out.println(student.toString());
        }

        // show message if there is no students returned
        if(students.isEmpty()){
            System.out.println("Student with lastName " + lastName + " not found.");
        }
        System.out.println("=======================================");

    }

    private static void searchByID(Document document, String id) throws Exception {
        NodeList nodeList = document.getElementsByTagName("Student");
        List<Student> students = StudentXMLParser.getStudentByID(nodeList, id);
        int len= students.size();
        System.out.println("found "+ len + " students");
        System.out.println("=======================================");
        for (Student student : students) {
            System.out.println(student.toString());
        }

        // show message if there is no students returned
        if(students.isEmpty()){
            System.out.println("Student with id " + id + " not found.");
        }
        System.out.println("=======================================");

    }

    private static void searchByGender(Document document, String gender) throws Exception {
        NodeList nodeList = document.getElementsByTagName("Student");
        List<Student> students = StudentXMLParser.getStudentByGender(nodeList, gender);
        int len= students.size();
        System.out.println("found "+ len + " students");
        System.out.println("=======================================");
        for (Student student : students) {
            System.out.println(student.toString());
        }

        // show message if there is no students returned
        if(students.isEmpty()){
            System.out.println("Student with gender " + gender + " not found.");
        }
        System.out.println("=======================================");

    }

    private static void searchByLevel(Document document, int level) throws Exception {
        NodeList nodeList = document.getElementsByTagName("Student");
        List<Student> students = StudentXMLParser.getStudentByLevel(nodeList, level);
        int len= students.size();
        System.out.println("found "+ len + " students");
        System.out.println("=======================================");
        for (Student student : students) {
            System.out.println(student.toString());
        }

        // show message if there is no students returned
        if(students.isEmpty()){
            System.out.println("Student with level " + level + " not found.");
        }
        System.out.println("=======================================");

    }

    private static void searchByAddress(Document document, String address) throws Exception {
        NodeList nodeList = document.getElementsByTagName("Student");
        List<Student> students = StudentXMLParser.getStudentByAddress(nodeList, address);
        int len= students.size();
        System.out.println("found "+ len + " students");
        System.out.println("=======================================");
        for (Student student : students) {
            System.out.println(student.toString());
        }

        // show message if there is no students returned
        if(students.isEmpty()){
            System.out.println("Student with address " + address + " not found.");
        }
        System.out.println("=======================================");

    }


    // Method to remove a student by ID
    private static void removeStudentById(Document document, String id) throws Exception {
        boolean found = StudentXMLParser.removeStudentById(document, id);

        if (found) {
            System.out.println("=======================================");
            System.out.println("Student with ID " + id + " has been removed.");
            System.out.println("=======================================");
        } else {
            System.out.println("=======================================");
            System.out.println("Student with ID " + id + " not found.");
            System.out.println("=======================================");
        }
    }


}
