package com.librarymanagement.service;

import com.librarymanagement.dao.BookDAO;
import com.librarymanagement.dao.IssueDAO;
import com.librarymanagement.dao.MemberDAO;
import com.librarymanagement.model.Book;
import com.librarymanagement.model.IssueRecord;
import com.librarymanagement.model.Member;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LibraryService {
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private IssueDAO issueDAO;

    public LibraryService() {
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
        this.issueDAO = new IssueDAO();
    }

    public void addBook(String title, String author, String category, int totalCopies) throws SQLException {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }
        if (author == null || author.trim().isEmpty()) {
            System.out.println("Author cannot be empty.");
            return;
        }
        if (category == null || category.trim().isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }
        if (totalCopies <= 0) {
            System.out.println("Total copies must be greater than zero.");
            return;
        }

        boolean added = bookDAO.addBook(new Book(title.trim(), author.trim(), category.trim(), totalCopies));
        System.out.println(added ? "Book added successfully." : "Book could not be added.");
    }

    public void viewBooks() throws SQLException {
        List<Book> books = bookDAO.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        printBooks(books);
    }

    public void searchBooks(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        List<Book> books = bookDAO.searchBooks(keyword.trim());

        if (books.isEmpty()) {
            System.out.println("No matching books found.");
            return;
        }

        printBooks(books);
    }

    public void updateBookCopies(int bookId, int totalCopies, int availableCopies) throws SQLException {
        if (totalCopies <= 0) {
            System.out.println("Total copies must be greater than zero.");
            return;
        }
        if (availableCopies < 0) {
            System.out.println("Available copies cannot be negative.");
            return;
        }
        if (totalCopies < availableCopies) {
            System.out.println("Available copies cannot be more than total copies.");
            return;
        }

        boolean updated = bookDAO.updateBookCopies(bookId, totalCopies, availableCopies);
        System.out.println(updated ? "Book updated successfully." : "Book not found.");
    }

    public void deleteBook(int bookId) throws SQLException {
        if (issueDAO.hasActiveIssues(bookId)) {
            System.out.println("Cannot delete. This book is currently issued to a member.");
            return;
        }

        boolean deleted = bookDAO.deleteBook(bookId);
        System.out.println(deleted ? "Book deleted successfully." : "Book not found.");
    }

    public void addMember(String name, String email, String phone) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }
        if (!email.trim().contains("@") || !email.trim().contains(".")) {
            System.out.println("Invalid email format.");
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            System.out.println("Phone cannot be empty.");
            return;
        }
        if (!phone.trim().matches("\\d{10}")) {
            System.out.println("Phone must be exactly 10 digits.");
            return;
        }

        boolean added = memberDAO.addMember(new Member(name.trim(), email.trim(), phone.trim()));
        System.out.println(added ? "Member registered successfully." : "Member could not be registered.");
    }

    public void viewMembers() throws SQLException {
        List<Member> members = memberDAO.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        printMembers(members);
    }

    public void searchMembers(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        List<Member> members = memberDAO.searchMembers(keyword.trim());

        if (members.isEmpty()) {
            System.out.println("No matching members found.");
            return;
        }

        printMembers(members);
    }

    public void issueBook(int bookId, int memberId) throws SQLException {
        if (!bookDAO.existsById(bookId)) {
            System.out.println("Book not found.");
            return;
        }

        if (!memberDAO.existsById(memberId)) {
            System.out.println("Member not found.");
            return;
        }

        boolean issued = issueDAO.issueBook(bookId, memberId);
        System.out.println(issued ? "Book issued successfully." : "Book is not available right now.");
    }

    public void returnBook(int issueId) throws SQLException {
        int result = issueDAO.returnBook(issueId);

        if (result == -1) {
            System.out.println("Active issue record not found.");
        } else if (result == 0) {
            System.out.println("Book returned successfully. No fine.");
        } else {
            System.out.println("Book returned successfully.");
            System.out.println("Overdue Fine: Rs." + result);
        }
    }

    public void viewIssuedBooks() throws SQLException {
        List<IssueRecord> records = issueDAO.getIssuedBooks();

        if (records.isEmpty()) {
            System.out.println("No books are currently issued.");
            return;
        }

        System.out.printf("%-8s %-8s %-10s %-13s %-13s %-10s%n",
                "IssueID", "BookID", "MemberID", "IssueDate", "DueDate", "Status");
        System.out.println("------------------------------------------------------------------------");

        LocalDate today = LocalDate.now();
        for (IssueRecord record : records) {
            boolean overdue = record.getDueDate() != null && today.isAfter(record.getDueDate());
            String status = overdue ? "OVERDUE !!" : record.getStatus();

            System.out.printf("%-8d %-8d %-10d %-13s %-13s %-10s%n",
                    record.getIssueId(),
                    record.getBookId(),
                    record.getMemberId(),
                    record.getIssueDate(),
                    record.getDueDate(),
                    status);
        }
    }

    private void printBooks(List<Book> books) {
        System.out.printf("%-5s %-30s %-25s %-18s %-8s %-10s%n",
                "ID", "Title", "Author", "Category", "Total", "Available");
        System.out.println("------------------------------------------------------------------------------------------------");

        for (Book book : books) {
            System.out.printf("%-5d %-30s %-25s %-18s %-8d %-10d%n",
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getTotalCopies(),
                    book.getAvailableCopies());
        }
    }

    private void printMembers(List<Member> members) {
        System.out.printf("%-5s %-25s %-30s %-15s%n", "ID", "Name", "Email", "Phone");
        System.out.println("----------------------------------------------------------------------------");

        for (Member member : members) {
            System.out.printf("%-5d %-25s %-30s %-15s%n",
                    member.getMemberId(),
                    member.getName(),
                    member.getEmail(),
                    member.getPhone());
        }
    }
    
    public void viewAllHistory() throws SQLException {
        List<IssueRecord> records = issueDAO.getAllIssueRecords();

        if (records.isEmpty()) {
            System.out.println("No issue history found.");
            return;
        }

        System.out.printf("%-8s %-8s %-10s %-13s %-13s %-13s %-10s%n",
                "IssueID", "BookID", "MemberID", "IssueDate", "DueDate", "ReturnDate", "Status");
        System.out.println("--------------------------------------------------------------------------------------");

        LocalDate today = LocalDate.now();
        for (IssueRecord record : records) {
            String status = record.getStatus();
            if ("ISSUED".equals(status) && record.getDueDate() != null && today.isAfter(record.getDueDate())) {
                status = "OVERDUE !!";
            }

            String returnDate = record.getReturnDate() == null ? "-" : record.getReturnDate().toString();

            System.out.printf("%-8d %-8d %-10d %-13s %-13s %-13s %-10s%n",
                    record.getIssueId(),
                    record.getBookId(),
                    record.getMemberId(),
                    record.getIssueDate(),
                    record.getDueDate(),
                    returnDate,
                    status);
        }
    }
}