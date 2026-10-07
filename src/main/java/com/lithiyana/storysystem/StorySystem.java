package com.lithiyana.storysystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.item.ItemStack;

public class StorySystem implements ModInitializer {

    public static final String MOD_ID = "storysystem";

    public static final Logger LOGGER =
        LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {

        LOGGER.info("STORY SYSTEM IS ALIVE!");

        ModItems.initialize();

        StoryPointManager.load();

        // Load all saved title data.
        TitleManager.load();

		StoryNetworking.initialize();


        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> {


                // =========================================================
                // /sp COMMANDS
                // =========================================================

                dispatcher.register(

                    Commands.literal("sp")


                        // -------------------------------------------------
                        // /sp
                        // PUBLIC
                        // -------------------------------------------------

                        .executes(context -> {

                            var player =
                                context.getSource()
                                    .getPlayerOrException();

                            int sp =
                                StoryPointManager.getStoryPoints(
                                    player.getUUID()
                                );

                            String rank =
                                RankManager.getRank(sp);


                            context.getSource().sendSuccess(

                                () -> Component.literal(
                                    "✦ STORY BOARD ✦"
                                ).withStyle(
                                    ChatFormatting.GOLD
                                ),

                                false
                            );


                            context.getSource().sendSuccess(

                                () -> Component.literal(
                                    "⭐ Story Points: "
                                )
                                .withStyle(
                                    ChatFormatting.YELLOW
                                )
                                .append(
                                    Component.literal(
                                        String.valueOf(sp)
                                    ).withStyle(
                                        ChatFormatting.GOLD
                                    )
                                ),

                                false
                            );


                            context.getSource().sendSuccess(

                                () -> Component.literal(
                                    "⚔ Rank: "
                                )
                                .withStyle(
                                    ChatFormatting.GRAY
                                )
                                .append(
                                    Component.literal(rank)
                                        .withStyle(
                                            RankManager
                                                .getRankColor(rank)
                                        )
                                ),

                                false
                            );


                            return 1;
                        })


                        // -------------------------------------------------
                        // /sp add <player> <amount>
                        // ADMIN ONLY
                        //
                        // Real SP reward.
                        // CAN award Tokens.
                        // CAN announce rank-ups.
                        // -------------------------------------------------

                        .then(

                            Commands.literal("add")

                                .requires(
                                    StorySystem::isAdmin
                                )

                                .then(

                                    Commands.argument(
                                        "player",
                                        EntityArgument.player()
                                    )

                                    .then(

                                        Commands.argument(
                                            "amount",
                                            integer(1)
                                        )

                                        .executes(context -> {

                                            var target =
                                                EntityArgument.getPlayer(
                                                    context,
                                                    "player"
                                                );

                                            int amount =
                                                getInteger(
                                                    context,
                                                    "amount"
                                                );

                                            // BEFORE the SP change.

                                            int oldSP =
                                                StoryPointManager
                                                    .getStoryPoints(
                                                        target.getUUID()
                                                    );

                                            String oldRank =
                                                RankManager.getRank(
                                                    oldSP
                                                );


                                            // Add SP.

                                            StoryPointManager
                                                .addStoryPoints(
                                                    target.getUUID(),
                                                    amount
                                                );


                                            // AFTER the SP change.

                                            int newTotal =
                                                StoryPointManager
                                                    .getStoryPoints(
                                                        target.getUUID()
                                                    );

											StoryNetworking.syncPlayerToEveryone(
												context.getSource().getServer(),
												target
											);

                                            String newRank =
                                                RankManager.getRank(
                                                    newTotal
                                                );


                                            // ---------------------------------
                                            // AUTOMATIC STORY TOKENS
                                            // ---------------------------------

                                            int tokensEarned =
                                                StoryPointManager
                                                    .getNewTokenMilestones(
                                                        target.getUUID()
                                                    );


                                            if (tokensEarned > 0) {

                                                giveStoryTokens(
                                                    target,
                                                    tokensEarned
                                                );

                                                StoryPointManager
                                                    .markTokenMilestonesPaid(
                                                        target.getUUID()
                                                    );


                                                target.sendSystemMessage(

                                                    Component.literal(
                                                        "🪙 +"
                                                        + tokensEarned
                                                        + " Story Token"
                                                        + (
                                                            tokensEarned == 1
                                                                ? ""
                                                                : "s"
                                                        )
                                                        + "!"
                                                    ).withStyle(
                                                        ChatFormatting.GOLD
                                                    )
                                                );
                                            }


                                            // ---------------------------------
                                            // SP confirmation
                                            // ---------------------------------

                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    "✦ +"
                                                    + amount
                                                    + " STORY POINTS ✦"
                                                ).withStyle(
                                                    ChatFormatting.GREEN
                                                ),

                                                false
                                            );


                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    target.getName()
                                                        .getString()
                                                )
                                                .withStyle(
                                                    ChatFormatting.WHITE
                                                )
                                                .append(
                                                    Component.literal(
                                                        " now has "
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        String.valueOf(
                                                            newTotal
                                                        )
                                                    ).withStyle(
                                                        ChatFormatting.GOLD
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        " SP."
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                ),

                                                false
                                            );


                                            // ---------------------------------
                                            // RANK-UP
                                            // ---------------------------------

                                            if (
                                                !oldRank.equals(newRank)
                                            ) {

                                                context.getSource()
                                                    .getServer()
                                                    .getPlayerList()
                                                    .broadcastSystemMessage(

                                                        Component.literal(
                                                            "✦ RANK UP! ✦"
                                                        ).withStyle(
                                                            ChatFormatting.GOLD,
                                                            ChatFormatting.BOLD
                                                        ),

                                                        false
                                                    );


                                                context.getSource()
                                                    .getServer()
                                                    .getPlayerList()
                                                    .broadcastSystemMessage(

                                                        Component.literal(
                                                            target.getName()
                                                                .getString()
                                                        )
                                                        .withStyle(
                                                            ChatFormatting.WHITE
                                                        )
                                                        .append(
                                                            Component.literal(
                                                                " has reached "
                                                            ).withStyle(
                                                                ChatFormatting.GRAY
                                                            )
                                                        )
                                                        .append(
                                                            Component.literal(
                                                                newRank
                                                                    .toUpperCase()
                                                            ).withStyle(
                                                                RankManager
                                                                    .getRankColor(
                                                                        newRank
                                                                    ),
                                                                ChatFormatting.BOLD
                                                            )
                                                        )
                                                        .append(
                                                            Component.literal(
                                                                "!"
                                                            ).withStyle(
                                                                ChatFormatting.GOLD
                                                            )
                                                        ),

                                                        false
                                                    );
                                            }


                                            return 1;
                                        })
                                    )
                                )
                        )


