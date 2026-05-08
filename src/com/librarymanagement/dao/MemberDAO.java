package com.librarymanagement.dao;

import com.librarymanagement.config.DBConnection;
import com.librarymanagement.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    public boolean addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPhone());

            return statement.executeUpdate() > 0;
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY member_id";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                members.add(mapMember(resultSet));
            }
        }

        return members;
    }

    public boolean existsById(int memberId) throws SQLException {
        String sql = "SELECT member_id FROM members WHERE member_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private Member mapMember(ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getInt("member_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("phone")
        );
    }
}
