package com.molean.isletopia.shared.database;


import com.molean.isletopia.shared.database.DataSourceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParameterDao {


    public static String get(String type, String obj, String p_key) {

        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                       select p_value
                       from minecraft.parameter
                       where type = ?
                         and target = ?
                         and p_key = ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, obj);
            preparedStatement.setString(3, p_key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void set(String type, String obj, String p_key, String value) {

        if (get(type, obj, p_key) == null) {
            insert(type, obj, p_key, value);
            return;
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.parameter
                    set p_value = ?
                    where type = ?
                      and target = ?
                      and p_key = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, obj);
            preparedStatement.setString(4, p_key);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void insert(String type, String obj, String p_key, String value) {

        if (get(type, obj, p_key) != null) {
            set(type, obj, p_key, value);
            return;
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.parameter(type, target, p_key, p_value)
                    values (?, ?, ?, ?)
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, obj);
            preparedStatement.setString(3, p_key);
            preparedStatement.setString(4, value);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void delete(String type, String obj, String p_key) {

        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                       delete
                       from minecraft.parameter
                       where type = ?
                         and target = ?
                         and p_key = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, obj);
            preparedStatement.setString(3, p_key);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<String> targets(String type) {
        ArrayList<String> strings = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                       select distinct target
                       from minecraft.parameter
                       where type = ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString(1);
                strings.add(string);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return strings;
    }

    public static List<String> keys(String type, String target) {
        ArrayList<String> strings = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                       select p_key
                       from minecraft.parameter
                       where type = ? and target= ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, target);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString(1);
                strings.add(string);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return strings;
    }
}
