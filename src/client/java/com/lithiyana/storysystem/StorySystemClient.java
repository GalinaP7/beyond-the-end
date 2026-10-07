package com.lithiyana.storysystem;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class StorySystemClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(
            StoryDataPayload.TYPE,

            (payload, context) -> {

                ClientStoryData.setStoryPoints(
                    payload.playerUUID(),
                    payload.storyPoints()
                );
            }
        );


        // Prevent data from one server/world hanging around
        // after the client disconnects.
        ClientPlayConnectionEvents.DISCONNECT.register(
            (handler, client) -> {
                ClientStoryData.clear();
            }
        );
    }
}