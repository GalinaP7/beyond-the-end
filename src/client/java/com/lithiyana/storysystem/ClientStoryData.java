package com.lithiyana.storysystem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientStoryData {

    // =========================================================
    // CLIENT-SIDE STORY DATA
    // =========================================================

    private static final Map<UUID, Integer> storyPoints =
        new HashMap<>();

    private static final Map<UUID, String> equippedTitles =
        new HashMap<>();


    // =========================================================
    // STORY POINTS
    // =========================================================

    public static void setStoryPoints(
        UUID playerUUID,
        int amount
    ) {

        storyPoints.put(
            playerUUID,
            amount
        );
    }


    public static int getStoryPoints(
        UUID playerUUID
    ) {

        return storyPoints.getOrDefault(
            playerUUID,
            0
        );
    }


    // =========================================================
    // EQUIPPED TITLES
    // =========================================================

    public static void setEquippedTitle(
        UUID playerUUID,
        String titleId
    ) {

        if (
            titleId == null
            || titleId.equals("none")
        ) {

            equippedTitles.remove(
                playerUUID
            );

            return;
        }


        equippedTitles.put(
            playerUUID,
            titleId
        );
    }


    public static Title getEquippedTitle(
        UUID playerUUID
    ) {

        String titleId =
            equippedTitles.get(
                playerUUID
            );


        if (titleId == null) {
            return null;
        }


        return Title.fromId(
            titleId
        );
    }


    // =========================================================
    // CLEAR CLIENT CACHE
    // =========================================================

    public static void clear() {

        storyPoints.clear();
        equippedTitles.clear();
    }
}