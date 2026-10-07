package com.lithiyana.storysystem;

import java.util.UUID;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record StoryDataPayload(
    UUID playerUUID,
    int storyPoints,
    String equippedTitleId
) implements CustomPacketPayload {

    public static final Type<StoryDataPayload> TYPE =
        new Type<>(StorySystem.id("story_data"));


    /*
     * Minecraft 26.2 does not provide a ready-made
     * ByteBufCodecs.UUID constant.
     *
     * RegistryFriendlyByteBuf already knows how to
     * read and write UUIDs, so we define the tiny
     * UUID codec ourselves.
     */
    private static final StreamCodec<
        RegistryFriendlyByteBuf,
        UUID
    > UUID_CODEC = StreamCodec.of(
        (buffer, uuid) -> buffer.writeUUID(uuid),
        buffer -> buffer.readUUID()
    );


    public static final StreamCodec<
        RegistryFriendlyByteBuf,
        StoryDataPayload
    > CODEC = StreamCodec.composite(

        UUID_CODEC,
        StoryDataPayload::playerUUID,

        ByteBufCodecs.VAR_INT,
        StoryDataPayload::storyPoints,

        ByteBufCodecs.STRING_UTF8,
        StoryDataPayload::equippedTitleId,

        StoryDataPayload::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}