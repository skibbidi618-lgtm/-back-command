package com.backmod;

import com.backmod.command.BackCommand;
import com.backmod.event.DeathEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

public class BackMod implements ModInitializer {

    public static final String MOD_ID = "backmod";

    @Override
    public void onInitialize() {
        ServerLivingEntityEvents.AFTER_DEATH.register(DeathEventHandler::onDeath);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            BackCommand.register(dispatcher)
        );
    }
}
