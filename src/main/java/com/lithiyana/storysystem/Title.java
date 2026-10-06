package com.lithiyana.storysystem;

import net.minecraft.ChatFormatting;

public enum Title {

    FROGLEAPER(
        "frogleaper",
        "Frogleaper",
        ChatFormatting.GREEN,
        false
    ),

    CARTOGRAPHER(
        "cartographer",
        "Cartographer",
        ChatFormatting.AQUA,
        false
    ),

    ARCHITECT(
        "architect",
        "Architect",
        ChatFormatting.GOLD,
        false
    ),

    OSHA_VIOLATION(
        "osha_violation",
        "OSHA Violation",
        ChatFormatting.RED,
        false
    ),

    PROFESSIONAL_MENACE(
        "professional_menace",
        "Professional Menace",
        ChatFormatting.DARK_PURPLE,
        true
    );

    private final String id;
    private final String displayName;
    private final ChatFormatting color;
    private final boolean hidden;

    Title(
        String id,
        String displayName,
        ChatFormatting color,
        boolean hidden
    ) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.hidden = hidden;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public boolean isHidden() {
        return hidden;
    }

    public static Title fromId(String id) {
        for (Title title : values()) {
            if (title.id.equalsIgnoreCase(id)) {
                return title;
            }
        }

        return null;
    }
}