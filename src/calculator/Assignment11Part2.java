package calculator;

import java.util.Arrays;
import java.util.HashMap;


/**
 * File: Assignment11Part2.java
 * ---------------------
 * This class provides a simple calculator that can handle mathematical expressions, variables,
 * and basic functions.
 */
public class Assignment11Part2{
    /**
     * The main method for processing a mathematical expression is provided as a command-line argument.
     */
    public static void main(String[] args) {
        // Check if command-line arguments are provided
        if (args.length > 0) {
            String inputExpression = args[0].trim();
            if (!inputExpression.isEmpty()) {
                // Preprocess the expression and handle variables
                String processedExpression = preprocessMathExpression(inputExpression);
                // Convert additional arguments to a map of variable names and values
                HashMap<String, Double> variableMap = (args.length > 1) ?
                        convertVariableStringsToMap(Arrays.copyOfRange(args, 1, args.length))
                        : new HashMap<>();
                System.out.println(Calculator.calculate(processedExpression, variableMap));
            } else {
                System.err.println("the written formula is empty.");
            }
        } else {
            System.err.println("Please write the formula in program arguments");
        }
    }

    /**
     * Converts an array of variable strings into a map of variable names and their corresponding values.
     *
     * @param variables An array of variable strings in the format "variable=value".
     * @return A map containing variable names as keys and their corresponding values as values.
     * @throws IllegalArgumentException If a variable string is not in the correct format.
     */
    private static HashMap<String, Double> convertVariableStringsToMap(String[] variables) {
        HashMap<String, Double> variablesAsMap = new HashMap<>();
        try {
            for (String var : variables) {
                String variable = preprocessMathExpression(var);
                String[] parts = variable.split("=");
                String variableName = parts[0];
                double value = Double.parseDouble(parts[1]);

                variablesAsMap.put(variableName, value);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            // If there is an issue with the format of a variable string, throw an exception
            throw new IllegalArgumentException("The change is assigned through the sign \"=\"");
        }
        return variablesAsMap;
    }

    /**
     * Preprocesses a mathematical expression by removing spaces and replacing commas with periods.
     *
     * @param expression The input mathematical expression.
     * @return The preprocessed expression with spaces removed and commas replaced by periods.
     */
    private static String preprocessMathExpression(String expression) {
        return expression.replace(" ", "").replace(",", ".");
    }
}