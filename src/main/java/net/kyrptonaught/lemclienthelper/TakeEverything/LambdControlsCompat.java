package net.kyrptonaught.lemclienthelper.TakeEverything;

import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.compat.CompatHandler;
import eu.midnightdust.midnightcontrols.client.compat.MidnightControlsCompat;
import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import eu.midnightdust.midnightcontrols.client.controller.PressAction;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.screen.PlayerScreenHandler;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;


public class LambdControlsCompat implements CompatHandler {

    public static void register() {
        MidnightControlsCompat.registerCompatHandler(new LambdControlsCompat());
    }

    private static final PressAction TAKE_EVERYTHING = (client, button, value, action) -> {
        if (client.currentScreen != null && client.player != null && !(client.player.currentScreenHandler instanceof PlayerScreenHandler)) {
            TakeEverythingNetworking.sendTakeEverythingPacket();
            return true;
        }
        return false;
    };

    @Override
    public void handle(@NotNull MidnightControlsClient mod) {
        new ButtonBinding.Builder(LEMClientHelperMod.MOD_ID + ".takeeverything")
                .buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB)
                .onlyInInventory()
                .action(TAKE_EVERYTHING)
                .cooldown()
                .category(ButtonBinding.INVENTORY_CATEGORY)
                //.linkKeybind(LEBClientHelperMod.takeEverythingKey)
                .register();
    }
}