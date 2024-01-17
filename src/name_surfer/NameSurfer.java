package name_surfer;


/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */


import com.shpp.cs.a.simple.SimpleProgram;

import javax.swing.*;
import java.awt.event.*;


public class NameSurfer extends SimpleProgram implements NameSurferConstants{
    /* Method: init() */

    /**
     * This method has the responsibility for reading in the data base
     * and initializing the interactors at the top of the window.
     */
    public void init() {
        dataBase = new NameSurferDataBase(NAMES_DATA_FILE);
        graph = new NameSurferGraph();
        add(graph);
        createButtons();
        addActionListeners();
    }

    /**
     * Creates and initializes the interactive components in the user interface,
     * such as text fields and buttons.
     */
    private void createButtons() {

        add(new JLabel("Name: "), NORTH);

        textField = new JTextField(NAME_FIELD);
        textField.setActionCommand("Graph");
        textField.addActionListener(this);
        add(textField, NORTH);

        add(new JButton("Graph"), NORTH);
        add(new JButton("Clear"), NORTH);

    }
    /* Method: actionPerformed(e) */

    /**
     * This class is responsible for detecting when the buttons are
     * clicked, so you will have to define a method to respond to
     * button actions.
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        // If the "Graph" button is clicked
        if (cmd.equals("Graph")) {
            String name = textField.getText();
            NameSurferEntry entry = dataBase.findEntry(name);
          //  System.out.println(entry);
            // If the name is found in the database
            if (entry != null) {
                // Add the name's popularity entry to the graph and update the display
                graph.addEntry(entry);
                graph.update();
            } else {
                // Show an error message in case the name is not found in the database
                this.getDialog().showErrorMessage(dialogMessage + " ! ! !");
            }
        }
        // If the "Clear" button is clicked
        else if (cmd.equals("Clear")) {
            // Clear the graph, reset the text field, and update the display
            graph.clear();
            textField.setText("");
            graph.update();
        }
    }

    /**
     * Represents the graphical component used for displaying the name popularity graph.
     */
    private NameSurferGraph graph;
    /**
     * Represents the database of name popularity data used in the program.
     */
    private NameSurferDataBase dataBase;
    /**
     * Represents the text field where users can input a name for graphing.
     */
    private JTextField textField;
    /**
     * A static message to be displayed in case the name does not exist in the database.
     */
    static String dialogMessage = "the name does not exist in the database";
}