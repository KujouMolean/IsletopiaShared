package com.molean.isletopia.shared.utils;

import com.molean.isletopia.shared.model.Island;
import com.molean.isletopia.shared.model.IslandId;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class IslandUtils {

    public static String getDisplayInfo(Locale locale, Island island) {

        IslandId islandId = island.getIslandId();
        String name = island.getName();
        if (name == null) {
            name = I18n.getMessage(locale, "island.display.noName");;
        }
        StringBuilder str = new StringBuilder(I18n.getMessage(locale, "island.display.name", Pair.of("name", name), Pair.of("id", island.getId() + "")) + "\n");
        str.append(I18n.getMessage(locale, "island.display.owner")).append(UUIDManager.get(island.getUuid())).append("\n");
        if (island.getMembers().size() > 0) {
            str.append(I18n.getMessage(locale, "island.display.members")).append("\n");
            int cnt = 0;
            ArrayList<UUID> uuids = new ArrayList<>(island.getMembers());
            StringBuilder line = new StringBuilder();
            line.append(" §7- ");
            for (int i = 0; i < uuids.size() && i < 12; i++) {
                line.append(UUIDManager.get(uuids.get(i)));
                line.append("  ");
                cnt++;
                if (cnt % 3 == 0 || cnt == uuids.size()) {
                    str.append(line).append("\n");
                    line = new StringBuilder();
                    line.append(" §7- ");
                }
            }
            if (island.getMembers().size() > 12) {
                str.append(I18n.getMessage(locale, "island.display.members.more", Pair.of("size", island.getMembers().size() + ""))).append("\n");
            }
        }
        if (island.getIslandFlags().size() != 0) {
            str.append(I18n.getMessage(locale, "island.display.flags")).append("\n");
            ArrayList<String> strings = new ArrayList<>(island.getIslandFlags());
            for (int i = 0; i < strings.size() && i < 4; i++) {
                str.append(" §7- ").append(strings.get(i)).append("\n");
            }
            if (strings.size() > 4) {
                str.append(I18n.getMessage(locale, "island.display.flags.more", Pair.of("size", island.getIslandFlags().size() + ""))).append("\n");
            }

        }

        Timestamp creation = island.getCreation();
        String format = creation.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        str.append(I18n.getMessage(locale, "island.display.creation")).append(format).append("\n");
        str.append(I18n.getMessage(locale, "island.display.location", Pair.of("server", "%s"), Pair.of("x", "%d"), Pair.of("z", "%d")).formatted(islandId.getServer(), islandId.getX(), islandId.getZ())).append("\n");
        if (island.containsFlag("Lock")) {
            str.append(I18n.getMessage(locale, "island.display.lock"));
        }
        return str.toString();
    }

}