                        // -------------------------------------------------
                        // /sp remove <player> <amount>
                        // ADMIN ONLY
                        //
                        // Does NOT award Tokens.
                        // -------------------------------------------------

                        .then(

                            Commands.literal("remove")

                                .requires(
                                    StorySystem::isAdmin
                                )

                                .then(

                                    Commands.argument(
                                        "player",
                                        EntityArgument.player()
                                    )

                                    .then(

                                        Commands.argument(
                                            "amount",
                                            integer(1)
                                        )

                                        .executes(context -> {

                                            var target =
                                                EntityArgument.getPlayer(
                                                    context,
                                                    "player"
                                                );

                                            int amount =
                                                getInteger(
                                                    context,
                                                    "amount"
                                                );


                                            StoryPointManager
                                                .removeStoryPoints(
                                                    target.getUUID(),
                                                    amount
                                                );


                                            int newTotal =
                                                StoryPointManager
                                                    .getStoryPoints(
                                                        target.getUUID()
                                                    );

											StoryNetworking.syncPlayerToEveryone(
												context.getSource().getServer(),
												target
											);


                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    "✦ -"
                                                    + amount
                                                    + " STORY POINTS ✦"
                                                ).withStyle(
                                                    ChatFormatting.RED
                                                ),

                                                false
                                            );


                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    target.getName()
                                                        .getString()
                                                )
                                                .withStyle(
                                                    ChatFormatting.WHITE
                                                )
                                                .append(
                                                    Component.literal(
                                                        " now has "
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        String.valueOf(
                                                            newTotal
                                                        )
                                                    ).withStyle(
                                                        ChatFormatting.GOLD
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        " SP."
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                ),

                                                false
                                            );

                                            return 1;
                                        })
                                    )
                                )
                        )


                        // -------------------------------------------------
                        // /sp set <player> <amount>
                        // ADMIN ONLY
                        //
                        // Does NOT award Tokens.
                        // -------------------------------------------------

                        .then(

                            Commands.literal("set")

                                .requires(
                                    StorySystem::isAdmin
                                )

                                .then(

                                    Commands.argument(
                                        "player",
                                        EntityArgument.player()
                                    )

                                    .then(

                                        Commands.argument(
                                            "amount",
                                            integer(0)
                                        )

                                        .executes(context -> {

                                            var target =
                                                EntityArgument.getPlayer(
                                                    context,
                                                    "player"
                                                );

                                            int amount =
                                                getInteger(
                                                    context,
                                                    "amount"
                                                );


                                            StoryPointManager
                                                .setStoryPoints(
                                                    target.getUUID(),
                                                    amount
                                                );


                                            int newTotal =
                                                StoryPointManager
                                                    .getStoryPoints(
                                                        target.getUUID()
                                                    );

											StoryNetworking.syncPlayerToEveryone(
												context.getSource().getServer(),
												target
											);

                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    "✦ STORY POINTS UPDATED ✦"
                                                ).withStyle(
                                                    ChatFormatting.AQUA
                                                ),

                                                false
                                            );


                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    target.getName()
                                                        .getString()
                                                )
                                                .withStyle(
                                                    ChatFormatting.WHITE
                                                )
                                                .append(
                                                    Component.literal(
                                                        " now has "
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        String.valueOf(
                                                            newTotal
                                                        )
                                                    ).withStyle(
                                                        ChatFormatting.GOLD
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        " SP."
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                ),

                                                false
                                            );


                                            return 1;
                                        })
                                    )
                                )
                        )
                );


                // =========================================================
                // /token COMMANDS
                // =========================================================

                dispatcher.register(

                    Commands.literal("token")


                        // -------------------------------------------------
                        // /token give <player> <amount>
                        // ADMIN ONLY
                        // -------------------------------------------------

                        .then(

                            Commands.literal("give")

                                .requires(
                                    StorySystem::isAdmin
                                )

                                .then(

                                    Commands.argument(
                                        "player",
                                        EntityArgument.player()
                                    )

                                    .then(

                                        Commands.argument(
                                            "amount",
                                            integer(1)
                                        )

                                        .executes(context -> {

                                            var target =
                                                EntityArgument.getPlayer(
                                                    context,
                                                    "player"
                                                );

                                            int amount =
                                                getInteger(
                                                    context,
                                                    "amount"
                                                );


                                            giveStoryTokens(
                                                target,
                                                amount
                                            );


                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    "🪙 Gave "
                                                    + amount
                                                    + " Story Token"
                                                    + (
                                                        amount == 1
                                                            ? ""
                                                            : "s"
                                                    )
                                                    + " to "
                                                    + target.getName()
                                                        .getString()
                                                    + "."
                                                ).withStyle(
                                                    ChatFormatting.GOLD
                                                ),

                                                false
                                            );


                                            return 1;
                                        })
                                    )
                                )
                        )


                        // -------------------------------------------------
                        // /token resetmilestones <player>
                        // ADMIN ONLY
                        // -------------------------------------------------

                        .then(

                            Commands.literal(
                                "resetmilestones"
                            )

                            .requires(
                                StorySystem::isAdmin
                            )

                            .then(

                                Commands.argument(
                                    "player",
                                    EntityArgument.player()
                                )

                                .executes(context -> {

                                    var target =
                                        EntityArgument.getPlayer(
                                            context,
                                            "player"
                                        );


                                    StoryPointManager
                                        .resetTokenMilestones(
                                            target.getUUID()
                                        );


                                    context.getSource().sendSuccess(

                                        () -> Component.literal(
                                            "🪙 Token milestone history "
                                            + "for "
                                            + target.getName()
                                                .getString()
                                            + " was reset to 0."
                                        ).withStyle(
                                            ChatFormatting.YELLOW
                                        ),

                                        false
                                    );


                                    return 1;
                                })
                            )
                        )


                        // -------------------------------------------------
                        // /token syncmilestones <player>
                        // ADMIN ONLY
                        // -------------------------------------------------

                        .then(

                            Commands.literal(
                                "syncmilestones"
                            )

                            .requires(
                                StorySystem::isAdmin
                            )

                            .then(

                                Commands.argument(
                                    "player",
                                    EntityArgument.player()
                                )

                                .executes(context -> {

                                    var target =
                                        EntityArgument.getPlayer(
                                            context,
                                            "player"
                                        );

                                    StoryPointManager
                                        .syncTokenMilestones(
                                            target.getUUID()
                                        );


                                    int paid =
                                        StoryPointManager
                                            .getTokenMilestonesPaid(
                                                target.getUUID()
                                            );


                                    context.getSource().sendSuccess(

                                        () -> Component.literal(
                                            "🪙 "
                                            + target.getName()
                                                .getString()
                                            + "'s Token milestones "
                                            + "were synced to "
                                            + paid
                                            + "."
                                        ).withStyle(
                                            ChatFormatting.AQUA
                                        ),

                                        false
                                    );


                                    return 1;
                                })
                            )
                        )
                );


                // =========================================================
                // /titles COMMANDS
                // =========================================================

                dispatcher.register(

                    Commands.literal("titles")


                        // -------------------------------------------------
                        // /titles
                        // PUBLIC
                        //
                        // Shows your equipped and unlocked titles.
                        // -------------------------------------------------

                        .executes(context -> {

                            var player =
                                context.getSource()
                                    .getPlayerOrException();


                            Title equipped =
                                TitleManager.getEquippedTitle(
                                    player.getUUID()
                                );


                            context.getSource().sendSuccess(

                                () -> Component.literal(
                                    "✦ YOUR TITLES ✦"
                                ).withStyle(
                                    ChatFormatting.GOLD
                                ),

                                false
                            );


                            if (equipped == null) {

                                context.getSource().sendSuccess(

                                    () -> Component.literal(
                                        "Equipped: None"
                                    ).withStyle(
                                        ChatFormatting.GRAY
                                    ),

                                    false
                                );

                            } else {

                                context.getSource().sendSuccess(

                                    () -> Component.literal(
                                        "Equipped: "
                                    ).withStyle(
                                        ChatFormatting.GRAY
                                    )
                                    .append(
                                        Component.literal(
                                            "["
                                            + equipped.getDisplayName()
                                            + "]"
                                        ).withStyle(
                                            equipped.getColor()
                                        )
                                    ),

                                    false
                                );
                            }


                            var unlocked =
                                TitleManager.getUnlockedTitles(
                                    player.getUUID()
                                );


                            if (unlocked.isEmpty()) {

                                context.getSource().sendSuccess(

                                    () -> Component.literal(
                                        "Unlocked: None"
                                    ).withStyle(
                                        ChatFormatting.DARK_GRAY
                                    ),

                                    false
                                );

                            } else {

                                context.getSource().sendSuccess(

                                    () -> Component.literal(
                                        "Unlocked:"
                                    ).withStyle(
                                        ChatFormatting.GRAY
                                    ),

                                    false
                                );


                                for (Title title : unlocked) {

                                    context.getSource().sendSuccess(

                                        () -> Component.literal(
                                            " • "
                                        )
                                        .withStyle(
                                            ChatFormatting.DARK_GRAY
                                        )
                                        .append(
                                            Component.literal(
                                                title.getDisplayName()
                                            ).withStyle(
                                                title.getColor()
                                            )
                                        ),

                                        false
                                    );
                                }
                            }


                            return 1;
                        })


                        // -------------------------------------------------
                        // /titles equip <title>
                        // PUBLIC
                        //
                        // Can ONLY equip an unlocked title.
                        // Equipping replaces the previous title.
                        // -------------------------------------------------

                        .then(

                            Commands.literal("equip")

                                .then(

                                    Commands.argument(
                                        "title",
                                        word()
                                    )

                                    .suggests(
                                        (context, builder) -> {

                                            var player =
                                                context.getSource()
                                                    .getPlayerOrException();


                                            for (
                                                Title title
                                                : TitleManager
                                                    .getUnlockedTitles(
                                                        player.getUUID()
                                                    )
                                            ) {

                                                builder.suggest(
                                                    title.getId()
                                                );
                                            }


                                            return builder.buildFuture();
                                        }
                                    )

                                    .executes(context -> {

                                        var player =
                                            context.getSource()
                                                .getPlayerOrException();

                                        String titleId =
                                            getString(
                                                context,
                                                "title"
                                            );

                                        Title title =
                                            Title.fromId(
                                                titleId
                                            );


                                        if (title == null) {

                                            context.getSource()
                                                .sendFailure(

                                                    Component.literal(
                                                        "That title does "
                                                        + "not exist."
                                                    ).withStyle(
                                                        ChatFormatting.RED
                                                    )
                                                );

                                            return 0;
                                        }


                                        if (
                                            !TitleManager.hasTitle(
                                                player.getUUID(),
                                                title
                                            )
                                        ) {

                                            context.getSource()
                                                .sendFailure(

                                                    Component.literal(
                                                        "You have not "
                                                        + "unlocked ["
                                                        + title
                                                            .getDisplayName()
                                                        + "]."
                                                    ).withStyle(
                                                        ChatFormatting.RED
                                                    )
                                                );

                                            return 0;
                                        }


                                        TitleManager.equipTitle(
                                            player.getUUID(),
                                            title
                                        );

										// Immediately update everyone's nametag data.
										StoryNetworking.syncPlayerToEveryone(
											context.getSource().getServer(),
											player
										);


                                        context.getSource().sendSuccess(

                                            () -> Component.literal(
                                                "⚔ Equipped "
                                            ).withStyle(
                                                ChatFormatting.GRAY
                                            )
                                            .append(
                                                Component.literal(
                                                    "["
                                                    + title.getDisplayName()
                                                    + "]"
                                                ).withStyle(
                                                    title.getColor()
                                                )
                                            )
                                            .append(
                                                Component.literal(
                                                    "!"
                                                ).withStyle(
                                                    ChatFormatting.GRAY
                                                )
                                            ),

                                            false
                                        );


                                        return 1;
                                    })
                                )
                        )


                        // -------------------------------------------------
                        // /titles clear
                        // PUBLIC
                        //
                        // Unequips your current title.
                        // -------------------------------------------------

                        .then(

                            Commands.literal("clear")

                                .executes(context -> {

                                    var player =
                                        context.getSource()
                                            .getPlayerOrException();


                                    Title equipped =
                                        TitleManager
                                            .getEquippedTitle(
                                                player.getUUID()
                                            );


                                    if (equipped == null) {

                                        context.getSource()
                                            .sendFailure(

                                                Component.literal(
                                                    "You do not have "
                                                    + "a title equipped."
                                                ).withStyle(
                                                    ChatFormatting.RED
                                                )
                                            );

                                        return 0;
                                    }


                                    TitleManager
                                        .clearEquippedTitle(
                                            player.getUUID()
                                        );

									// Immediately remove the title from everyone's nametag.
									StoryNetworking.syncPlayerToEveryone(
										context.getSource().getServer(),
										player
									);


                                    context.getSource().sendSuccess(

                                        () -> Component.literal(
                                            "⚔ Title cleared."
                                        ).withStyle(
                                            ChatFormatting.GRAY
                                        ),

                                        false
                                    );


                                    return 1;
                                })
                        )


                        // -------------------------------------------------
                        // /titles give <player> <title>
                        // ADMIN ONLY
                        //
                        // Unlocks a title for a player.
                        // -------------------------------------------------

                        .then(

                            Commands.literal("give")

                                .requires(
                                    StorySystem::isAdmin
                                )

                                .then(

                                    Commands.argument(
                                        "player",
                                        EntityArgument.player()
                                    )

                                    .then(

                                        Commands.argument(
                                            "title",
                                            word()
                                        )

                                        .suggests(
                                            (context, builder) -> {

                                                for (
                                                    Title title
                                                    : Title.values()
                                                ) {

                                                    builder.suggest(
                                                        title.getId()
                                                    );
                                                }


                                                return builder
                                                    .buildFuture();
                                            }
                                        )

                                        .executes(context -> {

                                            var target =
                                                EntityArgument.getPlayer(
                                                    context,
                                                    "player"
                                                );

                                            String titleId =
                                                getString(
                                                    context,
                                                    "title"
                                                );

                                            Title title =
                                                Title.fromId(
                                                    titleId
                                                );


                                            if (title == null) {

                                                context.getSource()
                                                    .sendFailure(

                                                        Component.literal(
                                                            "That title "
                                                            + "does not "
                                                            + "exist."
                                                        ).withStyle(
                                                            ChatFormatting.RED
                                                        )
                                                    );

                                                return 0;
                                            }


                                            boolean newlyUnlocked =
                                                TitleManager.unlockTitle(
                                                    target.getUUID(),
                                                    title
                                                );


                                            if (!newlyUnlocked) {

                                                context.getSource()
                                                    .sendFailure(

                                                        Component.literal(
                                                            target.getName()
                                                                .getString()
                                                            + " already "
                                                            + "has ["
                                                            + title
                                                                .getDisplayName()
                                                            + "]."
                                                        ).withStyle(
                                                            ChatFormatting.RED
                                                        )
                                                    );

                                                return 0;
                                            }


                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    "⚔ Gave "
                                                    + target.getName()
                                                        .getString()
                                                    + " the title "
                                                ).withStyle(
                                                    ChatFormatting.GRAY
                                                )
                                                .append(
                                                    Component.literal(
                                                        "["
                                                        + title
                                                            .getDisplayName()
                                                        + "]"
                                                    ).withStyle(
                                                        title.getColor()
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        "."
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                ),

                                                false
                                            );


                                            target.sendSystemMessage(

                                                Component.literal(
                                                    "✦ TITLE UNLOCKED! ✦"
                                                ).withStyle(
                                                    ChatFormatting.GOLD,
                                                    ChatFormatting.BOLD
                                                )
                                            );


                                            target.sendSystemMessage(

                                                Component.literal(
                                                    "You unlocked "
                                                ).withStyle(
                                                    ChatFormatting.GRAY
                                                )
                                                .append(
                                                    Component.literal(
                                                        "["
                                                        + title
                                                            .getDisplayName()
                                                        + "]"
                                                    ).withStyle(
                                                        title.getColor()
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        "!"
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                )
                                            );


                                            return 1;
                                        })
                                    )
                                )
                        )


                        // -------------------------------------------------
                        // /titles revoke <player> <title>
                        // ADMIN ONLY
                        //
                        // Removes a title from a player.
                        // If it was equipped, it is automatically cleared.
                        // -------------------------------------------------

                        .then(

                            Commands.literal("revoke")

                                .requires(
                                    StorySystem::isAdmin
                                )

                                .then(

                                    Commands.argument(
                                        "player",
                                        EntityArgument.player()
                                    )

                                    .then(

                                        Commands.argument(
                                            "title",
                                            word()
                                        )

                                        .suggests(
                                            (context, builder) -> {

                                                for (
                                                    Title title
                                                    : Title.values()
                                                ) {

                                                    builder.suggest(
                                                        title.getId()
                                                    );
                                                }


                                                return builder
                                                    .buildFuture();
                                            }
                                        )

                                        .executes(context -> {

                                            var target =
                                                EntityArgument.getPlayer(
                                                    context,
                                                    "player"
                                                );

                                            String titleId =
                                                getString(
                                                    context,
                                                    "title"
                                                );

                                            Title title =
                                                Title.fromId(
                                                    titleId
                                                );


                                            if (title == null) {

                                                context.getSource()
                                                    .sendFailure(

                                                        Component.literal(
                                                            "That title "
                                                            + "does not "
                                                            + "exist."
                                                        ).withStyle(
                                                            ChatFormatting.RED
                                                        )
                                                    );

                                                return 0;
                                            }


                                            boolean removed =
                                                TitleManager.revokeTitle(
                                                    target.getUUID(),
                                                    title
                                                );

                                            if (!removed) {

                                                context.getSource()
                                                    .sendFailure(

                                                        Component.literal(
                                                            target.getName()
                                                                .getString()
                                                            + " does not "
                                                            + "have ["
                                                            + title
                                                                .getDisplayName()
                                                            + "]."
                                                        ).withStyle(
                                                            ChatFormatting.RED
                                                        )
                                                    );

                                                return 0;
                                            }

											// The revoked title may have been equipped,
											// so update everyone's nametag data.
											StoryNetworking.syncPlayerToEveryone(
												context.getSource().getServer(),
												target
											);

                                            context.getSource().sendSuccess(

                                                () -> Component.literal(
                                                    "⚔ Revoked "
                                                ).withStyle(
                                                    ChatFormatting.GRAY
                                                )
                                                .append(
                                                    Component.literal(
                                                        "["
                                                        + title
                                                            .getDisplayName()
                                                        + "]"
                                                    ).withStyle(
                                                        title.getColor()
                                                    )
                                                )
                                                .append(
                                                    Component.literal(
                                                        " from "
                                                        + target.getName()
                                                            .getString()
                                                        + "."
                                                    ).withStyle(
                                                        ChatFormatting.GRAY
                                                    )
                                                ),

                                                false
                                            );


                                            return 1;
                                        })
                                    )
                                )
                        )
                );
            }
        );
    }


    // =========================================================
    // ADMIN PERMISSION HELPER
    // =========================================================

    private static boolean isAdmin(
        net.minecraft.commands.CommandSourceStack source
    ) {

        return source.permissions().hasPermission(
            Permissions.COMMANDS_GAMEMASTER
        );
    }


    // =========================================================
    // PHYSICAL TOKEN DELIVERY
    // =========================================================

    private static void giveStoryTokens(
        net.minecraft.server.level.ServerPlayer player,
        int amount
    ) {

        int remaining = amount;


        /*
         * Story Tokens currently stack to 64.
         *
         * Creating separate stacks here means this helper
         * still works if an admin gives more than 64 Tokens.
         */
        while (remaining > 0) {

            int stackSize =
                Math.min(
                    remaining,
                    ModItems.STORY_TOKEN
                        .getDefaultMaxStackSize()
                );


            ItemStack tokenStack =
                new ItemStack(
                    ModItems.STORY_TOKEN,
                    stackSize
                );


            boolean completelyAdded =
                player.getInventory()
                    .add(tokenStack);


            /*
             * Minecraft may modify tokenStack when it adds
             * items to the inventory.
             *
             * If anything remains afterward, the inventory
             * couldn't hold it, so drop the remainder at
             * the player's feet.
             */
            if (
                !completelyAdded
                && !tokenStack.isEmpty()
            ) {

                player.drop(
                    tokenStack,
                    false
                );
            }


            remaining -= stackSize;
        }
    }


    public static Identifier id(String path) {

        return Identifier.fromNamespaceAndPath(
            MOD_ID,
            path
        );
    }
}