package net.kyrptonaught.lemclienthelper.hud;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.hud.armorHud.ArmorHudPacket;
import net.kyrptonaught.lemclienthelper.hud.armorHud.ArmorHudRenderer;
import net.kyrptonaught.lemclienthelper.hud.genericHud.packets.PlayerBarPacket;
import net.kyrptonaught.lemclienthelper.hud.glideHud.packets.GlideHudPacket;
import net.kyrptonaught.lemclienthelper.hud.glideHud.packets.GlideScorePacket;
import net.kyrptonaught.lemclienthelper.hud.glideHud.GlideHudRenderer;
import net.kyrptonaught.lemclienthelper.hud.glideHud.packets.GlideTimerPacket;
import net.kyrptonaught.lemclienthelper.hud.glideHud.packets.GlideTimerTogglePacket;

/*
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
*/

public class HudMod {
    public static String MOD_ID = "hud";

    public static boolean SHOULD_RENDER_PLAYERBAR = false;

    public static boolean IN_ROUND = false;

    public static byte[] PLAYER_STATUS;

    public static boolean SHOULD_RENDER_ARMOR = false;

    public static boolean SHOULD_RENDER_GLIDE = false;

    public static boolean GLIDE_SCORE_ATTACK = false;

    public static boolean TIMER_RUNNING = false;

    public static int FINAL_TIME;

    public static int ELAPSED_TIME = 0;

    public static int GLIDE_SCORE = 0;

    public static int GLIDE_LAST_RING = 0;


    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new HudConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);
        //register hud's here
        HudRenderCallback.EVENT.register(ArmorHudRenderer::onHudRender);
        HudRenderCallback.EVENT.register(GlideHudRenderer::onHudRender);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_ARMOR = false);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_GLIDE = false);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_PLAYERBAR = false);

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

        PayloadTypeRegistry.playS2C().register(GlideScorePacket.PACKET_ID, GlideScorePacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(GlideScorePacket.PACKET_ID, ((payload, context) -> {
            GLIDE_SCORE = payload.score();
            GLIDE_LAST_RING = payload.lastRing();
        }));

        PayloadTypeRegistry.playS2C().register(PlayerBarPacket.PACKET_ID, PlayerBarPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(PlayerBarPacket.PACKET_ID, ((payload, context) -> {
            SHOULD_RENDER_PLAYERBAR = payload.enabled();
            IN_ROUND = payload.inRound();
            PLAYER_STATUS = payload.players();
        }));

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
                                                        }))))
                                        .then(CommandManager.literal("score")
                                                .then(CommandManager.literal("set")
                                                        .then(CommandManager.argument("score", IntegerArgumentType.integer())
                                                                .then(CommandManager.argument("lastRing", IntegerArgumentType.integer())
                                                                        .executes(context -> {
                                                                            ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"), new GlideScorePacket(IntegerArgumentType.getInteger(context, "score"), IntegerArgumentType.getInteger(context, "lastRing")));
                                                                            return 0;
                                                                        })))))))));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(CommandManager.literal(LEMClientHelperMod.MOD_ID)
                        .then(CommandManager.literal("examplePlayerBar")
                                .then(CommandManager.argument("target", EntityArgumentType.players())
                                        .then(CommandManager.argument("inRound", BoolArgumentType.bool())
                                        .executes(context -> {
                                            ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"),
                                                    new PlayerBarPacket(
                                                            true,
                                                            BoolArgumentType.getBool(context, "inRound"),
                                                            new byte[]{1,1,0,0,1}));
                                            return 0;
                                    }))))));
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
