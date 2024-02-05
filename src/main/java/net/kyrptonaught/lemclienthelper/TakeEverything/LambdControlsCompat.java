package net.kyrptonaught.lemclienthelper.TakeEverything;

import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.compat.CompatHandler;
import eu.midnightdust.midnightcontrols.client.compat.MidnightControlsCompat;
import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class LambdControlsCompat implements CompatHandler {

    public static void register() {
        MidnightControlsCompat.registerCompatHandler(new LambdControlsCompat());
    }

    public static boolean takeEverything() {
        TakeEverythingNetworking.sendTakeEverythingPacket();
        return true;
    }


    @Override
    public void handle(@NotNull MidnightControlsClient mod) {
        new ButtonBinding.Builder(LEMClientHelperMod.MOD_ID + ".takeeverything")
                .category(ButtonBinding.INVENTORY_CATEGORY)
                .buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB)
                .action((client, action, value, buttonState) -> takeEverything())
                .onlyInInventory()
                .register();
    }
}