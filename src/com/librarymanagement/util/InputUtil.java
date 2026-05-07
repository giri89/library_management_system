package com.librarymanagement.util;

import java.util.Scanner;

public class InputUtil {
    private Scanner scanner;

    public InputUtil(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readText(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    public int readInt(String label) {
        while (true) {
            System.out.print(label);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
