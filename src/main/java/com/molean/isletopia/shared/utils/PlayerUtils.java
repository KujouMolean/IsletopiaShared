package com.molean.isletopia.shared.utils;

import com.molean.isletopia.shared.database.AchievementDao;
import com.molean.isletopia.shared.database.ClubDao;
import com.molean.isletopia.shared.database.IslandDao;
import com.molean.isletopia.shared.model.Club;
import com.molean.isletopia.shared.model.ClubMember;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class PlayerUtils {
    public static String getDisplay(UUID uuid) {

        String s = null;
        String format = null;
        Integer count = null;
        String club = null;
        int score = 0;
        try {
            s = UUIDManager.get(uuid);
            LocalDateTime playerFirstIslandCreation = IslandDao.getPlayerFirstIslandCreation(uuid);
            format = "未知";
            if (playerFirstIslandCreation != null) {
                format = playerFirstIslandCreation.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            }
            count = IslandDao.countIslandByPlayer(uuid);
            club = "无";
            Set<Club> joinedClubs = ClubDao.getJoinedClubs(uuid);

            if (joinedClubs.size() > 0) {
                ArrayList<String> strings = new ArrayList<>();
                for (Club joinedClub : joinedClubs) {
                    ClubMember clubMember = joinedClub.getMembers().get(uuid);
                    strings.add(joinedClub.getDisplay() + "(" + clubMember.getRole().toString() + ")");
                }
                club = String.join(",", strings);
            }

            score = AchievementDao.getAchievementScores(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return """
                §f%s
                §7加入时间: %s
                §7岛屿数量: %s
                §7参与社团: %s
                §7成就积分: %d
                """.formatted(s, format, count, club, score);
    }
}
