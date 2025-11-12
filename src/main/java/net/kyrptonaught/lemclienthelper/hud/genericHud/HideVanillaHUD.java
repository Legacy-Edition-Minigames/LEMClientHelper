package net.kyrptonaught.lemclienthelper.hud.genericHud;

import java.util.HashMap;

public class HideVanillaHUD {

    /**
     * <pre>
     * ALL - All Elements
     * EXPERIENCE - Experience Bar/Locator Bar
     * HEARTS - Player health
     * HOTBAR - Player hotbar
     * HUNGER - Player hunger
     * STATS - EXPERIENCE, HEARTS, &, HUNGER
     * </pre>
     */
    public enum HUD_ELEMENT {
        ALL,
        EXPERIENCE,
        HEARTS,
        HOTBAR,
        HUNGER,
        STATS
    }

    public static HashMap<HUD_ELEMENT, Boolean> visible;

}
