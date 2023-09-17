package net.kyrptonaught.lemclienthelper.hud;

import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

public class HudConfig implements AbstractConfigFile {
    public boolean enabled = true;

    public boolean alwaysEnabled = false;

    public boolean glideHudAlwaysEnabled = false;

    public boolean glideTimerRunning = false;

    public float armorHudScale = 1;

    public float glideHudScale = 1;

    public int xOffset = 20;
}
