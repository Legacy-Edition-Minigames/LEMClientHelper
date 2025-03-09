package net.kyrptonaught.lemclienthelper.hud.genericHud;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.kyrptonaught.lemclienthelper.hud.genericHud.packets.BannerPacket;
import net.kyrptonaught.lemclienthelper.hud.genericHud.packets.PlayerBarPacket;
import net.minecraft.text.Text;

/*
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.kyrptonaught.lemclienthelper.ServerInfo.packets.serverInfoPackets;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.CommandManager;
 */

public class GenericHudMod {
    public static boolean SHOULD_RENDER_PLAYERBAR = false;

    public static boolean BANNER_RECEIVED = false; // Will set to false after banner is finished rendering.
    public static Text BANNER_TEXT = null;

    public static void onInitialize() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_PLAYERBAR = false);

        PayloadTypeRegistry.playS2C().register(PlayerBarPacket.PACKET_ID, PlayerBarPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(PlayerBarPacket.PACKET_ID, ((payload, context) -> SHOULD_RENDER_PLAYERBAR = payload.enabled()));

        PayloadTypeRegistry.playS2C().register(BannerPacket.PACKET_ID, BannerPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(BannerPacket.PACKET_ID, ((payload, context) -> {
            BANNER_TEXT = payload.text();
            BANNER_RECEIVED = true;
        }));

        /*
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(CommandManager.literal(LEMClientHelperMod.MOD_ID)
                        .then(CommandManager.literal("examplePlayerBar")
                                .then(CommandManager.argument("inRound", BoolArgumentType.bool())
                                    .then(CommandManager.argument("target", EntityArgumentType.players())
                                            .executes(context -> {
                                                ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"),
                                                        new serverInfoPackets.inRoundPacket(BoolArgumentType.getBool(context,"inRound")));
                                                ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"),
                                                        new PlayerBarPacket(true));
                                                return 0;
                                        }))))));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(CommandManager.literal(LEMClientHelperMod.MOD_ID)
                        .then(CommandManager.literal("sendTestBanner")
                                .then(CommandManager.argument("target", EntityArgumentType.players())
                                        .then(CommandManager.argument("minigame", IntegerArgumentType.integer(0,2))
                                                .then(CommandManager.argument("gamemode", IntegerArgumentType.integer(0,7))
                                                    .then(CommandManager.argument("text", TextArgumentType.text(registryAccess))
                                                            .executes(context -> {
                                                                ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"),
                                                                        new serverInfoPackets.minigamePacket(
                                                                                ServerInfoData.MINIGAME_TYPES.values()[IntegerArgumentType.getInteger(context,"minigame")]));
                                                                ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"),
                                                                        new serverInfoPackets.gamemodePacket(
                                                                                ServerInfoData.GAME_MODES.values()[IntegerArgumentType.getInteger(context,"gamemode")]));
                                                                ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "target"),
                                                                        new BannerPacket(TextArgumentType.getTextArgument(context,"text")));
                                                                return 0;
                                                }))))))));
         */
    }
}
