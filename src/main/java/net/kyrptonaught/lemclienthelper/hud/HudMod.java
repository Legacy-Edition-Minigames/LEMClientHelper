package net.kyrptonaught.lemclienthelper.hud;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.hud.armorHud.ArmorHudPacket;
import net.kyrptonaught.lemclienthelper.hud.armorHud.ArmorHudRenderer;
import net.kyrptonaught.lemclienthelper.hud.glideHud.packets.GlideHudPacket;
import net.kyrptonaught.lemclienthelper.hud.glideHud.GlideHudRenderer;
import net.kyrptonaught.lemclienthelper.hud.glideHud.packets.GlideTimerPacket;
import net.kyrptonaught.lemclienthelper.hud.glideHud.packets.GlideTimerTogglePacket;

public class HudMod {
    public static String MOD_ID = "hud";

    public static boolean SHOULD_RENDER_ARMOR = false;

    public static boolean SHOULD_RENDER_GLIDE = false;

    public static boolean GLIDE_SCORE_ATTACK = false;

    public static boolean TIMER_RUNNING = false;

    public static int FINAL_TIME;

    public static int ELAPSED_TIME = 0;


    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new HudConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);
        //register hud's here
        HudRenderCallback.EVENT.register(ArmorHudRenderer::onHudRender);
        HudRenderCallback.EVENT.register(GlideHudRenderer::onHudRender);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_ARMOR = false);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_GLIDE = false);

        PayloadTypeRegistry.playS2C().register(ArmorHudPacket.PACKET_ID, ArmorHudPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(ArmorHudPacket.PACKET_ID, ((payload, context) -> SHOULD_RENDER_ARMOR = payload.enabled()));

        PayloadTypeRegistry.playS2C().register(GlideHudPacket.PACKET_ID, GlideHudPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(GlideHudPacket.PACKET_ID, ((payload, context) -> {
            SHOULD_RENDER_GLIDE = payload.enabled();
            GLIDE_SCORE_ATTACK = payload.score();
        }));

        PayloadTypeRegistry.playS2C().register(GlideTimerTogglePacket.PACKET_ID, GlideTimerTogglePacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(GlideTimerTogglePacket.PACKET_ID, ((payload, context) -> TIMER_RUNNING = payload.enabled()));

        // Was going to include a reset timer packet, but you can also send a timer set packet of 0 before round start.
        PayloadTypeRegistry.playS2C().register(GlideTimerPacket.PACKET_ID, GlideTimerPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(GlideTimerPacket.PACKET_ID, ((payload, context) -> FINAL_TIME = payload.ticks()));

        ClientTickEvents.END_CLIENT_TICK.register(event -> {
            if (TIMER_RUNNING) {
                ELAPSED_TIME = ELAPSED_TIME + 1; //This feels like a poor way of doing things.
            }
        });

        /*
        // Debug commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                        dispatcher.register(CommandManager.literal(LEMClientHelperMod.MOD_ID)
                        .then(CommandManager.literal("glide")
                                .then(CommandManager.argument("target", EntityArgumentType.players())
                                        .then(CommandManager.literal("timer")
                                            .then(CommandManager.literal("start")
                                                    .executes(context -> {
                                                        ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"), new GlideTimerTogglePacket(true));
                                                        return 0;
                                                    }))
                                            .then(CommandManager.literal("stop")
                                                    .executes(context -> {
                                                        ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"), new GlideTimerTogglePacket(false));
                                                        return 0;
                                                    }))
                                            .then(CommandManager.literal("set")
                                                    .then(CommandManager.argument("ticks", IntegerArgumentType.integer())
                                                            .executes(context -> {
                                                                ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"), new GlideTimerPacket(IntegerArgumentType.getInteger(context, "ticks")));
                                                                return 0;
                                                            }))))
                                    .then(CommandManager.literal("hud")
                                            .then(CommandManager.argument("toggle", BoolArgumentType.bool())
                                                    .then(CommandManager.argument("score", BoolArgumentType.bool())
                                                        .executes( context -> {
                                                            ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"), new GlideHudPacket(BoolArgumentType.getBool(context, "toggle"), BoolArgumentType.getBool(context, "score")));
                                                            return 0;
                                                    }))))))));
        */
    }

    public static boolean shouldDisplayArmor() {
        return getConfig().alwaysEnabled || SHOULD_RENDER_ARMOR;
    }

    public static boolean shouldDisplayGlide() {
        return getConfig().glideAlwaysEnabled || SHOULD_RENDER_GLIDE;
    }

    public static HudConfig getConfig() {
        return (HudConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
