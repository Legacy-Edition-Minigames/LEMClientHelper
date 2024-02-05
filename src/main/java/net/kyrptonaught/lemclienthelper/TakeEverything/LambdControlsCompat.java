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
    private static final MinecraftClient clientInstance = MinecraftClient.getInstance();
    /**
     * Using a static variable for the client's instance to avoid resource leak in
     * the take everything bool, though I'm not sure if checking for the
     * player is totally nessisary.
     */

    public static void register() {
        MidnightControlsCompat.registerCompatHandler(new LambdControlsCompat());
    }

    public static boolean takeEverything() {
        if (clientInstance.player != null) {
            TakeEverythingNetworking.sendTakeEverythingPacket();
            return true;
        }
        return false;
    }


    @Override
    public void handle(@NotNull MidnightControlsClient mod) {
        new ButtonBinding.Builder(LEMClientHelperMod.MOD_ID + ".takeeverything")
                .category(ButtonBinding.INVENTORY_CATEGORY)        
                .buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB)
                .action((client,action,value,buttonState)->takeEverything())
                .onlyInInventory()
                .filter(((client, buttonBinding) -> client.currentScreen instanceof GenericContainerScreen || 
                                                    client.currentScreen instanceof Generic3x3ContainerScreen ||
                                                    client.currentScreen instanceof ShulkerBoxScreen ))
                .register();                        /**
                                                     * Filter limits the screens where the keybind is active
                                                     * Currently can be used in: Barrels/Chests, Shulkers, & Hoppers/Droppers
                                                     * This can be expanded to allow for more inventory screens,
                                                     * However I don't think that we will often need to send
                                                     * a take everything packet for a brewing stand/beacon
                                                     */
    }
}