package com.molean.isletopia.shared.database;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SkinDao {

    @Nullable
    public static String getSkinValue(UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select minecraft.skin.value from minecraft.skin where uuid=?
                    """;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        }
        return null;
    }

    @Nullable
    public static String getSkinSignature(UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select minecraft.skin.signature from minecraft.skin where uuid=?
                    """;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        }
        return null;
    }

    public static void  setSkin(UUID uuid,String value, String signature) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                   insert into minecraft.skin(uuid,value,signature) values(?,?,?) on duplicate key update value=?,signature=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, value);
            preparedStatement.setString(3, signature);
            preparedStatement.setString(4, value);
            preparedStatement.setString(5, signature);
           preparedStatement.executeUpdate();
        }
    }
}
