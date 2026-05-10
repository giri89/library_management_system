package com.librarymanagement.service;

import com.librarymanagement.dao.BookDAO;
import com.librarymanagement.dao.IssueDAO;
import com.librarymanagement.dao.MemberDAO;
import com.librarymanagement.model.Book;
import com.librarymanagement.model.IssueRecord;
import com.librarymanagement.model.Member;

import java.sql.SQLException;
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
        if (totalCopies <= 0) {
            System.out.println("Total copies must be greater than zero.");
            return;
        }

        boolean added = bookDAO.addBook(new Book(title, author, category, totalCopies));
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
        List<Book> books = bookDAO.searchBooks(keyword);

        if (books.isEmpty()) {
            System.out.println("No matching books found.");
            return;
        }

        printBooks(books);
    }

    public void updateBookCopies(int bookId, int totalCopies, int availableCopies) throws SQLException {
        if (availableCopies < 0 || totalCopies < availableCopies) {
            System.out.println("Invalid copy count.");
            return;
        }

        boolean updated = bookDAO.updateBookCopies(bookId, totalCopies, availableCopies);
        System.out.println(updated ? "Book updated successfully." : "Book not found.");
    }

    public void deleteBook(int bookId) throws SQLException {
        boolean deleted = bookDAO.deleteBook(bookId);
        System.out.println(deleted ? "Book deleted successfully." : "Book not found.");
    }

    public void addMember(String name, String email, String phone) throws SQLException {
        boolean added = memberDAO.addMember(new Member(name, email, phone));
        System.out.println(added ? "Member registered successfully." : "Member could not be registered.");
    }

    public void viewMembers() throws SQLException {
        List<Member> members = memberDAO.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

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
        boolean returned = issueDAO.returnBook(issueId);
        System.out.println(returned ? "Book returned successfully." : "Active issue record not found.");
    }

    public void viewIssuedBooks() throws SQLException {
        List<IssueRecord> records = issueDAO.getIssuedBooks();

        if (records.isEmpty()) {
            System.out.println("No books are currently issued.");
            return;
        }

        System.out.printf("%-8s %-8s %-10s %-15s %-10s%n", "IssueID", "BookID", "MemberID", "IssueDate", "Status");
        System.out.println("------------------------------------------------------------");

        for (IssueRecord record : records) {
            System.out.printf("%-8d %-8d %-10d %-15s %-10s%n",
                    record.getIssueId(),
                    record.getBookId(),
                    record.getMemberId(),
                    record.getIssueDate(),
                    record.getStatus());
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
}
