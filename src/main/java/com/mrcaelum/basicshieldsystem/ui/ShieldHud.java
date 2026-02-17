package com.mrcaelum.basicshieldsystem.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ShieldHud extends CustomUIHud {

    public ShieldHud(@NonNullDecl PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    public void build(UICommandBuilder builder) {
        builder.append("Hud/Shield/Shield.ui");
        setShieldValue(0.0f, builder);
    }

    public void setShieldValue(float shieldValue, UICommandBuilder builder) {
        builder.set("#ShieldBar.Visible", shieldValue > 0);
        builder.set("#ProgressBarShield.Value", shieldValue);
    }
}
