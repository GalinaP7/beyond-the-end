package com.lithiyana.storysystem.mixin;

import com.lithiyana.storysystem.ClientStoryData;
import com.lithiyana.storysystem.RankManager;
import com.lithiyana.storysystem.StoryAvatarRenderState;

import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Avatar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerNameTagMixin {

    // =========================================================
    // COPY THE PLAYER UUID INTO THE RENDER STATE
    // =========================================================

    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
        at = @At("TAIL")
    )
    private void storysystem$storePlayerUUID(
        Avatar avatar,
        AvatarRenderState state,
        float partialTick,
        CallbackInfo ci
    ) {

        ((StoryAvatarRenderState) state)
            .storysystem$setPlayerUUID(
                avatar.getUUID()
            );
    }


    // =========================================================
    // MOVE THE ENTIRE NAMETAG BLOCK UP
    // =========================================================

    @Inject(
        method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At("HEAD")
    )
    private void storysystem$raiseNameTag(
        AvatarRenderState state,
        PoseStack poseStack,
        SubmitNodeCollector collector,
        CameraRenderState cameraState,
        CallbackInfo ci
    ) {

        poseStack.pushPose();

        // Moves BOTH the username and Story System line upward.
        poseStack.translate(
            0.0,
            0.15,
            0.0
        );
    }


    // =========================================================
    // RENDER STORY SYSTEM NAMETAG LINE
    // =========================================================

    @Inject(
        method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At("TAIL")
    )
    private void storysystem$renderStoryLine(
        AvatarRenderState state,
        PoseStack poseStack,
        SubmitNodeCollector collector,
        CameraRenderState cameraState,
        CallbackInfo ci
    ) {

        if (
            state.nameTagAttachment == null
            || state.nameTag == null
        ) {
            return;
        }


        UUID playerUUID =
            ((StoryAvatarRenderState) state)
                .storysystem$getPlayerUUID();


        if (playerUUID == null) {
            return;
        }


        int storyPoints =
            ClientStoryData.getStoryPoints(
                playerUUID
            );

        String rank =
            RankManager.getRank(
                storyPoints
            );


        Component storyLine =
            Component.literal(rank)
                .withStyle(
                    RankManager.getRankColor(rank)
                )
                .append(
                    Component.literal(
                        " ✦ "
                    ).withStyle(
                        ChatFormatting.GOLD
                    )
                )
                .append(
                    Component.literal(
                        String.valueOf(storyPoints)
                    ).withStyle(
                        ChatFormatting.GOLD
                    )
                )
                .append(
                    Component.literal(
                        " SP"
                    ).withStyle(
                        ChatFormatting.GRAY
                    )
                );


        collector.submitNameTag(
            poseStack,
            state.nameTagAttachment,

            // Keep this at 10.
            // This controls the gap between the username
            // and the Story System line.
            10,

            storyLine,
            !state.isDiscrete,
            state.lightCoords,
            cameraState
        );
    }


    // =========================================================
    // RESTORE THE POSE STACK
    // =========================================================

    @Inject(
        method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At("RETURN")
    )
    private void storysystem$restoreNameTagPosition(
        AvatarRenderState state,
        PoseStack poseStack,
        SubmitNodeCollector collector,
        CameraRenderState cameraState,
        CallbackInfo ci
    ) {

        poseStack.popPose();
    }
}