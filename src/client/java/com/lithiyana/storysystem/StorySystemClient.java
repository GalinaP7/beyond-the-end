package com.lithiyana.storysystem;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class StorySystemClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // =====================================================
        // RECEIVE STORY DATA FROM THE SERVER
        // =====================================================

        ClientPlayNetworking.registerGlobalReceiver(
            StoryDataPayload.TYPE,

            (payload, context) -> {

                ClientStoryData.setStoryPoints(
                    payload.playerUUID(),
                    payload.storyPoints()
                );


                ClientStoryData.setEquippedTitle(
                    payload.playerUUID(),
                    payload.equippedTitleId()
                );
            }
        );


        // =====================================================
        // CLEAR CACHED DATA WHEN LEAVING A SERVER
        // =====================================================

        // Prevent Story data from one server/world hanging
        // around after the client disconnects.
        ClientPlayConnectionEvents.DISCONNECT.register(
            (handler, client) -> {

                ClientStoryData.clear();
            }
        );
    }
}