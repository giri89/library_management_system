package com.librarymanagement.model;

import java.time.LocalDate;

public class IssueRecord {
    private int issueId;
    private int bookId;
    private int memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public IssueRecord(int issueId, int bookId, int memberId, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate, String status) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getIssueId()           { return issueId; }
    public int getBookId()            { return bookId; }
    public int getMemberId()          { return memberId; }
    public LocalDate getIssueDate()   { return issueDate; }
    public LocalDate getDueDate()     { return dueDate; }
    public LocalDate getReturnDate()  { return returnDate; }
    public String getStatus()         { return status; }
}