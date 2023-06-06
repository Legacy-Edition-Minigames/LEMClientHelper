package net.kyrptonaught.lemclienthelper.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class HudMod {
    public static void onInitialize() {
        HudRenderCallback.EVENT.register(new ArmorHud());
    }
}
