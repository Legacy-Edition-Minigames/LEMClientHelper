package net.kyrptonaught.lemclienthelper.ClientData;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ClientDataNetworking {
    public static final Identifier HAS_MODS_PACKET = new Identifier("scoreboardplayerinfo", "has_mods_packet");


    @Environment(EnvType.CLIENT)
    public static void sendHasLEMPacket() {
        ClientLoginNetworking.registerGlobalReceiver(HAS_MODS_PACKET, (client, handler, buf, listenerAdder) -> {
            FabricLoader loader = FabricLoader.getInstance();

            PacketByteBuf respondeBuf = new PacketByteBuf(Unpooled.buffer());
            respondeBuf.writeBoolean(true); //LEMClientHelper, Always true
            respondeBuf.writeBoolean(ClientDataMod.isOptifineLoaded(loader));
            respondeBuf.writeBoolean(ClientDataMod.isControllerModLoaded(loader));
            respondeBuf.writeInt(ServerConfigsMod.getConfig().guiScale);
            respondeBuf.writeInt(ServerConfigsMod.getConfig().panScale);

            return CompletableFuture.completedFuture(respondeBuf);
        });
    }
}
