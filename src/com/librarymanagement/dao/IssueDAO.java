package com.librarymanagement.dao;

import com.librarymanagement.config.DBConnection;
import com.librarymanagement.model.IssueRecord;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssueDAO {
    public boolean issueBook(int bookId, int memberId) throws SQLException {
        String insertIssue = "INSERT INTO issue_records (book_id, member_id, issue_date, status) VALUES (?, ?, ?, 'ISSUED')";
        String reduceAvailability = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement updateBook = connection.prepareStatement(reduceAvailability);
                 PreparedStatement insertRecord = connection.prepareStatement(insertIssue)) {

                updateBook.setInt(1, bookId);
                int updatedRows = updateBook.executeUpdate();

                if (updatedRows == 0) {
                    connection.rollback();
                    return false;
                }

                insertRecord.setInt(1, bookId);
                insertRecord.setInt(2, memberId);
                insertRecord.setDate(3, Date.valueOf(LocalDate.now()));
                insertRecord.executeUpdate();

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public boolean returnBook(int issueId) throws SQLException {
        String findIssue = "SELECT book_id FROM issue_records WHERE issue_id = ? AND status = 'ISSUED'";
        String returnIssue = "UPDATE issue_records SET return_date = ?, status = 'RETURNED' WHERE issue_id = ?";
        String increaseAvailability = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement findStatement = connection.prepareStatement(findIssue)) {
                findStatement.setInt(1, issueId);

                try (ResultSet resultSet = findStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        connection.rollback();
                        return false;
                    }

                    int bookId = resultSet.getInt("book_id");

                    try (PreparedStatement returnStatement = connection.prepareStatement(returnIssue);
                         PreparedStatement updateBook = connection.prepareStatement(increaseAvailability)) {

                        returnStatement.setDate(1, Date.valueOf(LocalDate.now()));
                        returnStatement.setInt(2, issueId);
                        returnStatement.executeUpdate();

                        updateBook.setInt(1, bookId);
                        updateBook.executeUpdate();

                        connection.commit();
                        return true;
                    }
                }

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<IssueRecord> getIssuedBooks() throws SQLException {
        List<IssueRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM issue_records WHERE status = 'ISSUED' ORDER BY issue_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                records.add(mapIssueRecord(resultSet));
            }
        }

        return records;
    }

    private IssueRecord mapIssueRecord(ResultSet resultSet) throws SQLException {
        Date returnDate = resultSet.getDate("return_date");

        return new IssueRecord(
                resultSet.getInt("issue_id"),
                resultSet.getInt("book_id"),
                resultSet.getInt("member_id"),
                resultSet.getDate("issue_date").toLocalDate(),
                returnDate == null ? null : returnDate.toLocalDate(),
                resultSet.getString("status")
        );
    }
}
