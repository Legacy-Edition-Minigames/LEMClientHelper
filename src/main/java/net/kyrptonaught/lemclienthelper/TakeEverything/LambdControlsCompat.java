package net.kyrptonaught.lemclienthelper.TakeEverything;
/*
import dev.lambdaurora.lambdacontrols.client.LambdaControlsClient;
import dev.lambdaurora.lambdacontrols.client.compat.CompatHandler;
import dev.lambdaurora.lambdacontrols.client.compat.LambdaControlsCompat;
import dev.lambdaurora.lambdacontrols.client.controller.ButtonBinding;
import dev.lambdaurora.lambdacontrols.client.controller.PressAction;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;


public class LambdControlsCompat implements CompatHandler {

    public static void register() {
        LambdaControlsCompat.registerCompatHandler(new LambdControlsCompat());
    }

    private static final PressAction TAKE_EVERYTHING = (client, button, value, action) -> {
        if (client.currentScreen != null && client.player != null && !(client.player.currentScreenHandler instanceof PlayerScreenHandler)) {
            TakeEverythingNetworking.sendSortPacket();
            return true;
        }
        return false;
    };

    @Override
    public void handle(@NotNull LambdaControlsClient mod) {
        new ButtonBinding.Builder(new Identifier(LEMClientHelperMod.MOD_ID, "takeeverything"))
                .buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB)
                .onlyInInventory()
                .action(TAKE_EVERYTHING)
                .cooldown()
                .category(ButtonBinding.INVENTORY_CATEGORY)
                //.linkKeybind(LEBClientHelperMod.takeEverythingKey)
                .register();
    }
}

 */