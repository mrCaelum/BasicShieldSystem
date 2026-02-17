package com.mrcaelum.basicshieldsystem;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.mrcaelum.basicshieldsystem.listeners.PlayerListener;
import com.mrcaelum.basicshieldsystem.systems.FlatShieldDamageSystem;
import com.mrcaelum.basicshieldsystem.systems.ShieldHudUpdateSystem;
import com.mrcaelum.basicshieldsystem.ui.ShieldHud;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * BasicShieldSystem - Basic Shield System for Hytale
 *
 * @author mrCaelum
 * @version 0.0.1
 */
public class BasicShieldSystemPlugin extends JavaPlugin {
    private static final int SHIELD_STAT_INDEX =
            EntityStatType.getAssetMap().getIndex("Shield");
    Map<PlayerRef, ShieldHud> playerRefShieldHudMap = new HashMap<>();
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static BasicShieldSystemPlugin instance;

    public BasicShieldSystemPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static BasicShieldSystemPlugin getInstance() {
        return instance;
    }

    @Override
    protected void setup() {
        LOGGER.at(Level.INFO).log("Setting up...");
        try {
            new PlayerListener().register(getEventRegistry());
            LOGGER.at(Level.INFO).log("Registered player event listeners");
        } catch (Exception e) {
            LOGGER.at(Level.WARNING).withCause(e).log("Failed to register listeners");
        }

        getEntityStoreRegistry().registerSystem(new FlatShieldDamageSystem(
                SHIELD_STAT_INDEX
        ));
        this.getEntityStoreRegistry().registerSystem(
                new ShieldHudUpdateSystem(
                        SHIELD_STAT_INDEX,
                        playerRefShieldHudMap
                )
        );
        LOGGER.at(Level.INFO).log("Setup complete!");
    }

    @Override
    protected void start() {
        LOGGER.at(Level.INFO).log("Started!");
    }

    @Override
    protected void shutdown() {
        LOGGER.at(Level.INFO).log("Shutting down...");
        instance = null;
    }
}