package com.backmod.command;

import com.backmod.event.DeathEventHandler;
import com.backmod.event.DeathEventHandler.DeathLocation;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class BackCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("back")
                .executes(BackCommand::execute)
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("§cThis command can only be used by a player."));
            return 0;
        }

        UUID uuid = player.getUUID();
        DeathLocation death = DeathEventHandler.DEATH_LOCATIONS.get(uuid);

        if (death == null) {
            source.sendFailure(Component.literal("§cNo death location found. You haven't died yet!"));
            return 0;
        }

        ServerLevel targetLevel = source.getServer().getLevel(death.dimension());

        if (targetLevel == null) {
            source.sendFailure(Component.literal(
                "§cCould not find the dimension you died in: §f" + death.dimension().location()
            ));
            return 0;
        }

        player.teleportTo(
            targetLevel,
            death.x(),
            death.y(),
            death.z(),
            player.getYRot(),
            player.getXRot()
        );

        source.sendSuccess(() -> Component.literal(
            "§aTeleported to your last death location: §f"
            + (int) death.x() + ", " + (int) death.y() + ", " + (int) death.z()
            + " §ain §f" + death.dimension().location()
        ), false);

        DeathEventHandler.DEATH_LOCATIONS.remove(uuid);

        return 1;
    }
}
