package name_surfer;

/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes
 * or the window is resized.
 */

import acm.graphics.*;

import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
        implements NameSurferConstants, ComponentListener{

    /**
     * A list that stores NameSurferEntry objects to be displayed on the graph.
     */
    private final ArrayList<NameSurferEntry> entryList = new ArrayList<>();

    /**
     * A set that keeps track of added names to prevent duplicates in the entry list.
     */
    private final Set<String> addedNames = new HashSet<>();

    /**
     * Creates a new NameSurferGraph object that displays the data.
     */
    public NameSurferGraph() {
        addComponentListener(this);
      //  update();
    }


    /**
     * Clears the list of name surfer entries stored inside this class.
     */
    public void clear() {
        entryList.clear();
        addedNames.clear();
    }


    /* Method: addEntry(entry) */

    /**
     * Adds a new NameSurferEntry to the list of entries on the display.
     * Note that this method does not actually draw the graph, but
     * simply stores the entry; the graph is drawn by calling update.
     */
    public void addEntry(NameSurferEntry entry) {
        String name = entry.getName();
        if (!addedNames.contains(name)) {
            entryList.add(entry);
            addedNames.add(name);
        }
    }


    /**
     * Updates the display image by deleting all the graphical objects
     * from the canvas and then reassembling the display according to
     * the list of entries. Your application must call update after
     * calling either clear or addEntry; update is also called whenever
     * the size of the canvas changes.
     */
    public void update() {
        removeAll();
        drawHorizontalLine(GRAPH_MARGIN_SIZE);
        drawHorizontalLine(getHeight() - GRAPH_MARGIN_SIZE);
        drawGraphAndVerticalLines();
    }

    /**
     * Draws the graph and vertical decade lines based on the provided list of NameSurferEntry objects.
     */
    private void drawGraphAndVerticalLines() {
        // Calculate the horizontal step between decades.
        int stepX = getWidth() / NDECADES;

        // Calculate the vertical scaling factor to fit rankings within the graph's height.
        double scaleY = (double) (getHeight() - 2 * GRAPH_MARGIN_SIZE) / MAX_RANK;

        // Iterate through each decade to draw vertical lines and labels.
        for (int i = 0; i < NDECADES; i++) {
            // Calculate the start year of the current decade.
            int startDecade = START_DECADE + DECADE_STEP * i;

            // Create a label with text representing the current decade.
            String decadeText = "  " + startDecade;
            GLabel label = new GLabel(decadeText);
            label.setFont("Arial-18");

            // Create a vertical line for the current decade.
            GLine line = new GLine(stepX * i, 0, stepX * i, getHeight());

            add(line);
            add(label, stepX * i, getHeight() - label.getDescent());
        }

        // Iterate through the list of NameSurferEntry objects.
        for (int i = 0; i < entryList.size(); i++) {
            // Get the current NameSurferEntry from the list.
            NameSurferEntry current = entryList.get(i);

            // Iterate through the decades.
            for (int j = 0; j < NDECADES; j++) {
                // Get the ranking for the current decade and adjust it if it's zero.
                int rank = (isZero(current.getRank(j)));

                // Skip drawing a line for the first decade (j=0).
                if (j != 0) {
                    drawLine(stepX, j, rank, i, current, scaleY);
                }

                // Draw labels and data points for each decade.
                drawLabelsAndPoint(stepX, j, rank, scaleY, i, current.getName());
            }
        }
    }


    /**
     * Draws a line segment on the graph representing a specific decade's rank for a name.
     *
     * @param stepX      The horizontal step between decades.
     * @param j          The index of the current decade.
     * @param rank       The rank for the current decade.
     * @param entryIndex The index of the NameSurferEntry in the entryList.
     * @param current    The current NameSurferEntry being processed.
     * @param scaleY     The vertical scaling factor for ranking values.
     */
    private void drawLine(int stepX, int j, int rank, int entryIndex, NameSurferEntry current, double scaleY) {
        // Calculate the rank for the previous decade and adjust it if it's zero.
        int previousRank = (isZero(current.getRank(j - 1)));

        // Create a GLine representing the line segment between two consecutive decades.
        GLine line = new GLine(stepX * (j - 1), GRAPH_MARGIN_SIZE + previousRank * scaleY,
                stepX * j, GRAPH_MARGIN_SIZE + rank * scaleY);

        // Choose and set the color for the line based on the entry's index.
        chooseColor(line, entryIndex);
        add(line);
    }


    /**
     * Draws labels and a data point for a specific decade's rank and name.
     *
     * @param stepX      The horizontal step between decades.
     * @param j          The index of the current decade.
     * @param rank       The rank for the current decade.
     * @param scaleY     The vertical scaling factor for ranking values.
     * @param entryIndex The index of the NameSurferEntry in the entryList.
     * @param name       The name being displayed.
     */
    private void drawLabelsAndPoint(int stepX, int j, int rank, double scaleY, int entryIndex, String name) {
        // Create a label for displaying the rank (with "*" if it's MAX_RANK).
        GLabel rankText = addLabel((rank == MAX_RANK ? "*" : " " + rank), entryIndex);

        // Create a label for displaying the name.
        GLabel nameText = addLabel(name, entryIndex);

        // Calculate the width of the name label.
        double nameTextWidth = nameText.getWidth();

        // Add the rank label and position it appropriately.
        add(rankText, stepX * j + nameTextWidth, GRAPH_MARGIN_SIZE + rank * scaleY);

        // Add the name label and position it appropriately.
        add(nameText, stepX * j, GRAPH_MARGIN_SIZE + rank * scaleY);

        // Draw an oval data point for the rank and name.
        drawOval(stepX, j, rank, scaleY, entryIndex);
    }


    /**
     * Draws an oval data point on the graph representing a specific decade's rank and name.
     *
     * @param stepX      The horizontal step between decades.
     * @param j          The index of the current decade.
     * @param rank       The rank for the current decade.
     * @param scaleY     The vertical scaling factor for ranking values.
     * @param entryIndex The index of the NameSurferEntry in the entryList.
     */
    private void drawOval(int stepX, int j, int rank, double scaleY, int entryIndex) {
        // Create an oval representing a data point.
        GOval point = new GOval(stepX * j - POINT_DIAMETER / 2,
                (GRAPH_MARGIN_SIZE + rank * scaleY) - POINT_DIAMETER / 2,
                POINT_DIAMETER, POINT_DIAMETER);

        // Fill the oval to make it visible.
        point.setFilled(true);

        // Choose and set the color for the oval based on the entry's index.
        chooseColor(point, entryIndex);
        add(point);
    }

    /**
     * Creates and configures a GLabel for displaying text on the canvas.
     *
     * @param text The text to be displayed on the label.
     * @param i    The index used to choose the label's color.
     * @return A GLabel with the specified text and styling.
     */
    private GLabel addLabel(String text, int i) {
        // Calculate a font scaling factor based on canvas size.
        double fontScale = ((double) getWidth() * getHeight()) / (APPLICATION_HEIGHT * APPLICATION_WIDTH);

        // Create a GLabel with the specified text.
        GLabel label = new GLabel(text);

        // Set the font for the label with scaling.
        label.setFont(new Font("Arial", Font.BOLD, (int) (13 * fontScale)));

        // Choose and set the color for the label based on the entry's index.
        chooseColor(label, i);

        // Bring the label to the front so it's visible.
        label.sendToFront();

        return label;
    }


    /**
     * Checks if the rank is zero and replaces it with the maximum rank value if so.
     *
     * @param rank The rank value to be checked and possibly replaced.
     * @return The original rank value if not zero, or the maximum rank value if zero.
     */
    private int isZero(int rank) {
        // Check if the rank is zero and replace it with the maximum rank value if true.
        return rank == 0 ? MAX_RANK : rank;
    }


    /**
     * Sets the color of a GObject based on the provided index and a predefined array of colors.
     *
     * @param obj The GObject whose color is to be set.
     * @param i   The index used to select a color from the predefined array.
     */
    private void chooseColor(GObject obj, int i) {
        // Define an array of colors.
        Color[] colors = {Color.BLUE, Color.RED, Color.MAGENTA, Color.BLACK};

        // Set the color of the GObject using the specified index and the color array.
        obj.setColor(colors[i % colors.length]);
    }


    /**
     * Draws horizontal line through all canvas
     *
     * @param height set margin
     */
    public void drawHorizontalLine(double height) {
        GLine line = new GLine(0, height, getWidth(), height);
        add(line);
    }


    /* Implementation of the ComponentListener interface */
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        update();
    }

    public void componentShown(ComponentEvent e) {
    }
}