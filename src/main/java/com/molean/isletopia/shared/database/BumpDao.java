package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.model.BumpInfo;

import java.sql.*;

public class BumpDao {
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
