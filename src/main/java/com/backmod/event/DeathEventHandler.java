package com.backmod.event;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathEventHandler {

    public record DeathLocation(ResourceKey<Level> dimension, double x, double y, double z) {}

    public static final Map<UUID, DeathLocation> DEATH_LOCATIONS = new HashMap<>();

    public static void onDeath(LivingEntity entity, DamageSource damageSource) {
        if (!(entity instanceof ServerPlayer player)) return;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        ResourceKey<Level> dimension = player.level().dimension();

        DEATH_LOCATIONS.put(player.getUUID(), new DeathLocation(dimension, x, y, z));

        player.sendSystemMessage(Component.literal(
            "§cYou died at §f" + (int) x + ", " + (int) y + ", " + (int) z
            + " §cin §f" + dimension.location()
            + "§c. Use §f/back §cto return."
        ));
    }
}
