package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import  GUI_Logic.MainGUILogic;
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

        // Adding placeholder listeners for other buttons
//        updateStudentButton.addActionListener();
//        searchButton.addActionListener();
//        sortButton.addActionListener();
    }

    // =================================================================================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}
