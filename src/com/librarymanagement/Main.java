package com.librarymanagement;

import com.librarymanagement.service.LibraryService;
import com.librarymanagement.util.InputUtil;

import java.sql.SQLException;

public class Main {
    private static LibraryService libraryService = new LibraryService();
    private static InputUtil inputUtil = new InputUtil(new java.util.Scanner(System.in));

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Welcome to Library Management System ");
        System.out.println("========================================");

        while (true) {
            printMenu();
            int choice = inputUtil.readInt("Enter your choice");

            try {
                switch (choice) {
                    case 1: addBook(); break;
                    case 2: libraryService.viewBooks(); break;
                    case 3: searchBooks(); break;
                    case 4: updateBookCopies(); break;
                    case 5: deleteBook(); break;
                    case 6: addMember(); break;
                    case 7: libraryService.viewMembers(); break;
                    case 8: searchMembers(); break;
                    case 9: issueBook(); break;
                    case 10: returnBook(); break;
                    case 11: libraryService.viewIssuedBooks(); break;
                    case 12: libraryService.viewAllHistory(); break;
                    case 0:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("----------------------------------------");
        System.out.println("              MAIN MENU");
        System.out.println("----------------------------------------");
        System.out.println(" 1.  Add Book");
        System.out.println(" 2.  View All Books");
        System.out.println(" 3.  Search Books");
        System.out.println(" 4.  Update Book Copies");
        System.out.println(" 5.  Delete Book");
        System.out.println(" 6.  Register Member");
        System.out.println(" 7.  View All Members");
        System.out.println(" 8.  Search Members");
        System.out.println(" 9.  Issue Book");
        System.out.println(" 10. Return Book");
        System.out.println(" 11. View Issued Books");
        System.out.println(" 12. View All History");
        System.out.println(" 0.  Exit");
        System.out.println("----------------------------------------");
    }

    private static void addBook() throws SQLException {
        String title = inputUtil.readText("Enter title");
        String author = inputUtil.readText("Enter author");
        String category = inputUtil.readText("Enter category");
        int totalCopies = inputUtil.readInt("Enter total copies");
        libraryService.addBook(title, author, category, totalCopies);
    }

    private static void searchBooks() throws SQLException {
        String keyword = inputUtil.readText("Enter search keyword");
        libraryService.searchBooks(keyword);
    }

    private static void updateBookCopies() throws SQLException {
        int bookId = inputUtil.readInt("Enter book ID");
        int totalCopies = inputUtil.readInt("Enter total copies");
        int availableCopies = inputUtil.readInt("Enter available copies");
        libraryService.updateBookCopies(bookId, totalCopies, availableCopies);
    }

    private static void deleteBook() throws SQLException {
        int bookId = inputUtil.readInt("Enter book ID to delete");
        libraryService.deleteBook(bookId);
    }

    private static void addMember() throws SQLException {
        String name = inputUtil.readText("Enter name");
        String email = inputUtil.readText("Enter email");
        String phone = inputUtil.readText("Enter phone (10 digits)");
        libraryService.addMember(name, email, phone);
    }

    private static void searchMembers() throws SQLException {
        String keyword = inputUtil.readText("Enter name or email to search");
        libraryService.searchMembers(keyword);
    }

    private static void issueBook() throws SQLException {
        int bookId = inputUtil.readInt("Enter book ID");
        int memberId = inputUtil.readInt("Enter member ID");
        libraryService.issueBook(bookId, memberId);
    }

    private static void returnBook() throws SQLException {
        int issueId = inputUtil.readInt("Enter issue ID");
        libraryService.returnBook(issueId);
    }
}