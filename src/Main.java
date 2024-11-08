import java.util.Scanner;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Main {

    public static void main(String[] args) {
        final String filePath = "src/university.xml";
        final String inputFilePath = "src/input_university.xml";
        Scanner scanner = new Scanner(System.in);

        try {

            // this part just for testing retrieve data from xml

            // Load the testing XML document
            Document document = loadXMLDocument(filePath);

            // Parse students from the document
            List<Student> students = StudentXMLParser.parseStudents(document.getDocumentElement().getChildNodes());


            for(Student student : students){
                System.out.println(student.toString());
            }


            // Load the real XML document
            Document newDocument = loadXMLDocument(inputFilePath);


            // Allow user to add students
            System.out.print("Enter number of students to add: ");
            int count = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            for (int i = 0; i < count; i++) {
                students.add(getStudents(scanner));
            }

            StudentXMLParser.writeStudentsToXML(students, inputFilePath);

            // Main menu loop
            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1- Show all students");
                System.out.println("2- Get student by GPA");
                System.out.println("3- Get student by first name");
                System.out.println("4- Remove student by ID");
                System.out.println("5- Exit");

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
                        System.out.println("Exiting program.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load XML document once
    private static Document loadXMLDocument(String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(filePath));
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
