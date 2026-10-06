package com.lithiyana.storysystem;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {

    public static final Item STORY_TOKEN = register(
        "story_token",
        Item::new,
        new Item.Properties()
            .stacksTo(64)
            .rarity(Rarity.UNCOMMON)
    );

    private static <T extends Item> T register(
        String name,
        Function<Item.Properties, T> itemFactory,
        Item.Properties properties
    ) {

        ResourceKey<Item> itemKey = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(
                StorySystem.MOD_ID,
                name
            )
        );

        T item = itemFactory.apply(
            properties.setId(itemKey)
        );

        Registry.register(
            BuiltInRegistries.ITEM,
            itemKey,
            item
        );

        return item;
    }

    public static void initialize() {
        StorySystem.LOGGER.info("Registering Story System items!");
    }
}