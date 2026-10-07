package com.lithiyana.storysystem.mixin;
import com.lithiyana.storysystem.StoryAvatarRenderState;

import java.util.UUID;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public abstract class AvatarRenderStateMixin
    implements StoryAvatarRenderState {

    @Unique
    private UUID storysystem$playerUUID;


    @Override
    public UUID storysystem$getPlayerUUID() {
        return storysystem$playerUUID;
    }


    @Override
    public void storysystem$setPlayerUUID(UUID playerUUID) {
        this.storysystem$playerUUID = playerUUID;
    }
}