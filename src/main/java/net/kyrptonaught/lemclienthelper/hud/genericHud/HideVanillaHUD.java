package net.kyrptonaught.lemclienthelper.hud.glideHud;

import java.util.HashMap;

import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HideVanillaHUD {
    public static final enum HUD_ELEMENT {
        ALL,
        EXPERIENCE,
        HEARTS,
        HOTBAR,
        HUNGER,
        STATS
    }

    public static HashMap<HUD_ELEMENT, Boolean> visible;

    init {
        visible.clear();
        HUD_ELEMENT.forEach(element -> 
            visibe.putIfAbsent(element, true)
        );
    }
}
