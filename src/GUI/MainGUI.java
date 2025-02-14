package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

import  GUI_Logic.MainGUILogic;
import exception.DuplicateStudentIDException;

public class MainGUI extends JFrame {

    // to show the output for each action
    private final JTextArea outputArea;

    // 5 buttons that perform each functionality
    private JButton showAllButton, addStudentButton, updateStudentButton, searchButton, sortButton, removeStudentButton;

    // Logic handler for the application
    private final MainGUILogic guiLogic;

    // =================================================================================================================
    public MainGUI() {
        // title of the screen
        setTitle("Student Management");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize the logic handler
        guiLogic = new MainGUILogic();

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

    // =================================================================================================================

    /**
     * create the 5 buttons of the App
     *
     * @return the panel that holds the buttons
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

    // =================================================================================================================

    private void addListeners() {
        showAllButton.addActionListener(e -> outputArea.setText(guiLogic.showAllStudents()));

        // Open form in a new window for adding student
        addStudentButton.addActionListener(e -> {
            StudentFormWindow formWindow = new StudentFormWindow(outputArea, guiLogic.getDocument());
            formWindow.setVisible(true);
        });

        removeStudentButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Student ID to remove:");
            if (id != null && !id.isEmpty()) {
                outputArea.setText(guiLogic.removeStudent(id));
            }
        });

        updateStudentButton.addActionListener(e -> {
            UpdateStudentWindow updateWindow = new UpdateStudentWindow(guiLogic);
            updateWindow.setVisible(true);
        });

        searchButton.addActionListener(e -> showSearchPanel());

        sortButton.addActionListener(e -> showSortDialog());
    }

    // =================================================================================================================

    /**
     * create 7 buttons for search
     * and create listener for them
     */
    private void showSearchPanel() {
        JFrame searchFrame = new JFrame("Search Students");
        searchFrame.setSize(300, 400);
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(this);

        JPanel searchPanel = new JPanel(new GridLayout(7, 1, 5, 5));

        // Create buttons for each search type
        JButton searchByIdButton = new JButton("Search by ID");
        JButton searchByFirstNameButton = new JButton("Search by First Name");
        JButton searchByLastNameButton = new JButton("Search by Last Name");
        JButton searchByGpaButton = new JButton("Search by GPA");
        JButton searchByLevelButton = new JButton("Search by Level");
        JButton searchByGenderButton = new JButton("Search by Gender");
        JButton searchByAddressButton = new JButton("Search by Address");

        // Add buttons to panel
        searchPanel.add(searchByIdButton);
        searchPanel.add(searchByFirstNameButton);
        searchPanel.add(searchByLastNameButton);
        searchPanel.add(searchByGpaButton);
        searchPanel.add(searchByLevelButton);
        searchPanel.add(searchByGenderButton);
        searchPanel.add(searchByAddressButton);

        // Add panel to frame
        searchFrame.add(searchPanel);
        searchFrame.setVisible(true);

        // Add action listeners for search buttons
        searchByIdButton.addActionListener(e -> search("ID", guiLogic::searchByID));
        searchByFirstNameButton.addActionListener(e -> search("First Name", guiLogic::searchByfName));
        searchByLastNameButton.addActionListener(e -> search("Last Name", guiLogic::searchBylName));
        searchByGpaButton.addActionListener(e -> search("GPA", guiLogic::searchByGPA));
        searchByLevelButton.addActionListener(e -> search("Level", guiLogic::searchByLevel));
        searchByGenderButton.addActionListener(e -> search("Gender", guiLogic::searchByGender));
        searchByAddressButton.addActionListener(e -> search("Address", guiLogic::searchByAddress));
    }

// =================================================================================================================

    /**
     * Method to perform a search with a given field and input
     */
    private void search(String field, Function<String, String> searchFunction) {
        String input = JOptionPane.showInputDialog("Enter " + field + " to search:");
        if (input != null && !input.isEmpty()) {
            String result = searchFunction.apply(input);
            outputArea.setText(result);
        } else {
            JOptionPane.showMessageDialog(this, "No input provided.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // =====================================================================================================
    private void showSortDialog() {
        // Create a dialog
        JDialog sortDialog = new JDialog(this, "Sort Options", true);
        sortDialog.setSize(300, 150);
        sortDialog.setLayout(new BorderLayout());

        // Panel for field selection
        JPanel fieldPanel = new JPanel(new FlowLayout());
        JLabel fieldLabel = new JLabel("Sort by:");
        String[] fields = {"ID", "First Name", "Last Name", "GPA", "Level", "Gender", "Address"};
        JComboBox<String> fieldDropdown = new JComboBox<>(fields);
        fieldPanel.add(fieldLabel);
        fieldPanel.add(fieldDropdown);

        // Panel for sort order
        JPanel orderPanel = new JPanel(new FlowLayout());
        JLabel orderLabel = new JLabel("Order:");
        JRadioButton ascButton = new JRadioButton("Ascending");
        JRadioButton descButton = new JRadioButton("Descending");
        ButtonGroup orderGroup = new ButtonGroup();
        orderGroup.add(ascButton);
        orderGroup.add(descButton);
        orderPanel.add(orderLabel);
        orderPanel.add(ascButton);
        orderPanel.add(descButton);

        ascButton.setSelected(true);


        // Sort button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton performSortButton = new JButton("Sort");
        performSortButton.addActionListener(e -> {
            String selectedField = (String) fieldDropdown.getSelectedItem();
            boolean isDescending = descButton.isSelected();

            // Call the sortStudents method with the selected field and order
            assert selectedField != null;
            String sortedOutput = null;
            try {
                sortedOutput = guiLogic.sortStudents(selectedField, isDescending);
            } catch (DuplicateStudentIDException ex) {
                throw new RuntimeException(ex);
            }

            // Display the sorted output in the output area
            outputArea.setText(sortedOutput);
            sortDialog.dispose();
        });

        buttonPanel.add(performSortButton);

        // Add panels to dialog
        sortDialog.add(fieldPanel, BorderLayout.NORTH);
        sortDialog.add(orderPanel, BorderLayout.CENTER);
        sortDialog.add(buttonPanel, BorderLayout.SOUTH);

        sortDialog.setLocationRelativeTo(this);
        sortDialog.setVisible(true);
    }


    // =====================================================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}
