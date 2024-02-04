package net.kyrptonaught.lemclienthelper.TakeEverything;

import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.compat.CompatHandler;
import eu.midnightdust.midnightcontrols.client.compat.MidnightControlsCompat;
import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;



public class LambdControlsCompat implements CompatHandler {

    public static void register() {
        MidnightControlsCompat.registerCompatHandler(new LambdControlsCompat());
    }

    public static boolean takeEverything() {
        if (MinecraftClient.getInstance().player != null) {
            TakeEverythingNetworking.sendTakeEverythingPacket();
            return true;
        }
        return false;
    }


    @Override
    public void handle(@NotNull MidnightControlsClient mod) {
        new ButtonBinding.Builder(LEMClientHelperMod.MOD_ID + ".takeeverything")
                .buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB)
                .onlyInInventory()
                .category(ButtonBinding.INVENTORY_CATEGORY)
                .action((client,action,value,buttonState)->takeEverything()).cooldown()
                .filter(((client, buttonBinding) -> client.currentScreen instanceof GenericContainerScreen || 
                                                    client.currentScreen instanceof Generic3x3ContainerScreen ||
                                                    client.currentScreen instanceof ShulkerBoxScreen ))
                .register();
    }
}