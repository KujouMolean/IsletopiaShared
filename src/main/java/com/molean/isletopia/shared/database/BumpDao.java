package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.model.BumpInfo;

import java.sql.*;

public class BumpDao {


    public static void checkTable() throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                create table if not exists bump
                (
                    id       int primary key auto_increment,
                    username varchar(100)       not null,
                    datetime timestamp          not null,
                    claimed  bool default false not null,
                    constraint uk
                        unique (username, datetime)
                );
                                
                """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        }

    }

    public static boolean exist(BumpInfo bumpInfo) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select * from minecraft.bump where username=? and datetime=?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bumpInfo.getUsername());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(bumpInfo.getDateTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    public static void addBumpInfo(BumpInfo bumpInfo) throws SQLException {
        if (exist(bumpInfo)) {
            return;
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.bump(username, datetime) VALUES(?,?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bumpInfo.getUsername());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(bumpInfo.getDateTime()));
            preparedStatement.executeUpdate();
        }
    }


}
