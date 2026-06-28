package net.kyrptonaught.lemclienthelper.AutoGG;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;

// AutoGG implemented by Terra <3

public class AutoGGMod {
    public static String MOD_ID = "autogg";

    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new AutoGGConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {onClientTick();});
    }

    public static AutoGGConfig getConfig() {
        return (AutoGGConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }

    private static long lastGG = 0;
    private static int ticksUntilGG = -1;

    public static void queueGG(int delayTicks) {
        ticksUntilGG = delayTicks;
    }

    public static void onClientTick() {
        if (ticksUntilGG < 0) return;

        ticksUntilGG --;

        if (ticksUntilGG == 0) {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null) {
                client.player.networkHandler.sendChatMessage(getConfig().message);
            }

            ticksUntilGG = -1;
        }
    }

    public static void onSoundPlayed(Identifier soundId) {
        if (soundId == null) return;

        if (!getConfig().enabled) return;

        String id = soundId.toString();

        if (!id.equals("lem.base:sound.game.win") && !id.equals("lem.base:sound.game.lose")) return;

        long time = System.currentTimeMillis();

        if (time - lastGG < 5000) return;

        lastGG = time;

        queueGG(getConfig().delay);
    }
}