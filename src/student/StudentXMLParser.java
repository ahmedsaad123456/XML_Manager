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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
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
    public static Document writeStudentsToXML(List<Student> students , Document document , boolean overwrite) throws DuplicateStudentIDException {
        try {

            // If overwrite is true, clear the existing content of the document
            if (overwrite) {
                document = createNewDocument();
            }

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

            return  document;

        }
        catch (DuplicateStudentIDException e) {
            throw e;  // Rethrow the exception.DuplicateStudentIDException to be handled by the caller
        }
        catch (Exception e) {
            e.printStackTrace();
            return  document;
        }

    }
    // =================================================================================================================


    /**
     * Creates a new Document (clears the existing content).
     * @return A new empty Document.
     * @throws Exception if there's an error creating the document.
     */
    private static Document createNewDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Create a root element "University"
        Element rootElement = document.createElement("University");
        document.appendChild(rootElement);

        return document;
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
     * Parses a NodeList of student nodes and creates a list of Student objects with searched id.
     * @param nodeList The NodeList containing XML nodes.
     * @param id searched id
     * @return A list of Student objects with the searched id.
     */
    public static List<Student> getStudentByID(NodeList nodeList , String id){
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Student student = fromNode(node);
            if (student!= null && student.ID().equals(id)) {
                students.add(student);
            }
        }
        return students;
    }

    // =================================================================================================================

    /**
     * Parses a NodeList of student nodes and creates a list of Student objects with searched lastname.
     * @param nodeList The NodeList containing XML nodes.
     * @param lastname searched lastname
     * @return A list of Student objects with the searched lastname.
     */
    public static List<Student> getStudentByLastName(NodeList nodeList, String lastname){
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Student student = fromNode(node);
            if (student!= null && student.lastName().equals(lastname)) {
                students.add(student);
            }
        }
        return students;
    }

    // =================================================================================================================

    /**
     * Parses a NodeList of student nodes and creates a list of Student objects with searched gender.
     * @param nodeList The NodeList containing XML nodes.
     * @param gender searched gender
     * @return A list of Student objects with the searched gender.
     */
    public static List<Student> getStudentByGender(NodeList nodeList, String gender){
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Student student = fromNode(node);
            if (student!= null && student.gender().equals(gender)) {
                students.add(student);
            }
        }
        return students;
    }

    // =================================================================================================================

    /**
     * Parses a NodeList of student nodes and creates a list of Student objects with searched level.
     * @param nodeList The NodeList containing XML nodes.
     * @param level searched level
     * @return A list of Student objects with the searched level.
     */
    public static List<Student> getStudentByLevel(NodeList nodeList, int level){
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Student student = fromNode(node);
            if (student!= null && student.level() == level) {
                students.add(student);
            }
        }
        return students;
    }

    // =================================================================================================================

    /**
     * Parses a NodeList of student nodes and creates a list of Student objects with searched address.
     * @param nodeList The NodeList containing XML nodes.
     * @param address searched address
     * @return A list of Student objects with the searched address.
     */
    public static List<Student> getStudentByAddress(NodeList nodeList, String address){
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Student student = fromNode(node);
            if (student!= null && student.address().equals(address)) {
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
    /**
     * Updates a specific attribute of a student in the XML file by ID.
     *
     * @param document The XML document where the student data is stored.
     * @param id The ID of the student to be updated.
     * @param attribute The name of the attribute or tag to update (e.g., "FirstName", "LastName").
     * @param newValue The new value for the specified attribute or tag.
     * @return true if the student was found and the attribute was updated, false otherwise.
     * @throws Exception if an error occurs while updating the XML file.
     */
    public static boolean updateStudentAttributeById(Document document, String id, String attribute, String newValue) throws Exception {
        if (document == null || id == null || id.isEmpty() || attribute == null || attribute.isEmpty()) {
            return false;
        }

        NodeList students = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < students.getLength(); i++) {
            Node studentNode = students.item(i);

            if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) studentNode;

                String studentId = studentElement.getAttribute("ID");
                if (id.equals(studentId)) {

                    updateElementValue(studentElement, attribute, newValue);

                    saveDocumentToFile(document);

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Updates the value of an XML element if the new value is not empty.
     *
     * @param parentElement The parent element containing the tag to be updated.
     * @param tagName The name of the tag whose value needs to be updated.
     * @param newValue The new value to be set for the tag.
     */
    private static void updateElementValue(Element parentElement, String tagName, String newValue) {
        if (newValue != null && !newValue.isEmpty()) {
            NodeList nodeList = parentElement.getElementsByTagName(tagName);
            if (nodeList != null && nodeList.getLength() > 0) {
                nodeList.item(0).setTextContent(newValue);
            }
        }
    }


    // =================================================================================================================

    private static void saveDocumentToFile(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("src/input_university.xml"));
        transformer.transform(source, result);
    }

    // =================================================================================================================



}
