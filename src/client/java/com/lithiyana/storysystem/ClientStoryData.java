package com.lithiyana.storysystem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientStoryData {

    private static final Map<UUID, Integer> storyPoints =
        new HashMap<>();


    public static void setStoryPoints(
        UUID playerUUID,
        int amount
    ) {

        storyPoints.put(
            playerUUID,
            amount
        );
    }


    public static int getStoryPoints(UUID playerUUID) {

        return storyPoints.getOrDefault(
            playerUUID,
            0
        );
    }


    public static void clear() {
        storyPoints.clear();
    }
}