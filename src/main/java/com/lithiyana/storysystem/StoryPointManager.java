package com.lithiyana.storysystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.loader.api.FabricLoader;

public class StoryPointManager {

    // Current lifetime Story Points for each player.
    private static final Map<UUID, Integer> storyPoints = new HashMap<>();

    // Number of 10-SP Token milestones already paid.
    private static final Map<UUID, Integer> tokenMilestonesPaid = new HashMap<>();

    private static final Path DATA_FOLDER =
        FabricLoader.getInstance()
            .getConfigDir()
            .resolve("storysystem");

    private static final Path DATA_FILE =
        DATA_FOLDER.resolve("story_points.txt");


    // =========================================================
    // GETTERS
    // =========================================================

    public static int getStoryPoints(UUID playerUUID) {
        return storyPoints.getOrDefault(playerUUID, 0);
    }

    public static int getTokenMilestonesPaid(UUID playerUUID) {
        return tokenMilestonesPaid.getOrDefault(playerUUID, 0);
    }


    // =========================================================
    // STORY POINT CHANGES
    // =========================================================

    public static void addStoryPoints(UUID playerUUID, int amount) {

        int currentPoints =
            getStoryPoints(playerUUID);

        storyPoints.put(
            playerUUID,
            currentPoints + amount
        );

        save();
    }

    public static void setStoryPoints(UUID playerUUID, int amount) {

        storyPoints.put(
            playerUUID,
            Math.max(0, amount)
        );

        save();
    }

    public static void removeStoryPoints(UUID playerUUID, int amount) {

        int currentPoints =
            getStoryPoints(playerUUID);

        int newTotal =
            Math.max(0, currentPoints - amount);

        storyPoints.put(
            playerUUID,
            newTotal
        );

        save();
    }


    // =========================================================
    // TOKEN MILESTONE TRACKING
    // =========================================================

    public static int getNewTokenMilestones(UUID playerUUID) {

        int currentSP =
            getStoryPoints(playerUUID);

        int milestonesReached =
            currentSP / 10;

        int milestonesAlreadyPaid =
            getTokenMilestonesPaid(playerUUID);

        return Math.max(
            0,
            milestonesReached - milestonesAlreadyPaid
        );
    }


    public static void markTokenMilestonesPaid(UUID playerUUID) {

        int currentSP =
            getStoryPoints(playerUUID);

        int milestonesReached =
            currentSP / 10;

        int oldMilestonesPaid =
            getTokenMilestonesPaid(playerUUID);

        // Never move milestone progress backwards.
        tokenMilestonesPaid.put(
            playerUUID,
            Math.max(
                oldMilestonesPaid,
                milestonesReached
            )
        );

        save();
    }


    /*
     * DEVELOPMENT / ADMIN TOOL
     *
     * Completely forget which Token milestones
     * have already been paid.
     *
     * Example:
     *
     * SP = 40
     * milestones paid = 4
     *
     * reset ->
     *
     * SP = 40
     * milestones paid = 0
     *
     * Be careful:
     * future SP additions could now award those
     * old milestones again.
     */
    public static void resetTokenMilestones(UUID playerUUID) {

        tokenMilestonesPaid.put(
            playerUUID,
            0
        );

        save();
    }


    /*
     * SAFE ADMIN REPAIR TOOL
     *
     * Marks every Token milestone through the
     * player's CURRENT SP as already paid.
     *
     * Example:
     *
     * SP = 47
     *
     * 47 / 10 = 4
     *
     * milestones paid becomes 4.
     */
    public static void syncTokenMilestones(UUID playerUUID) {

        int currentSP =
            getStoryPoints(playerUUID);

        tokenMilestonesPaid.put(
            playerUUID,
            currentSP / 10
        );

        save();
    }


    // =========================================================
    // SAVE
    // =========================================================

    public static void save() {

        try {

            Files.createDirectories(DATA_FOLDER);

            StringBuilder data =
                new StringBuilder();

            for (
                Map.Entry<UUID, Integer> entry
                    : storyPoints.entrySet()
            ) {

                UUID playerUUID =
                    entry.getKey();

                int points =
                    entry.getValue();

                int milestonesPaid =
                    getTokenMilestonesPaid(playerUUID);

                data.append(playerUUID)
                    .append("=")
                    .append(points)
                    .append("=")
                    .append(milestonesPaid)
                    .append("\n");
            }

            Files.writeString(
                DATA_FILE,
                data.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );

            StorySystem.LOGGER.info(
                "Saved Story Points!"
            );

        } catch (IOException e) {

            StorySystem.LOGGER.error(
                "Could not save Story Points!",
                e
            );
        }
    }


    // =========================================================
    // LOAD
    // =========================================================

    public static void load() {

        if (!Files.exists(DATA_FILE)) {

            StorySystem.LOGGER.info(
                "No Story Points file found. Starting fresh!"
            );

            return;
        }

        try {

            storyPoints.clear();
            tokenMilestonesPaid.clear();

            for (
                String line
                    : Files.readAllLines(DATA_FILE)
            ) {

                if (line.isBlank()) {
                    continue;
                }

                String[] parts =
                    line.split("=");

                UUID playerUUID =
                    UUID.fromString(parts[0]);

                int points =
                    Integer.parseInt(parts[1]);

                storyPoints.put(
                    playerUUID,
                    points
                );


                /*
                 * NEW FORMAT:
                 *
                 * UUID=SP=MILESTONES_PAID
                 *
                 * OLD FORMAT:
                 *
                 * UUID=SP
                 */
                if (parts.length >= 3) {

                    int milestonesPaid =
                        Integer.parseInt(parts[2]);

                    tokenMilestonesPaid.put(
                        playerUUID,
                        milestonesPaid
                    );

                } else {

                    /*
                     * Old save-file compatibility.
                     *
                     * Existing SP is treated as already
                     * accounted for when upgrading.
                     */
                    tokenMilestonesPaid.put(
                        playerUUID,
                        points / 10
                    );
                }
            }


            StorySystem.LOGGER.info(
                "Loaded Story Points for "
                + storyPoints.size()
                + " player(s)!"
            );

        } catch (Exception e) {

            StorySystem.LOGGER.error(
                "Could not load Story Points!",
                e
            );
        }
    }
}