package com.mrcaelum.basicshieldsystem.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public final class FlatShieldDamageSystem extends DamageEventSystem {

    private final int shieldStatIndex;
    private final ComponentType<EntityStore, EntityStatMap> STAT_MAP;

    public FlatShieldDamageSystem(int shieldStatIndex) {
        this.shieldStatIndex = shieldStatIndex;
        this.STAT_MAP = EntityStatMap.getComponentType();
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(STAT_MAP);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> chunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull Damage damage
    ) {
        float incomingDamage = damage.getAmount();
        if (incomingDamage <= 0f) {
            return;
        }

        EntityStatMap stats = chunk.getComponent(index, STAT_MAP);
        if (stats == null) {
            return;
        }

        EntityStatValue shield = stats.get(shieldStatIndex);
        if (shield == null) {
            return;
        }

        float currentShield = shield.get();
        if (currentShield <= 0f) {
            return;
        }

        float absorbed = Math.min(incomingDamage, currentShield);

        stats.subtractStatValue(
                EntityStatMap.Predictable.NONE,
                shieldStatIndex,
                absorbed
        );

        damage.setAmount(incomingDamage - absorbed);
    }
}
