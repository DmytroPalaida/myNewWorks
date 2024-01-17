package project;

import java.util.Scanner;

public class work2{
    // Define a constant integer representing the base for numeric operations.
    private static final int BASE = 10;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first number:  ");
        String n1 = sc.nextLine();
        System.out.print("Enter second number:  ");
        String n2 = sc.nextLine();
        System.out.print(n1 + " + " + n2 + " = " + addNumericStrings(n1, n2));

    }

    /**
     * Given two string representations of nonnegative integers, adds the
     * numbers represented by those strings and returns the result.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return sum String representation of n1 + n2
     */
    private static String addNumericStrings(String n1, String n2) {
        int maxLength = Math.max(n1.length(), n2.length());
        n1 = padWithZeros(n1, maxLength);
        n2 = padWithZeros(n2, maxLength);

        // Initialize variables for carrying over the sum
        int carry = 0;
        StringBuilder sum = new StringBuilder();

        for (int i = maxLength - 1; i >= 0; i--) {
            int value1 = n1.charAt(i) - '0';
            int value2 = n2.charAt(i) - '0';
            int addition = value1 + value2 + carry;
            carry = addition / BASE;
            sum.append(addition % BASE);
        }

        if (carry > 0) {
            sum.append(carry);
        }

        return sum.reverse().toString();
    }

    /**
     * Pads a given string with leading zeros to make it a specified length.
     *
     * @param str    The input string that needs to be padded.
     * @param length The desired length of the padded string.
     * @return A new string that is the input string padded with leading zeros to reach the specified length.
     */
    private static String padWithZeros(String str, int length) {
        StringBuilder paddedStr = new StringBuilder(str);
        while (paddedStr.length() < length) {
            paddedStr.insert(0, '0');
        }
        return paddedStr.toString();
    }
}
