package com.lithiyana.storysystem;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class StoryNetworking {

    public static void initialize() {

        // Register Story System's server -> client packet.
        PayloadTypeRegistry
            .clientboundPlay()
            .register(
                StoryDataPayload.TYPE,
                StoryDataPayload.CODEC
            );


        // When somebody joins:
        //
        // 1. Send every online player's Story data to them.
        // 2. Send their Story data to everybody already online.
        ServerPlayConnectionEvents.JOIN.register(
            (handler, sender, server) -> {

                ServerPlayer joiningPlayer =
                    handler.player;

                syncAllToPlayer(
                    server,
                    joiningPlayer
                );

                syncPlayerToEveryone(
                    server,
                    joiningPlayer
                );
            }
        );
    }


    // =========================================================
    // SEND ONE PLAYER'S STORY DATA TO ONE CLIENT
    // =========================================================

    public static void syncPlayerTo(
        ServerPlayer receiver,
        ServerPlayer storyPlayer
    ) {

        int storyPoints =
            StoryPointManager.getStoryPoints(
                storyPlayer.getUUID()
            );


        Title equippedTitle =
            TitleManager.getEquippedTitle(
                storyPlayer.getUUID()
            );


        String equippedTitleId =
            equippedTitle == null
                ? "none"
                : equippedTitle.getId();


        StoryDataPayload payload =
            new StoryDataPayload(
                storyPlayer.getUUID(),
                storyPoints,
                equippedTitleId
            );


        ServerPlayNetworking.send(
            receiver,
            payload
        );
    }


    // =========================================================
    // SEND ONE PLAYER'S STORY DATA TO EVERY ONLINE CLIENT
    // =========================================================

    public static void syncPlayerToEveryone(
        MinecraftServer server,
        ServerPlayer storyPlayer
    ) {

        for (
            ServerPlayer receiver
                : server.getPlayerList().getPlayers()
        ) {

            syncPlayerTo(
                receiver,
                storyPlayer
            );
        }
    }


    // =========================================================
    // SEND ALL ONLINE PLAYERS TO ONE CLIENT
    // =========================================================

    public static void syncAllToPlayer(
        MinecraftServer server,
        ServerPlayer receiver
    ) {

        for (
            ServerPlayer storyPlayer
                : server.getPlayerList().getPlayers()
        ) {

            syncPlayerTo(
                receiver,
                storyPlayer
            );
        }
    }
}