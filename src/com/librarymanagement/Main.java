package com.librarymanagement;

import com.librarymanagement.service.LibraryService;
import com.librarymanagement.util.InputUtil;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryService libraryService = new LibraryService();
        try (Scanner scanner = new Scanner(System.in)) {
            InputUtil input = new InputUtil(scanner);
            boolean running = true;

            while (running) {
                printMenu();
                int choice = input.readInt("Enter your choice: ");

                try {
                    switch (choice) {
                        case 1:
                            addBook(input, libraryService);
                            break;
                        case 2:
                            libraryService.viewBooks();
                            break;
                        case 3:
                            searchBooks(input, libraryService);
                            break;
                        case 4:
                            updateBook(input, libraryService);
                            break;
                        case 5:
                            deleteBook(input, libraryService);
                            break;
                        case 6:
                            addMember(input, libraryService);
                            break;
                        case 7:
                            libraryService.viewMembers();
                            break;
                        case 8:
                            issueBook(input, libraryService);
                            break;
                        case 9:
                            returnBook(input, libraryService);
                            break;
                        case 10:
                            libraryService.viewIssuedBooks();
                            break;
                        case 0:
                            running = false;
                            System.out.println("Thank you for using Library Management System.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                }

                System.out.println();
            }
        }
    }

    private static void printMenu() {
        System.out.println("========== Library Management System ==========");
        System.out.println("1. Add Book");
        System.out.println("2. View Books");
        System.out.println("3. Search Books");
        System.out.println("4. Update Book Copies");
        System.out.println("5. Delete Book");
        System.out.println("6. Register Member");
        System.out.println("7. View Members");
        System.out.println("8. Issue Book");
        System.out.println("9. Return Book");
        System.out.println("10. View Issued Books");
        System.out.println("0. Exit");
    }

    private static void addBook(InputUtil input, LibraryService libraryService) throws SQLException {
        String title = input.readText("Title: ");
        String author = input.readText("Author: ");
        String category = input.readText("Category: ");
        int totalCopies = input.readInt("Total copies: ");
        libraryService.addBook(title, author, category, totalCopies);
    }

    private static void searchBooks(InputUtil input, LibraryService libraryService) throws SQLException {
        String keyword = input.readText("Search keyword: ");
        libraryService.searchBooks(keyword);
    }

    private static void updateBook(InputUtil input, LibraryService libraryService) throws SQLException {
        int bookId = input.readInt("Book ID: ");
        int totalCopies = input.readInt("New total copies: ");
        int availableCopies = input.readInt("New available copies: ");
        libraryService.updateBookCopies(bookId, totalCopies, availableCopies);
    }

    private static void deleteBook(InputUtil input, LibraryService libraryService) throws SQLException {
        int bookId = input.readInt("Book ID: ");
        libraryService.deleteBook(bookId);
    }

    private static void addMember(InputUtil input, LibraryService libraryService) throws SQLException {
        String name = input.readText("Name: ");
        String email = input.readText("Email: ");
        String phone = input.readText("Phone: ");
        libraryService.addMember(name, email, phone);
    }

    private static void issueBook(InputUtil input, LibraryService libraryService) throws SQLException {
        int bookId = input.readInt("Book ID: ");
        int memberId = input.readInt("Member ID: ");
        libraryService.issueBook(bookId, memberId);
    }

    private static void returnBook(InputUtil input, LibraryService libraryService) throws SQLException {
        int issueId = input.readInt("Issue ID: ");
        libraryService.returnBook(issueId);
    }
}
