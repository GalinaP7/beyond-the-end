package com.lithiyana.storysystem;

import java.util.UUID;

public interface StoryAvatarRenderState {

    UUID storysystem$getPlayerUUID();

    void storysystem$setPlayerUUID(UUID playerUUID);
}