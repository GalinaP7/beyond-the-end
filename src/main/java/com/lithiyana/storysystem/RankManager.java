package com.lithiyana.storysystem;

import net.minecraft.ChatFormatting;

public class RankManager {

    public static String getRank(int storyPoints) {

        if (storyPoints >= 10000) {
            return "Mythic";
        }

        if (storyPoints >= 5000) {
            return "Legend";
        }

        if (storyPoints >= 3000) {
            return "Champion";
        }

        if (storyPoints >= 2000) {
            return "Hero";
        }

        if (storyPoints >= 1000) {
            return "Pathfinder";
        }

        if (storyPoints >= 500) {
            return "Explorer";
        }

        if (storyPoints >= 300) {
            return "Adventurer";
        }

        if (storyPoints >= 100) {
            return "Wanderer";
        }

        return "Unranked";
    }

    public static ChatFormatting getRankColor(String rank) {

        return switch (rank) {
            case "Wanderer" -> ChatFormatting.GREEN;
            case "Adventurer" -> ChatFormatting.AQUA;
            case "Explorer" -> ChatFormatting.BLUE;
            case "Pathfinder" -> ChatFormatting.DARK_PURPLE;
            case "Hero" -> ChatFormatting.GOLD;
            case "Champion" -> ChatFormatting.LIGHT_PURPLE;
            case "Legend" -> ChatFormatting.YELLOW;
            case "Mythic" -> ChatFormatting.RED;
            default -> ChatFormatting.GRAY;
        };
    }
}