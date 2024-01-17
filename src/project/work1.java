package project;

import java.util.Scanner;

public class work1{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Read the base and exponent from the user
        System.out.print("Enter base: ");
        double base = scan.nextDouble();
        System.out.print("Enter exponent: ");
        int exponent = scan.nextInt();

        // Calculate and display the result of base raised to the power of exponent
        // %.2f is a format specifier for floating-point numbers in output rounded to two decimal places
        System.out.printf("%.1f ^ %d = %.2f%n", base, exponent, raiseToPower(base, exponent));
    }

    /**
     * Calculates the result of raising a given base to a specified exponent.
     *
     * @param base     The base number.
     * @param exponent The exponent to which the base is raised.
     * @return The result of base raised to the power of the exponent.
     */
    private static double raiseToPower(double base, int exponent) {
        if (exponent == 0) {
            // Any number raised to the power of 0 is 1
            return 1.0;
        } else if (exponent > 0) {
            // Positive exponent: Multiply base by itself (exponent - 1) times
            return base * raiseToPower(base, exponent - 1);
        } else {
            // Negative exponent: Calculate the reciprocal and multiply recursively
            return 1.0 / (base * raiseToPower(base, -exponent - 1));
        }
    }
}
