package com.lithiyana.storysystem.mixin;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerNameTagMixin {

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
        if (state.nameTagAttachment == null || state.nameTag == null) {
            return;
        }

        Component storyLine = Component.literal("TEST LINE");

        collector.submitNameTag(
            poseStack,
            state.nameTagAttachment,
            10,
            storyLine,
            !state.isDiscrete,
            state.lightCoords,
            cameraState
        );
    }
}