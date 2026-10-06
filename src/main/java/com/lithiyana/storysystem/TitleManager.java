package com.lithiyana.storysystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TitleManager {

    private static final Path SAVE_FILE =
        Paths.get("config/storysystem/titles.txt");

    // Every player's unlocked titles.
    private static final Map<UUID, Set<Title>> unlockedTitles =
        new HashMap<>();

    // Every player can have AT MOST one equipped title.
    private static final Map<UUID, Title> equippedTitles =
        new HashMap<>();

    public static Set<Title> getUnlockedTitles(UUID playerUUID) {
        Set<Title> titles = unlockedTitles.get(playerUUID);

        if (titles == null) {
            return Collections.emptySet();
        }

        return Collections.unmodifiableSet(titles);
    }

    public static boolean hasTitle(UUID playerUUID, Title title) {
        return unlockedTitles
            .getOrDefault(playerUUID, Collections.emptySet())
            .contains(title);
    }

    public static Title getEquippedTitle(UUID playerUUID) {
        return equippedTitles.get(playerUUID);
    }

    public static boolean unlockTitle(UUID playerUUID, Title title) {
        Set<Title> titles = unlockedTitles.computeIfAbsent(
            playerUUID,
            uuid -> EnumSet.noneOf(Title.class)
        );

        boolean newlyUnlocked = titles.add(title);

        if (newlyUnlocked) {
            save();
        }

        return newlyUnlocked;
    }

    public static boolean equipTitle(UUID playerUUID, Title title) {

        // RULE #1:
        // You cannot equip a title you have not earned.
        if (!hasTitle(playerUUID, title)) {
            return false;
        }

        // RULE #2:
        // There is only one equipped-title slot.
        // put() automatically replaces the previous title.
        equippedTitles.put(playerUUID, title);

        save();
        return true;
    }

    public static void clearEquippedTitle(UUID playerUUID) {
        if (equippedTitles.remove(playerUUID) != null) {
            save();
        }
    }

    public static boolean revokeTitle(UUID playerUUID, Title title) {
        Set<Title> titles = unlockedTitles.get(playerUUID);

        if (titles == null || !titles.remove(title)) {
            return false;
        }

        // If the revoked title was equipped, unequip it too.
        if (equippedTitles.get(playerUUID) == title) {
            equippedTitles.remove(playerUUID);
        }

        if (titles.isEmpty()) {
            unlockedTitles.remove(playerUUID);
        }

        save();
        return true;
    }

    public static void save() {
        try {
            Files.createDirectories(SAVE_FILE.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(SAVE_FILE)) {

                for (Map.Entry<UUID, Set<Title>> entry
                        : unlockedTitles.entrySet()) {

                    UUID playerUUID = entry.getKey();
                    Set<Title> titles = entry.getValue();
                    Title equipped = equippedTitles.get(playerUUID);

                    StringBuilder unlockedIds = new StringBuilder();

                    for (Title title : titles) {
                        if (unlockedIds.length() > 0) {
                            unlockedIds.append(",");
                        }

                        unlockedIds.append(title.getId());
                    }

                    String equippedId =
                        equipped == null ? "none" : equipped.getId();

                    writer.write(
                        playerUUID
                            + "="
                            + unlockedIds
                            + "="
                            + equippedId
                    );

                    writer.newLine();
                }
            }

        } catch (IOException e) {
            StorySystem.LOGGER.error(
                "Could not save Story System titles!",
                e
            );
        }
    }

    public static void load() {
        unlockedTitles.clear();
        equippedTitles.clear();

        if (!Files.exists(SAVE_FILE)) {
            StorySystem.LOGGER.info(
                "No Story System title save file found yet."
            );
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(SAVE_FILE)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", -1);

                if (parts.length != 3) {
                    StorySystem.LOGGER.warn(
                        "Skipping invalid title save line: {}",
                        line
                    );
                    continue;
                }

                try {
                    UUID playerUUID = UUID.fromString(parts[0]);

                    Set<Title> titles =
                        EnumSet.noneOf(Title.class);

                    if (!parts[1].isBlank()) {
                        String[] titleIds = parts[1].split(",");

                        for (String titleId : titleIds) {
                            Title title = Title.fromId(titleId);

                            if (title != null) {
                                titles.add(title);
                            }
                        }
                    }

                    if (!titles.isEmpty()) {
                        unlockedTitles.put(playerUUID, titles);
                    }

                    if (!parts[2].equalsIgnoreCase("none")) {
                        Title equipped =
                            Title.fromId(parts[2]);

                        // Don't allow corrupted save data to equip
                        // a title the player doesn't own.
                        if (equipped != null
                                && titles.contains(equipped)) {
                            equippedTitles.put(
                                playerUUID,
                                equipped
                            );
                        }
                    }

                } catch (IllegalArgumentException e) {
                    StorySystem.LOGGER.warn(
                        "Skipping invalid title save line: {}",
                        line
                    );
                }
            }

            StorySystem.LOGGER.info(
                "Loaded titles for {} player(s)!",
                unlockedTitles.size()
            );

        } catch (IOException e) {
            StorySystem.LOGGER.error(
                "Could not load Story System titles!",
                e
            );
        }
    }
}