package net.kyrptonaught.lemclienthelper.hud.genericHud;

import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.lemclienthelper.hud.genericHud.packets.BannerPacket;
import net.kyrptonaught.lemclienthelper.hud.genericHud.packets.PlayerBarPacket;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.kyrptonaught.lemclienthelper.ServerInfo.packets.serverInfoPackets;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.LevelStorageSource;
import wily.factoryapi.FactoryAPI;
import wily.factoryapi.FactoryAPIClient;
import wily.factoryapi.base.ArbitrarySupplier;
import wily.factoryapi.base.client.*;
import wily.factoryapi.util.ColorUtil;
import wily.factoryapi.util.FactoryGuiElement;
import wily.factoryapi.util.FactoryScreenUtil;
import wily.legacy.client.*;
import net.minecraft.commands.arguments.ComponentArgument;
import wily.legacy.client.controller.ControllerBinding;
import wily.legacy.client.controller.LegacyKeyMapping;
import wily.legacy.client.screen.ControlTooltip;
import wily.legacy.client.screen.compat.IrisCompat;
import wily.legacy.client.screen.compat.ModMenuCompat;
import wily.legacy.client.screen.compat.SodiumCompat;
import wily.legacy.init.LegacyRegistries;
import wily.legacy.network.TopMessage;

import java.util.Optional;

public class GenericHudMod {
    public static boolean SHOULD_RENDER_PLAYERBAR = false;

    public static boolean BANNER_RECEIVED = false; // Will set to false after banner is finished rendering.
    public static Component BANNER_TEXT = null;

    public static void onInitialize() {
        boolean l4jLoaded = FabricLoader.getInstance().isModLoaded("legacy");

        if (l4jLoaded) {
            FactoryAPIClient.setup((m) -> {
                LegacyOptions.CLIENT_STORAGE.load();
                UIAccessor accessor = FactoryScreenUtil.getGuiAccessor();
                accessor.getStaticDefinitions().add(UIDefinition.createBeforeInit((a) -> {
                    if (LegacyMixinOptions.legacyGui.get()) {
                        a.getElements().put(FactoryGuiElement.EXPERIENCE_BAR.name() + ".isVisible", () -> {
                            return HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.EXPERIENCE, true) && HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.STATS,true) && HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.ALL, true);
                        });
                        a.getElements().put(FactoryGuiElement.PLAYER_HEALTH.name() + ".isVisible", () -> {
                            return HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.HEARTS, true) && HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.STATS,true) && HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.ALL, true);
                        });
                        a.getElements().put(FactoryGuiElement.HOTBAR.name() + ".isVisible", () -> {
                            return HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.HOTBAR, true) && HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.ALL, true);
                        });
                    }
                }));
            });
        }


        HudRenderCallback.EVENT.register(BannerRenderer::onHudRender);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_PLAYERBAR = false);

        PayloadTypeRegistry.playS2C().register(PlayerBarPacket.PACKET_ID, PlayerBarPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(PlayerBarPacket.PACKET_ID, ((payload, context) -> { 
                SHOULD_RENDER_PLAYERBAR = payload.enabled();
                HideVanillaHUD.visible.put(HideVanillaHUD.HUD_ELEMENT.EXPERIENCE, !payload.enabled());
        }));

        PayloadTypeRegistry.playS2C().register(BannerPacket.PACKET_ID, BannerPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(BannerPacket.PACKET_ID, ((payload, context) -> {
            BANNER_TEXT = payload.text();
            BannerRenderer.setElapsedMax(payload.elapsedMax().orElse(3.0f));
            BannerRenderer.setIcon();
            BANNER_RECEIVED = true;
        }));


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal(LEMClientHelperMod.MOD_ID)
                        .then(Commands.literal("examplePlayerBar")
                                .then(Commands.argument("inRound", BoolArgumentType.bool())
                                    .then(Commands.argument("target", EntityArgument.players())
                                            .executes(context -> {
                                                ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                        new serverInfoPackets.phasePacket((BoolArgumentType.getBool(context,"inRound") ? ServerInfoData.MINIGAME_PHASES.RUNNING : ServerInfoData.MINIGAME_PHASES.NONE)));
                                                ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                        new PlayerBarPacket(true));
                                                return 0;
                                        }))))));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal(LEMClientHelperMod.MOD_ID)
                        .then(Commands.literal("sendTestBanner")
                                .then(Commands.argument("target", EntityArgument.players())
                                        .then(Commands.argument("minigame", IntegerArgumentType.integer(0,2))
                                                .then(Commands.argument("gamemode", IntegerArgumentType.integer(0,7))
                                                        .then(Commands.argument("phase", IntegerArgumentType.integer(0,5))
                                                            .then(Commands.argument("text", ComponentArgument.textComponent(registryAccess))
                                                                    .then(Commands.argument("elapsedMax", FloatArgumentType.floatArg(3f,90f))
                                                                    .executes(context -> {
                                                                        ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                                                new serverInfoPackets.minigamePacket(
                                                                                        ServerInfoData.MINIGAME_TYPES.values()[IntegerArgumentType.getInteger(context,"minigame")]));
                                                                        ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                                                new serverInfoPackets.gamemodePacket(
                                                                                        ServerInfoData.GAME_MODES.values()[IntegerArgumentType.getInteger(context,"gamemode")]));
                                                                        ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                                                new serverInfoPackets.phasePacket(
                                                                                        ServerInfoData.MINIGAME_PHASES.values()[IntegerArgumentType.getInteger(context,"phase")]));
                                                                        ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                                                new BannerPacket(ComponentArgument.getRawComponent(context,"text"), Optional.of(FloatArgumentType.getFloat(context, "elapsedMax"))));
                                                                        return 0;
                                                        })).executes(context -> {
                                                                    ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                                            new serverInfoPackets.minigamePacket(
                                                                                    ServerInfoData.MINIGAME_TYPES.values()[IntegerArgumentType.getInteger(context, "minigame")]));
                                                                    ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                                            new serverInfoPackets.gamemodePacket(
                                                                                    ServerInfoData.GAME_MODES.values()[IntegerArgumentType.getInteger(context, "gamemode")]));
                                                                    ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                                            new serverInfoPackets.phasePacket(
                                                                                    ServerInfoData.MINIGAME_PHASES.values()[IntegerArgumentType.getInteger(context, "phase")]));
                                                                    ServerPlayNetworking.send(EntityArgument.getPlayer(context, "target"),
                                                                            new BannerPacket(ComponentArgument.getRawComponent(context, "text"),Optional.empty()));
                                                                    return 0;
                                                                })))))))));

    }
}
