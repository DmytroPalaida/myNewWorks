package calculator;

public class Error{

    /**
     * Validates a mathematical expression for potential errors including undefined variables,
     * invalid math operations, illegal start, and illegal end of the expression.
     *
     * @param expression The mathematical expression to be validated.
     */
    protected static void validateExpression(String expression) {
        if (containsUndefinedVariables(expression)) {
            handleError("There are undefined variables in the expression");
        }

        if (containsInvalidMathOperations(expression)) {
            handleError("There is something wrong with math operations in the expression");
        }

        if (startsWithInvalidSymbol(expression)) {
            handleError("Illegal start of expression");
        }

        if (endsWithInvalidSymbol(expression)) {
            handleError("Illegal end of expression");
        }

    }

    /**
     * Checks if the expression contains undefined variables.
     *
     * @param expression The expression to check for undefined variables.
     * @return True if the expression contains undefined variables; otherwise, false.
     */
    private static boolean containsUndefinedVariables(String expression) {
        // Functions that are allowed in the expression without being considered as variables
        String[] excludedFunctions = {"sin", "cos", "tan", "atan", "log10", "log2", "sqrt"};
        // Convert the expression to lowercase for case-insensitive comparison
        expression = expression.toLowerCase();
        // Check if the expression contains any excluded functions
        for (String function : excludedFunctions) {
            if (expression.contains(function)) {
                return false;
            }
        }
        // Check if the expression contains any letters, which might indicate undefined variables
        for (char character : expression.toCharArray()) {
            if (Character.isLetter(character)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a mathematical expression contains invalid combinations of mathematical operations,
     * such as consecutive addition or subtraction followed by multiplication or division.
     *
     * @param expression The mathematical expression to be checked for invalid math operations.
     * @return True if invalid math operations are found, false otherwise.
     */
    private static boolean containsInvalidMathOperations(String expression) {
        return expression.contains("+*") || expression.contains("*+") ||
                expression.contains("+/") || expression.contains("/+") ||
                expression.contains("-*") || expression.contains("-/");
    }

    /**
     * Checks if a mathematical expression starts with an invalid symbol (* or /).
     *
     * @param expression The mathematical expression to be checked.
     * @return True if the expression starts with an invalid symbol, false otherwise.
     */
    private static boolean startsWithInvalidSymbol(String expression) {
        return expression.startsWith("*") || expression.startsWith("/");
    }

    /**
     * Checks if a mathematical expression ends with an invalid symbol (*, /, +, or -).
     *
     * @param expression The mathematical expression to be checked.
     * @return True if the expression ends with an invalid symbol, false otherwise.
     */
    private static boolean endsWithInvalidSymbol(String expression) {
        return expression.endsWith("*") || expression.endsWith("/") ||
                expression.endsWith("+") || expression.endsWith("-");
    }

    /**
     * Throws an exception with the provided error message.
     *
     * @param errorMessage The error message to be included in the exception.
     * @throws IllegalArgumentException Always throws an IllegalArgumentException with the specified error message.
     */
    private static void handleError(String errorMessage) {
        throw new IllegalArgumentException(errorMessage);
    }
}
