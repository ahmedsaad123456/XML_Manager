package student;

import exception.DuplicateStudentIDException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StudentXMLParser {


    /**
     * load xml file
     *
     * @param filePath path of the file
     * @return the document of the xml file
     * @throws Exception when causing any errors
     *
     */
    public static Document loadXMLDocument(String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(filePath));
    }
    // =================================================================================================================

    /**
     * Parses a NodeList of student nodes and creates a list of Student objects.
     *
     * @param nodeList The NodeList containing XML nodes.
     * @return A list of Student objects.
     */
    public static List<Student> parseStudents(NodeList nodeList) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Student student = fromNode(node);
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }

    // =================================================================================================================


    /**
     * Creates a Student object from an XML Node.
     *
     * @param node The XML Node containing student data.
     * @return Student object or null if the node is not an element node.
     */
    private static Student fromNode(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) node;
            String ID = node.getAttributes().getNamedItem("ID").getNodeValue();
            String firstName = elem.getElementsByTagName("FirstName").item(0).getTextContent();
            String lastName = elem.getElementsByTagName("LastName").item(0).getTextContent();
            String gender = elem.getElementsByTagName("Gender").item(0).getTextContent();
            double gpa = Double.parseDouble(elem.getElementsByTagName("GPA").item(0).getTextContent());
            int level = Integer.parseInt(elem.getElementsByTagName("Level").item(0).getTextContent());
            String address = elem.getElementsByTagName("Address").item(0).getTextContent();
            return new Student(ID, firstName, lastName, gender, gpa, level, address);
        }
        return null;
    }

    // =================================================================================================================


    /**
     * Writes a list of Student objects to an XML file using the provided Document.
     *
     * @param document The XML document to update with student data.
     * @param students List of students to be saved.
     */
    public static void writeStudentsToXML(List<Student> students , Document document) throws DuplicateStudentIDException {
        try {

            NodeList nodeList = document.getElementsByTagName("Student");
            List<Student> existingStudents = parseStudents(nodeList);


            Element rootElement = (Element) document.getElementsByTagName("University").item(0);
            if (rootElement == null) {
                // If no root element "University" exists, create one
                rootElement = document.createElement("University");
                document.appendChild(rootElement);
            }

            // Loop through each student in the list and add their information as XML elements
            for (Student student : students) {
                // Check if the student ID already exists

                boolean idExists = existingStudents.stream()
                        .anyMatch(existingStudent -> existingStudent.ID().equals(student.ID()));

                if (idExists) {
                    throw new DuplicateStudentIDException("Student with ID " + student.ID() + " already exists.");
                }
                // Create a "Student" element and set the "ID" attribute with the student's ID
                Element studentElement = document.createElement("Student");
                studentElement.setAttribute("ID", student.ID());

                // Create and append the needed elements with the student element
                Element firstName = document.createElement("FirstName");
                firstName.appendChild(document.createTextNode(student.firstName()));
                studentElement.appendChild(firstName);

                Element lastName = document.createElement("LastName");
                lastName.appendChild(document.createTextNode(student.lastName()));
                studentElement.appendChild(lastName);

                Element gender = document.createElement("Gender");
                gender.appendChild(document.createTextNode(student.gender()));
                studentElement.appendChild(gender);

                Element gpa = document.createElement("GPA");
                gpa.appendChild(document.createTextNode(String.valueOf(student.gpa())));
                studentElement.appendChild(gpa);

                Element level = document.createElement("Level");
                level.appendChild(document.createTextNode(String.valueOf(student.level())));
                studentElement.appendChild(level);

                Element address = document.createElement("Address");
                address.appendChild(document.createTextNode(student.address()));
                studentElement.appendChild(address);

                // Add student element to the root element "University"
                rootElement.appendChild(studentElement);
            }

            // Save the updated XML document to file
            saveDocumentToFile(document);

            System.out.println("Data appended successfully to the document.");

        }
        catch (DuplicateStudentIDException e) {
            throw e;  // Rethrow the exception.DuplicateStudentIDException to be handled by the caller
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =================================================================================================================

    /**
     * Parses a NodeList of student nodes and creates a list of Student objects with the searched gpa.
     *
     * @param nodeList The NodeList containing XML nodes.
     * @param gpa searched gpa
     * @return A list of Student objects with the searched gpa.
     */
    public static List<Student> getStudentByGPA(NodeList nodeList , double gpa) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Student student = fromNode(node);
            if (student != null && student.gpa() == gpa) {
                students.add(student);
            }
        }
        return students;
    }


    // =================================================================================================================

    /**
     * Parses a NodeList of student nodes and creates a list of Student objects with the searched firstName.
     *
     * @param nodeList The NodeList containing XML nodes.
     * @param firstName searched firstName
     * @return A list of Student objects with the searched firstName.
     */
    public static List<Student> getStudentByFirstName(NodeList nodeList , String firstName) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Student student = fromNode(node);
            if (student != null && student.firstName().equals(firstName)) {
                students.add(student);
            }
        }
        return students;
    }

    // =================================================================================================================


    /**
     * Removes a student from the XML file by ID.
     *
     * here we need to pass the document to update it after the deletion
     *
     * @param document The XML file where the student data is stored.
     * @param studentId The ID of the student to be removed.
     */
    public static boolean removeStudentById(Document document, String studentId) throws Exception {
        NodeList nodeList = document.getElementsByTagName("Student");
        boolean removed = false;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element studentElement = (Element) nodeList.item(i);
            if (studentElement.getAttribute("ID").equals(studentId)) {
                studentElement.getParentNode().removeChild(studentElement);
                removed = true;
                break;
            }
        }

        // Save the updated document back to file
        if (removed) {
            saveDocumentToFile(document);
        }
        return removed;
    }

    // =================================================================================================================

    private static void saveDocumentToFile(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("src/input_university.xml"));
        transformer.transform(source, result);
    }



}
