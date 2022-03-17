package com.molean.isletopia.shared.database;

import java.sql.*;
import java.time.LocalDateTime;

public class MSPTDao {

    public static void addRecord(String server, double mspt) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.mspt(server, mspt, time) VALUES(?,?,?)
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, server);
            preparedStatement.setDouble(2, mspt);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.execute();
        }
    }

    public static double queryRecent7Days(String server) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select avg(mspt) from minecraft.mspt where server=? and time > ? ;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, server);
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
            preparedStatement.setTimestamp(2, timestamp);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }else{
                return -1;
            }
        }
    }


    public static double queryLastMSPT(String server) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select mspt from minecraft.mspt where server=? order by id desc limit 1;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, server);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }else{
                return -1;
            }
        }
    }

    public static void trim() throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                   delete from minecraft.mspt where time < ? ;
                   """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.execute();
        }
    }
}
