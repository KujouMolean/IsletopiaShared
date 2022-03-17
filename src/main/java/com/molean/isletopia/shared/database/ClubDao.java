package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.model.Club;
import com.molean.isletopia.shared.model.ClubMember;
import com.molean.isletopia.shared.model.ClubRealm;
import com.molean.isletopia.shared.model.ClubRequest;

import java.sql.*;
import java.util.*;

public class ClubDao {
    private static Set<String> getClubRealmOption(String clubName) throws SQLException {
        HashSet<String> strings = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select opt
                    from minecraft.club_realm_option
                    where club = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                strings.add(resultSet.getString("opt"));
            }
        }
        return strings;
    }

    private static Set<UUID> getClubRealmWhitelist(String clubName) throws SQLException {
        HashSet<UUID> uuids = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select uuid
                    from minecraft.club_realm_whitelist
                    where club = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                uuids.add(UUID.fromString(resultSet.getString("uuid")));
            }
        }
        return uuids;
    }


    private static ClubRealm getClubRealmFromResultSet(ResultSet resultSet) throws SQLException {

        ClubRealm clubRealm = new ClubRealm();
        String club = resultSet.getString("club");
        clubRealm.setClub(club);
        clubRealm.setIcon(resultSet.getString("icon"));
        clubRealm.setDescription(resultSet.getString("description"));
        clubRealm.setTitle(resultSet.getString("title"));
        clubRealm.setRequirement(resultSet.getString("requirement"));
        clubRealm.setRule(resultSet.getString("rule"));
        Set<String> options = getClubRealmOption(club);
        Set<UUID> whitelist = getClubRealmWhitelist(club);
        clubRealm.setWhitelist(whitelist);
        clubRealm.setOptions(options);
        return clubRealm;
    }

    public static ClubRealm getClubRealm(String clubName) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select icon,club, title, description, requirement, rule
                    from minecraft.club_realm
                    where club=?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getClubRealmFromResultSet(resultSet);
            }
        }
        return null;
    }

    public static Set<ClubRealm> getClubRealms() throws SQLException {
        HashSet<ClubRealm> clubRealms = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select icon,club, title, description, requirement, rule
                    from minecraft.club_realm;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clubRealms.add(getClubRealmFromResultSet(resultSet));
            }
        }
        return clubRealms;
    }


    private static Map<UUID, ClubMember> getClubMembers(String clubName) throws SQLException {
        Map<UUID, ClubMember> map = new HashMap<>();

        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select id, uuid, time, role
                    from minecraft.club_member
                    where club = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ClubMember clubMember = new ClubMember();
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                clubMember.setUuid(uuid);
                clubMember.setTime(resultSet.getTimestamp("time").toLocalDateTime());
                clubMember.setRole(ClubMember.Role.valueOf(resultSet.getString("role")));
                map.put(uuid, clubMember);
            }
        }
        return map;
    }

    private static Club getClubFromResultSet(ResultSet resultSet) throws SQLException {
        Club club = new Club();
        String name = resultSet.getString("name");
        club.setName(name);
        club.setMembers(getClubMembers(name));
        club.setCreation(resultSet.getTimestamp("creation").toLocalDateTime());
        club.setDisplay(resultSet.getString("display"));
        club.setDescription(resultSet.getString("description"));
        club.setIcon(resultSet.getString("icon"));
        return club;
    }

    public static Club getClub(String clubName) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select id, name, display, description, creation,icon
                    from minecraft.club
                    where name = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getClubFromResultSet(resultSet);
            }
        }
        return null;
    }

    public static Set<Club> getClubs() throws SQLException {
        HashSet<Club> clubs = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select id, name, display, description, creation,icon
                    from minecraft.club
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clubs.add(getClubFromResultSet(resultSet));
            }
        }
        return clubs;
    }

    public static Set<Club> getJoinedClubs(UUID uuid) throws SQLException {
        HashSet<Club> clubs = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select club from minecraft.club_member where uuid=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("club");
                clubs.add(getClub(name));
            }
        }
        return clubs;
    }

    public static void updateClub(Club club) throws SQLException {
        if (getClub(club.getName()) == null) {
            createClub(club);
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.club
                    set club.display=?,
                        club.icon=?,
                        club.description=?
                    where club.name = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, club.getDescription());
            preparedStatement.setString(2, club.getIcon());
            preparedStatement.setString(3, club.getDescription());
            preparedStatement.setString(4, club.getName());
            preparedStatement.executeUpdate();
        }
        //update member
        Map<UUID, ClubMember> originMembers = getClubMembers(club.getName());
        Map<UUID, ClubMember> members = club.getMembers();
        for (UUID uuid : originMembers.keySet()) {
            if (!members.containsKey(uuid)) {
                removeMember(club.getName(), uuid);
            }
        }
        for (UUID uuid : members.keySet()) {
            if (!originMembers.containsKey(uuid)) {
                addMember(club.getName(), members.get(uuid));
            }
        }

        //update club realm

        if (club.getClubRealm() != null) {
            updateClubRealm(club.getClubRealm());
        }
    }

    public static void updateClubRealm(ClubRealm clubRealm) throws SQLException {
        if (getClubRealm(clubRealm.getClub()) == null) {
            createClubRealm(clubRealm);
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.club_realm
                    set club_realm.icon=?,
                        club_realm.description=?,
                        club_realm.rule=?,
                        club_realm.requirement=?,
                        club_realm.title=?
                    where club_realm.club = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubRealm.getIcon());
            preparedStatement.setString(2, clubRealm.getDescription());
            preparedStatement.setString(3, clubRealm.getRule());
            preparedStatement.setString(4, clubRealm.getRequirement());
            preparedStatement.setString(5, clubRealm.getTitle());
            preparedStatement.setString(6, clubRealm.getClub());
            preparedStatement.executeUpdate();
        }
        //update whitelist

        Set<UUID> originWhitelist = getClubRealmWhitelist(clubRealm.getClub());
        Set<UUID> whitelist = clubRealm.getWhitelist();
        for (UUID uuid : originWhitelist) {
            if (!whitelist.contains(uuid)) {
                removeWhitelist(clubRealm.getClub(), uuid);
            }
        }
        for (UUID uuid : whitelist) {
            if (!originWhitelist.contains(uuid)) {
                addWhitelist(clubRealm.getClub(), uuid);
            }
        }
        //update option
        Set<String> originOptions = getClubRealmOption(clubRealm.getClub());
        Set<String> options = clubRealm.getOptions();
        for (String option : originOptions) {
            if (!options.contains(option)) {
                removeOption(clubRealm.getClub(), option);
            }
        }
        for (String option : options) {
            if (!originOptions.contains(option)) {
                addOption(clubRealm.getClub(), option);
            }
        }


    }


    public static void addMember(String clubName, ClubMember clubMember) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.club_member(uuid, club, time, role)
                                        VALUES (?, ?, ?,?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubMember.getUuid().toString());
            preparedStatement.setString(2, clubName);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(clubMember.getTime()));
            preparedStatement.setString(4, clubMember.getRole().toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void removeMember(String clubName, UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    delete from minecraft.club_member where club=? and uuid=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void addWhitelist(String clubName, UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.club_realm_whitelist(uuid, club)
                                        VALUES (?, ?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, clubName);
            preparedStatement.executeUpdate();
        }
    }

    public static void removeWhitelist(String clubName, UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    delete from minecraft.club_realm_whitelist where club=? and uuid=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void addOption(String clubName, String option) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.club_realm_option( club, opt)
                                        VALUES (?, ?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);
            preparedStatement.setString(2, option);
            preparedStatement.executeUpdate();
        }
    }

    public static void removeOption(String clubName, String option) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    delete from minecraft.club_realm_option where club=? and opt=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubName);
            preparedStatement.setString(2, option);
            preparedStatement.executeUpdate();
        }
    }

    public static void createClubRealm(ClubRealm clubRealm) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.club_realm(club, icon, title, description, requirement, rule)
                    values (?, ?, ?, ?, ?, ?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubRealm.getClub());
            preparedStatement.setString(2, clubRealm.getIcon());
            preparedStatement.setString(3, clubRealm.getTitle());
            preparedStatement.setString(4, clubRealm.getDescription());
            preparedStatement.setString(5, clubRealm.getRequirement());
            preparedStatement.setString(6, clubRealm.getRule());
            preparedStatement.executeUpdate();
            for (UUID uuid : clubRealm.getWhitelist()) {
                addWhitelist(clubRealm.getClub(), uuid);
            }
            for (String option : clubRealm.getOptions()) {
                addOption(clubRealm.getClub(), option);
            }
        }
    }

    public static void createClub(Club club) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.club(name, display, description, creation)
                    values (?, ?, ?, ?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, club.getName());
            preparedStatement.setString(2, club.getDisplay());
            preparedStatement.setString(3, club.getDescription());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(club.getCreation()));
            preparedStatement.executeUpdate();
            for (ClubMember value : club.getMembers().values()) {
                addMember(club.getName(), value);
            }
            if (club.getClubRealm() != null) {
                updateClubRealm(club.getClubRealm());
            }

        }
    }

    public static void request(ClubRequest request) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.club_request(uuid, club) VALUES (?,?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, request.getUuid().toString());
            preparedStatement.setString(2, request.getClub());
            preparedStatement.executeUpdate();
        }
    }


    public static void updateRequest(ClubRequest clubRequest) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.club_request set result=? where id=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clubRequest.getResult().toString());
            preparedStatement.setInt(2, clubRequest.getId());
            preparedStatement.executeUpdate();
        }
    }

    public static List<ClubRequest> requests(String club) throws SQLException {
        ArrayList<ClubRequest> clubRequests = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select id, uuid, club, time, result from minecraft.club_request where club=? and result!='DONE' order by time desc
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, club);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ClubRequest clubRequest = new ClubRequest();
                clubRequest.setClub(resultSet.getString("club"));
                clubRequest.setId(resultSet.getInt("id"));
                clubRequest.setLocalDateTime(resultSet.getTimestamp("time").toLocalDateTime());
                clubRequest.setResult(ClubRequest.Result.valueOf(resultSet.getString("result")));
                clubRequests.add(clubRequest);
            }
        }
        return clubRequests;
    }
}
