package com.lithiyana.storysystem;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class StoryNetworking {

    public static void initialize() {

        // Register the packet type.
        //
        // "clientbound" means:
        //
        // SERVER -> CLIENT
        //
        // which is exactly what Story System needs here.
        PayloadTypeRegistry
            .clientboundPlay()
            .register(
                StoryDataPayload.TYPE,
                StoryDataPayload.CODEC
            );


        // Whenever a player joins, send the current Story Point
        // data for every online player to them.
        //
        // We also send the joining player's data to everybody
        // already online.
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

        StoryDataPayload payload =
            new StoryDataPayload(
                storyPlayer.getUUID(),
                storyPoints
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