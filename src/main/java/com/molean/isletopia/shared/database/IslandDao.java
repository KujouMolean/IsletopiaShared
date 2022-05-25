package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.database.DataSourceUtils;
import com.molean.isletopia.shared.model.Island;
import com.molean.isletopia.shared.model.IslandId;
import com.molean.isletopia.shared.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class IslandDao {


    public static Set<UUID> getIslandMember(int id) throws SQLException {
        Set<UUID> set = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select uuid
                    from minecraft.island_member
                    where island_id = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString(1));
                set.add(uuid);
            }
        }
        return set;
    }

    public static void addMember(Island island, UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.island_member(island_id,uuid)
                              values (?,?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, island.getId());
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.execute();
        }
    }

    public static void removeMember(Island island, UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    delete
                    from minecraft.island_member
                    where island_id = ?
                      and uuid = ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, island.getId());
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.execute();
        }
    }

    public static Set<String> getIslandFlag(int id) throws SQLException {
        HashSet<String> strings = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select flag
                    from minecraft.island_flag
                    where island_id = ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                strings.add(resultSet.getString(1));
            }
        }
        return strings;
    }

    public static void removeFlag(Island island, String key) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                        delete
                        from minecraft.island_flag
                        where island_id = ?
                          and flag like ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, island.getId());
            preparedStatement.setString(2, key + "%");
            preparedStatement.execute();
        }
    }


    public static void addFlag(Island island, String flag) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.island_flag(island_id, flag)
                                values (?, ?)
                      """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, island.getId());
            preparedStatement.setString(2, flag);
            preparedStatement.execute();
        }
    }

    public static void createIsland(Island island) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.island(x, z, spawnX, spawnY, spawnZ,yaw,pitch, server,uuid, creation,icon)
                                           values (?, ?, ?, ?, ?, ?,?,?, ?, ?,?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, island.getX());

            preparedStatement.setInt(2, island.getZ());
            preparedStatement.setDouble(3, island.getSpawnX());
            preparedStatement.setDouble(4, island.getSpawnY());
            preparedStatement.setDouble(5, island.getSpawnZ());
            preparedStatement.setFloat(6, island.getYaw());
            preparedStatement.setFloat(7, island.getPitch());
            preparedStatement.setString(8, island.getServer());
            preparedStatement.setString(9, island.getUuid().toString());
            preparedStatement.setTimestamp(10, island.getCreation());
            preparedStatement.setString(11, island.getIcon());
            preparedStatement.execute();
            Set<UUID> members = island.getMembers();
            Set<String> islandFlags = island.getIslandFlags();
            Island islandByIslandId = getIslandByIslandId(island.getIslandId());
            if (islandByIslandId == null) {
                throw new RuntimeException("Unexpected database error!");
            }
            for (UUID uuid : members) {
                addMember(islandByIslandId, uuid);
            }

            for (String islandFlag : islandFlags) {
                addFlag(islandByIslandId, islandFlag);
            }

        }

    }

    public static void updateIsland(Island island) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.island
                    set spawnX=?,
                        spawnY=?,
                        spawnZ=?,
                        yaw=?,
                        pitch=?,
                        uuid=?,
                        name=?,
                        creation=?,
                        icon=?
                    where id = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, island.getSpawnX());
            preparedStatement.setDouble(2, island.getSpawnY());
            preparedStatement.setDouble(3, island.getSpawnZ());
            preparedStatement.setFloat(4, island.getYaw());
            preparedStatement.setFloat(5, island.getPitch());
            preparedStatement.setString(6, island.getUuid().toString());
            preparedStatement.setString(7, island.getName());
            preparedStatement.setTimestamp(8, island.getCreation());
            preparedStatement.setString(9, island.getIcon());
            preparedStatement.setInt(10, island.getId());
            preparedStatement.execute();
        }


        //update member
        Set<UUID> oldMember = getIslandMember(island.getId());
        for (UUID uuid : oldMember) {
            if (!island.getMembers().contains(uuid)) {
                removeMember(island, uuid);
            }
        }

        for (UUID uuid : island.getMembers()) {
            if (!oldMember.contains(uuid)) {
                addMember(island, uuid);
            }
        }

        //update flag
        Set<String> oldFlags = getIslandFlag(island.getId());
        for (String s : oldFlags) {
            if (!island.getIslandFlags().contains(s)) {
                removeFlag(island, s.split("#")[0]);
            }
        }

        for (String flag : island.getIslandFlags()) {
            if (!oldFlags.contains(flag)) {
                addFlag(island, flag);
            }
        }
    }


    @Nullable
    public static Integer countIslandByPlayer(UUID owner) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select count(*)
                    from minecraft.island
                    where uuid = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return null;
    }

    @Nullable
    public static LocalDateTime getPlayerFirstIslandCreation(UUID owner) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select creation
                    from minecraft.island
                    where uuid=?
                    order by creation;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getTimestamp("creation").toLocalDateTime();
            }
        }
        return null;
    }


    public static Integer countIslandByServer(String server) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select count(*)
                    from minecraft.island
                    where server = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, server);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return null;
    }

    public static HashSet<Island> parseIsland(ResultSet resultSet) throws SQLException {
        HashSet<Island> islands = new HashSet<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int x = resultSet.getInt("x");
            int z = resultSet.getInt("z");
            String spawnWorld = resultSet.getString("spawnWorld");
            double spawnX = resultSet.getDouble("spawnX");
            double spawnY = resultSet.getDouble("spawnY");
            double spawnZ = resultSet.getDouble("spawnZ");
            float yaw = resultSet.getFloat("yaw");
            float pitch = resultSet.getFloat("pitch");
            String server = resultSet.getString("server");
            String uuidString = resultSet.getString("uuid");
            UUID uuid = null;
            if (uuidString != null) {
                uuid = UUID.fromString(uuidString);
            }
            String name = resultSet.getString("name");
            Timestamp creation = resultSet.getTimestamp("creation");
            String icon = resultSet.getString("icon");
            Set<UUID> islandMember = getIslandMember(id);
            Set<String> islandFlag = getIslandFlag(id);
            assert uuid != null;
            Island island = new Island(id, x, z, spawnWorld, spawnX, spawnY, spawnZ, yaw, pitch, server, uuid, name, creation, islandMember, islandFlag, icon);
            islands.add(island);
        }
        return islands;
    }


    public static List<IslandId> getPlayerIslandIds(UUID uuid) throws SQLException {
        List<IslandId> islandIds = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select server,x,z from minecraft.island where uuid = ? order by id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString(1);
                int x = resultSet.getInt(2);
                int z = resultSet.getInt(3);
                islandIds.add(new IslandId(string, x, z));
            }
        }
        return islandIds;
    }

    @Nullable
    public static Island getIslandByIslandId(IslandId islandId) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select * from minecraft.island where x = ? and z = ? and server = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, islandId.getX());
            preparedStatement.setInt(2, islandId.getZ());
            preparedStatement.setString(3, islandId.getServer());
            ResultSet resultSet = preparedStatement.executeQuery();
            HashSet<Island> islands = parseIsland(resultSet);
            if (!islands.isEmpty()) {
                return new ArrayList<>(islands).get(0);
            }
        }
        return null;
    }

    @Nullable
    public static Island getIslandById(int id) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select * from minecraft.island where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            HashSet<Island> islands = parseIsland(resultSet);
            if (!islands.isEmpty()) {
                return new ArrayList<>(islands).get(0);
            }
        }
        return null;
    }


    public static Set<IslandId> getAllIslandId(UUID owner) throws SQLException {
        HashSet<IslandId> islandIds = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select server,x,z from minecraft.island where uuid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString(1);
                int x = resultSet.getInt(2);
                int z = resultSet.getInt(3);
                islandIds.add(new IslandId(string, x, z));
            }
        }
        return islandIds;
    }

    public static Set<IslandId> getLocalServerIslandIds(String server) throws SQLException {
        HashSet<IslandId> islandIds = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select server,x,z from minecraft.island where server = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, server);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString(1);
                int x = resultSet.getInt(2);
                int z = resultSet.getInt(3);
                islandIds.add(new IslandId(string, x, z));
            }
        }
        return islandIds;
    }

    public static List<IslandId> getPlayerLocalServerIslands(String server, UUID owner) throws SQLException {
        List<IslandId> islandIds = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select server,x,z from minecraft.island where uuid = ? and server=? order by id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            preparedStatement.setString(2, server);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString(1);
                int x = resultSet.getInt(2);
                int z = resultSet.getInt(3);
                islandIds.add(new IslandId(string, x, z));
            }
        }
        return islandIds;
    }


    public static IslandId getPlayerLocalServerFirstIsland(String server, UUID owner) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select server,x,z from minecraft.island where server = ? and uuid = ? order by id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, server);
            preparedStatement.setString(2, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String string = resultSet.getString(1);
                int x = resultSet.getInt(2);
                int z = resultSet.getInt(3);
                return new IslandId(string, x, z);
            }
        }
        return null;
    }

    public static IslandId getPlayerFirstIsland(UUID owner) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select server,x,z from minecraft.island where uuid = ? order by id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String string = resultSet.getString(1);
                int x = resultSet.getInt(2);
                int z = resultSet.getInt(3);
                return new IslandId(string, x, z);
            }
        }
        return null;
    }

    public static void delete(int id) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            {
                //clear member
                String sql = "delete from minecraft.island_member where island_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
            }
            {
                //clear flag
                String sql = "delete from minecraft.island_flag where island_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.execute();

            }
            {
                String sql = "delete from minecraft.island_visit where island_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
            }
            {
                //remove record
                String sql = "delete from minecraft.island where id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
            }
        }
    }

    public static void addVisit(int id, String visitor) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.island_visit(island_id, visitor)
                    values (?, ?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, visitor);
            preparedStatement.execute();
        }
    }

    public static List<Pair<String, Timestamp>> queryVisit(int id, int day) throws SQLException {
        ArrayList<Pair<String, Timestamp>> pairs = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select visitor, time
                    from minecraft.island_visit
                    where minecraft.island_visit.island_id = ?
                      and island_visit.time > ?
                    order by time desc;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusDays(day)));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String visitor = resultSet.getString(1);
                Timestamp timestamp = resultSet.getTimestamp(2);
                pairs.add(new Pair<>(visitor, timestamp));
            }
        }
        return pairs;
    }

    public static void deleteMember(UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "delete from minecraft.island_member where uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void replaceOwner(UUID source, UUID target) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "update minecraft.island set uuid=? where uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, target.toString());
            preparedStatement.setString(2, source.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void replaceMember(UUID source, UUID target) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "update minecraft.island_member set uuid=? where uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, target.toString());
            preparedStatement.setString(2, source.toString());
            preparedStatement.executeUpdate();
        }
    }

}
