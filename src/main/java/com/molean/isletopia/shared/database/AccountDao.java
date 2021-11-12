package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class AccountDao {
    public static Account getAccount(String username) throws SQLException {
        if (username.startsWith("#")) {
            username = username.substring(1);
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                        select username, realname, password, regip, ip, regdate
                        from skin.users
                        where username = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Account account = new Account();
                account.setUsername(resultSet.getString(1));
                account.setRealname(resultSet.getString(2));
                account.setPassword(resultSet.getString(3));
                account.setRegip(resultSet.getString(4));
                account.setIp(resultSet.getString(5));
                account.setRegdate(resultSet.getLong(6));
                return account;
            }
        }
        return null;

    }

    public static void updateAccount(Account account) throws SQLException {
        if (account.getRealname().startsWith("#")) {
            account.setRealname(account.getRealname().substring(1));
        }

        if (account.getUsername().startsWith("#")) {
            account.setUsername(account.getUsername().substring(1));
        }

        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                       update skin.users set realname=?,password=?,regip=?,ip=?,regdate=? where username=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getRealname());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setString(3, account.getRegip());
            preparedStatement.setString(4, account.getIp());
            preparedStatement.setLong(5, account.getRegdate());
            preparedStatement.setString(6, account.getUsername());
            preparedStatement.executeUpdate();
        }
    }


    public static void register(Account account) throws SQLException {

        if (account.getRealname().startsWith("#")) {
            account.setRealname(account.getRealname().substring(1));
        }

        if (account.getUsername().startsWith("#")) {
            account.setUsername(account.getUsername().substring(1));
        }

        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into skin.users(username,realname,password,regip,ip,regdate)
                    values (?,?,?,?,?,?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getRealname());
            preparedStatement.setString(3, account.getPassword());
            preparedStatement.setString(4, account.getRegip());
            preparedStatement.setString(5, account.getIp());
            preparedStatement.setLong(6, account.getRegdate());
            preparedStatement.execute();

        }

    }
}
