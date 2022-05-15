package net.kyrptonaught.lemclienthelper.SmallInv;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.kyrptonaught.lebclienthelper.LEBClientHelperMod;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class SmallInvInit {
    public static ScreenHandlerType<SmallInvScreenHandler> LEGACY_INVENTORY_SCREEN_HANDLER;

    public static void init() {
        LEGACY_INVENTORY_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(LEBClientHelperMod.MOD_ID, "legacy_inventory"), SmallInvScreenHandler::new);
    }
}
