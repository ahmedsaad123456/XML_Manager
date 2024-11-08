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
     * Writes a list of Student objects to an XML file.
     *
     * @param students List of students to be saved.
     * @param fileName Name of the XML file to write.
     */
    public static void writeStudentsToXML(List<Student> students, String fileName) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Create the root element "University" and append it to the document

            Element rootElement = doc.createElement("University");
            doc.appendChild(rootElement);


            // Loop through each student in the list and add their information as XML elements
            for (Student student : students) {

                // Create a "Student" element and set the "ID" attribute with the student's ID
                Element studentElement = doc.createElement("Student");
                studentElement.setAttribute("ID", student.getID());

                // Create and append the needed elements with the student element

                // First Name
                Element firstName = doc.createElement("FirstName");
                firstName.appendChild(doc.createTextNode(student.getFirstName()));
                studentElement.appendChild(firstName);

                // last Name
                Element lastName = doc.createElement("LastName");
                lastName.appendChild(doc.createTextNode(student.getLastName()));
                studentElement.appendChild(lastName);


                // gender
                Element gender = doc.createElement("Gender");
                gender.appendChild(doc.createTextNode(student.getGender()));
                studentElement.appendChild(gender);


                // gpa
                Element gpa = doc.createElement("GPA");
                gpa.appendChild(doc.createTextNode(String.valueOf(student.getGpa())));
                studentElement.appendChild(gpa);

                // level
                Element level = doc.createElement("Level");
                level.appendChild(doc.createTextNode(String.valueOf(student.getLevel())));
                studentElement.appendChild(level);

                // address
                Element address = doc.createElement("Address");
                address.appendChild(doc.createTextNode(student.getAddress()));
                studentElement.appendChild(address);

                // add student element to the root element "university"
                rootElement.appendChild(studentElement);
            }

            // Write to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            // Specify local file path
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

            System.out.println("Data saved successfully to " + fileName);

        } catch (Exception e) {
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
            if (student != null && student.getGpa() == gpa) {
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
            if (student != null && student.getFirstName().equals(firstName)) {
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


    private static void saveDocumentToFile(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("src/university.xml"));
        transformer.transform(source, result);
    }



}
