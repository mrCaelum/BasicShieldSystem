package com.mrcaelum.basicshieldsystem.listeners;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.mrcaelum.basicshieldsystem.ui.ShieldHud;
import com.buuz135.mhud.MultipleHUD;

import java.util.logging.Level;

/**
 * Listener for player connection events.
 *
 * Listens to:
 * - PlayerConnectEvent - When a player connects to the server
 * - PlayerDisconnectEvent - When a player disconnects from the server
 */
public class PlayerListener {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    /**
     * Register all player event listeners.
     * @param eventBus The event registry to register listeners with
     */
    public void register(EventRegistry eventBus) {
        // PlayerReadyEvent - When a player is ready
        try {
            eventBus.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
            LOGGER.at(Level.INFO).log("Registered PlayerReadyEvent listener");
        } catch (Exception e) {
            LOGGER.at(Level.WARNING).withCause(e).log("Failed to register PlayerReadyEvent");
        }
    }

    /**
     * Handle player ready event.
     * @param event The player ready event
     */
    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> playerRefStore = event.getPlayerRef();
        Store<EntityStore> store = playerRefStore.getStore();
        PlayerRef playerRef = store.getComponent(playerRefStore, PlayerRef.getComponentType());

        if (playerRef == null) {
            return;
        }

        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        Player player = store.getComponent(ref, Player.getComponentType());

        if (player == null) {
            return;
        }

        ShieldHud shieldHud = new ShieldHud(playerRef);
        PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD"));
        if (plugin != null) {
            LOGGER.at(Level.INFO).log("MultipleHUD plugin found.");
            MultipleHUD.getInstance().setCustomHud(player, playerRef, "ShieldHUD", shieldHud);
        }
        else {
            LOGGER.at(Level.INFO).log("MultipleHUD plugin not found. Create base custom hud.");
            player.getHudManager().setCustomHud(playerRef, shieldHud);
        }
        shieldHud.show();
    }
}