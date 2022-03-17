package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.model.Mail;
import com.molean.isletopia.shared.platform.BukkitRelatedUtils;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

public class MailboxDao {
    static {
        //require bukkit api
//        assert PlatformRelatedUtils.getInstance() instanceof BukkitRelatedUtils;
    }

    public static void request(Mail mail) throws SQLException {
        UUID source = mail.getSource();
        UUID target = mail.getTarget();
        String message = mail.getMessage();
        byte[] data = mail.getData();

        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert minecraft.mailbox(source, target, message, data) VALUES (?,?,?,?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, source == null ? null : source.toString());
            preparedStatement.setString(2, target.toString());
            preparedStatement.setString(3, message);
            preparedStatement.setBlob(4, new ByteArrayInputStream(data));
            preparedStatement.executeUpdate();
        }
    }

    public static void claim(int id) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.mailbox set claimed=true where id = ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private static Mail getMailFromResultSet(ResultSet resultSet) throws SQLException {
        Mail mail = new Mail();
        mail.setId(resultSet.getInt("id"));
        String source = resultSet.getString("source");
        mail.setSource(source == null ? null : UUID.fromString(source));
        mail.setTarget(UUID.fromString(resultSet.getString("target")));
        mail.setMessage(resultSet.getString("message"));
        mail.setData(resultSet.getBytes("data"));
        mail.setClaimed(resultSet.getBoolean("claimed"));
        mail.setLocalDateTime(resultSet.getTimestamp("time").toLocalDateTime());
        return mail;
    }

    public static List<Mail> getMails(UUID target) throws SQLException {
        ArrayList<Mail> mail = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                   select id, source, target, message, data, claimed, time
                   from minecraft.mailbox
                   where target=?
                   order by time
                   """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, target.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Mail mailFromResultSet = getMailFromResultSet(resultSet);
                mail.add(mailFromResultSet);
            }
        }
        return mail;
    }
}
