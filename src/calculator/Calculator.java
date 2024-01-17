package calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator{
    /**
     * Creates and initializes a map of mathematical function names to their corresponding implementation
     * objects.
     * The map is used to associate function names with specific actions during expression evaluation.
     *
     * @return A HashMap containing function names as keys and instances of classes
     * implementing the IAction interface as values.
     */
    private static HashMap<String, IAction> makeFunctionMap() {
        HashMap<String, IAction> map = new HashMap<>();
        map.put("sin", new Sin());
        map.put("cos", new Cos());
        map.put("tan", new Tan());
        map.put("atan", new Atan());
        map.put("log10", new Log10());
        map.put("log2", new Log2());
        map.put("sqrt", new Sqrt());
        return map;
    }

    /**
     * Evaluates a mathematical expression containing predefined functions and replaces function calls
     * with their results.
     * The expression is processed using a regular expression to identify function calls.
     *
     * @param expression  The mathematical expression to be evaluated.
     * @param functionMap A map associating function names with instances of classes implementing
     *                    the IAction interface.
     * @return The result of evaluating the expression after replacing function calls.
     * @throws IllegalArgumentException If a function call is missing its required argument or if there's
     *                                  an issue with the expression.
     */
    private static double calculateWithFunctions(String expression, Map<String, IAction> functionMap) {
        String functionCallRegex = "(sin|cos|tan|atan|log10|log2|sqrt)(-?[\\d.]+)";
        Matcher matcher = Pattern.compile(functionCallRegex).matcher(expression);
        while (matcher.find()) {
            String functionName = matcher.group(1);
            String argumentToken = matcher.group(2);
            // Check if the argument for the function call is missing
            if (argumentToken.isEmpty()) {
                throw new IllegalArgumentException("Missing argument for function: " + functionName);
            }

            double argument = Double.parseDouble(argumentToken);
            // Calculate the result using the associated function implementation
            double result = functionMap.get(functionName).calculate(argument);
            // Replace the function call in the expression with its result
            expression = expression.replaceFirst(functionCallRegex, String.valueOf(result));
            // Reset the matcher to reprocess the modified expression
            matcher.reset(expression);
        }
        // Simplify the expression after handling function calls
        expression = simplifyExpression(expression);

        return calculate(expression);
    }

    /**
     * Evaluates a mathematical expression with support for variables and predefined functions.
     * The method follows the order of operations, handling parentheses first, then simplifying the expression,
     * and finally replacing function calls with their results before the actual calculation.
     *
     * @param formula   The mathematical expression to be evaluated, possibly containing variables and functions.
     * @param variables A map associating variable names with their respective values.
     * @return The result of evaluating the expression.
     * @throws IllegalArgumentException If there's an issue with the expression or an error during evaluation.
     */
    public static double calculate(String formula, Map<String, Double> variables) {
        // Handle parentheses in the expression by recursively evaluating sub-expressions
        formula = handleParentheses(formula, variables);
        // Preprocess and replace variables in the expression with their values
        formula = preprocessExpression(substituteVariablesInFormula(formula, variables));
        HashMap<String, IAction> functionMap = makeFunctionMap();
        Error.validateExpression(formula);


        return calculateWithFunctions(formula, functionMap);
    }

    /**
     * Recursively handles parentheses in a mathematical expression by evaluating sub-expressions
     * enclosed in parentheses and replacing them with their calculated results.
     *
     * @param formula   The mathematical expression possibly containing parentheses.
     * @param variables A map associating variable names with their respective values.
     * @return The modified expression with resolved sub-expressions inside parentheses.
     * @throws IllegalArgumentException If there's an issue with the parentheses in the expression.
     */
    private static String handleParentheses(String formula, Map<String, Double> variables) {
        while (formula.contains("(") || formula.contains(")")) {
            // Find the last open parenthesis and the first close parenthesis after it
            int openIndex = formula.lastIndexOf("(");
            int closeIndex = formula.indexOf(")", openIndex);

            if (closeIndex == -1) {
                throw new IllegalArgumentException("the right parenthesis is not closed");
            }
            if (openIndex == -1) {
                throw new IllegalArgumentException("the left parenthesis is not closed");
            }
            // Extract the sub-expression inside the parentheses
            String subExpression = formula.substring(openIndex + 1, closeIndex);
            // Calculate the result of the sub-expression
            String subResult = String.valueOf(calculate(subExpression, variables));
            // Replace the sub-expression with its calculated result in the original expression
            formula = formula.substring(0, openIndex) + subResult + formula.substring(closeIndex + 1);
        }

        return formula;
    }

    /**
     * Substitutes variables in a mathematical formula with their corresponding values.
     *
     * @param formula   The original mathematical formula that may contain variables.
     * @param variables A map of variables and their corresponding values for substitution.
     * @return The formula with variables replaced by their values.
     */
    private static String substituteVariablesInFormula(String formula, Map<String, Double> variables) {
        if (variables != null) {
            for (Map.Entry<String, Double> entry : variables.entrySet()) {
                // Extract the variable name and its corresponding value
                String variableName = entry.getKey();
                String replacement = entry.getValue().toString();
                // Replace occurrences of the variable with its value in the formula
                formula = replaceVariable(formula, variableName, replacement);
            }
        }
        return formula;
    }

    /**
     * Replaces occurrences of a variable in a mathematical formula with a specified replacement value.
     *
     * @param formula      The original mathematical formula that may contain variable occurrences.
     * @param variableName The name of the variable to be replaced.
     * @param replacement  The value to replace the variable occurrences with.
     * @return The formula with variable occurrences replaced by the specified value.
     */
    private static String replaceVariable(String formula, String variableName, String replacement) {
        // Construct a regular expression to match the variable name as a whole word
        String regex = "(?<!\\w)" + variableName + "(?!\\w)";
        // Use a regular expression to replace occurrences of the variable with the specified value
        return formula.replaceAll(regex, replacement);
    }

    /**
     * Preprocesses a mathematical expression by removing a trailing equal sign if present.
     *
     * @param expression The original mathematical expression.
     * @return The preprocessed expression without a trailing equal sign.
     */
    private static String preprocessExpression(String expression) {
        return expression.endsWith("=") ? expression.substring(0, expression.length() - 1) : expression;
    }

    /**
     * Simplifies the given mathematical expression by iteratively applying basic algebraic
     * simplifications, such as handling exponentiation, multiplication, division, and sign combinations.
     *
     * @param expression The mathematical expression to be simplified.
     * @return The simplified expression.
     */
    private static String simplifyExpression(String expression) {
        while (expression.contains("^")) {
            expression = expression.replace("^--", "^");
            expression = simplify(expression, true);
        }
        while (expression.contains("*") || expression.contains("/")) {
            expression = expression.replace("*--", "*").replace("/--", "/");
            expression = simplify(expression, false);
        }
        while (expression.contains("++") || expression.contains("+-") ||
                expression.contains("-+") || expression.contains("--")) {
            expression = expression.replace("++", "+").replace("+-", "-")
                    .replace("-+", "-").replace("--", "+");
        }
        return expression;
    }

    /**
     * Calculates the result of a mathematical expression by iteratively applying binary operations
     * (addition and subtraction) on its operands.
     *
     * @param expression The mathematical expression to be evaluated.
     * @return The result of the calculation.
     * @throws IllegalArgumentException If the expression contains invalid numeric values.
     */
    private static double calculate(String expression) {
        try {
            // If the expression has no operations, return a numeric value
            if (getOperationIndex(expression, false) == -1) {
                return Double.parseDouble(expression);
            }
            // Initialize the result and the current operation
            double result = 0;
            char operation = '+';
            // Process the expression until it is empty
            while (!expression.isEmpty()) {
                // Extract the next number and operation index
                int operationIndex = getOperationIndex(expression, false);
                String substringToParse = (operationIndex != -1) ?
                        expression.substring(0, operationIndex) : expression;

                double number = Double.parseDouble(substringToParse);
                expression = (operationIndex != -1) ?
                        expression.substring(operationIndex) : "";

                result = performOperation(result, number, operation);
                // Update the operation and move to the next part of the expression
                if (!expression.isEmpty()) {
                    operation = expression.charAt(0);
                    expression = expression.substring(1);
                }
            }

            return result;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The expression must contain a valid numeric value.");
        }
    }

    /**
     * Performs a mathematical operation (+ or -) with the given result and number.
     *
     * @param result    The current result of the mathematical expression.
     * @param number    The next number in the expression to be operated on.
     * @param operation The mathematical operation to be performed (either '+' or '-').
     * @return The result of the operation.
     * @throws IllegalArgumentException If an invalid operation is provided.
     */
    private static double performOperation(double result, double number, char operation) {
        return switch (operation) {
            case '+' -> result + number;
            case '-' -> result - number;
            default -> throw new IllegalArgumentException("Invalid operation: " + operation);
        };
    }

    /**
     * Simplifies the mathematical expression by performing binary operations (power, multiplication,
     * or division).
     *
     * @param expression The expression to be simplified.
     * @param pow        A flag indicating whether the operation is a power operation.
     * @return The simplified expression after performing binary operations.
     */
    private static String simplify(String expression, boolean pow) {
        // Determine the indices of multiplication and division operations in the expression.
        int multiplyIndex = expression.indexOf("*");
        multiplyIndex = multiplyIndex == -1 ? Integer.MAX_VALUE : multiplyIndex;
        int divideIndex = expression.indexOf("/");
        divideIndex = divideIndex == -1 ? Integer.MAX_VALUE : divideIndex;
        // Choose the operation index based on the minimum of multiplication and division indices.
        int index = Math.min(multiplyIndex, divideIndex);

        // Split the expression using the appropriate method based on the pow flag.
        String[] parts;
        if (pow) {
            parts = splitExpressionUsingCaretOperator(expression);
        } else {
            parts = splitExpressionForDivisionAndMultiplication(expression);
        }
        return performBinaryOperation(parts[0], parts[1], pow, expression, index);
    }

    /**
     * Performs a binary operation (power, division, or multiplication) to simplify a part of
     * the mathematical expression.
     *
     * @param leftPart   The left part of the expression to be simplified.
     * @param rightPart  The right part of the expression to be simplified.
     * @param pow        A flag indicating whether the operation is a power operation.
     * @param expression The original expression for reference.
     * @param index      The index in the expression where the operation is located.
     * @return The simplified expression after performing the specified binary operation.
     * @throws IllegalArgumentException If the expression contains invalid numeric values.
     * @throws ArithmeticException      If attempting to divide by zero during a division operation.
     */
    private static String performBinaryOperation(String leftPart, String rightPart, boolean pow,
                                                 String expression, int index) {
        double operand1;
        double operand2;
        try {
            // Extract the numeric value of the operand from the left part
            if (getOperationIndex(leftPart, true) != -1) {
                operand1 = Double.parseDouble(leftPart.substring(getOperationIndex(leftPart, true) + 1));
                leftPart = leftPart.substring(0, getOperationIndex(leftPart, true) + 1);
            } else {
                operand1 = Double.parseDouble(leftPart);
                leftPart = "";
            }
            // Extract the numeric value of the operand from the right part
            if (getOperationIndex(rightPart, false) != -1) {
                operand2 = Double.parseDouble(rightPart.substring(0, getOperationIndex(rightPart, false)));
                rightPart = rightPart.substring(getOperationIndex(rightPart, false));
            } else {
                operand2 = Double.parseDouble(rightPart);
                rightPart = "";
            }
            // Perform the specified binary operation (power, division, or multiplication)
            if (pow) {
                return leftPart + Math.pow(operand1, operand2) + rightPart;
            } else if (expression.charAt(index) == '/') {
                if (operand2 == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return leftPart + (operand1 / operand2) + rightPart;
            } else {
                return leftPart + (operand1 * operand2) + rightPart;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The expression must contain a valid numeric value.");
        }
    }

    /**
     * Splits the given expression using the division or multiplication operators.
     *
     * @param expression The expression to be split.
     * @return An array containing two parts of the expression.
     */
    private static String[] splitExpressionForDivisionAndMultiplication(String expression) {
        return expression.split("[*/]", 2);
    }

    /**
     * Splits the given expression using the caret (^) operator.
     *
     * @param expression The expression to be split.
     * @return An array containing two parts of the expression.
     */
    private static String[] splitExpressionUsingCaretOperator(String expression) {
        int caretIndex = expression.lastIndexOf("^");

        if (caretIndex != -1) {
            String leftPart = expression.substring(0, caretIndex);
            String rightPart = expression.substring(caretIndex + 1);
            return new String[]{leftPart, rightPart};
        } else {
            return new String[]{expression, ""};
        }
    }

    /**
     * Gets the index of the operation symbol in the expression.
     *
     * @param expression The expression to search for the operation symbol.
     * @param fromTheEnd If true, searches from the end of the expression; otherwise, from the beginning.
     * @return The index of the operation symbol, or -1 if not found.
     */
    private static int getOperationIndex(String expression, boolean fromTheEnd) {
        if (expression.isEmpty()) return -1;

        if (fromTheEnd) {
            for (int i = expression.length() - 1; i >= 0; i--) {
                if (isOperationSymbol(expression.charAt(i)) &&
                        (i == 0 || expression.charAt(i - 1) != 'E')) return i;
            }
        } else {
            for (int i = 1; i < expression.length(); i++) {

                if (isOperationSymbol(expression.charAt(i)) &&
                        expression.charAt(i - 1) != 'E') return i;
            }
        }
        return -1;
    }

    /**
     * Checks if a given character is a valid mathematical operation symbol (+, -, *, /, ^).
     *
     * @param symbol The character to be checked.
     * @return True if the character is a valid mathematical operation symbol, false otherwise.
     */
    private static boolean isOperationSymbol(char symbol) {
        return "^*/+-".contains(String.valueOf(symbol));
    }
}

