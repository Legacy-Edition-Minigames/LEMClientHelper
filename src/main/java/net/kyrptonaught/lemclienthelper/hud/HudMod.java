package net.kyrptonaught.lemclienthelper.hud;

import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.hud.armorHud.ArmorHudMod;
import net.kyrptonaught.lemclienthelper.hud.genericHud.GenericHudMod;
import net.kyrptonaught.lemclienthelper.hud.glideHud.GlideHudMod;
import wily.legacy.Legacy4JClient;

public class HudMod {
    public static String MOD_ID = "hud";

    public static void onInitialize(){
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new HudConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);

        GenericHudMod.onInitialize();
        ArmorHudMod.onInitialize();
        GlideHudMod.onInitialize();
    }


    public static HudConfig getConfig() {
        return (HudConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
