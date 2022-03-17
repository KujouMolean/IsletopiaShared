package com.molean.isletopia.shared.model;

import com.molean.isletopia.shared.database.DataSourceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PromoteDao {

    public static class Promote {
        public int islandId;
        public UUID uuid;
        public LocalDateTime localDateTime;
    }

    public static void checkTable() {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    create table if not exists promote
                    (
                        id        int primary key auto_increment,
                        island_id int          not null,
                        uuid      varchar(100) not null,
                        time      timestamp default current_timestamp not null
                    );
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void add(int island_id, UUID uuid) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.promote(island_id, uuid) values (?, ?);
                      """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, island_id);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static List<Promote> query() {
        List<Promote> integers = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select island_id,uuid,time from minecraft.promote order by time desc;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int islandId = resultSet.getInt(1);
                UUID uuid = UUID.fromString(resultSet.getString(2));
                Timestamp timestamp = resultSet.getTimestamp(3);
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                Promote promote = new Promote();
                promote.islandId=islandId;
                promote.uuid = uuid;
                promote.localDateTime = localDateTime;
                integers.add(promote);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return integers;
    }
}
