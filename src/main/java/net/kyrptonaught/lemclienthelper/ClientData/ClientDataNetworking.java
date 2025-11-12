package net.kyrptonaught.lemclienthelper.ClientData;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class ClientDataNetworking {
    public static final ResourceLocation HAS_MODS_PACKET = ResourceLocation.fromNamespaceAndPath("scoreboardplayerinfo", "has_mods_packet");


    @Environment(EnvType.CLIENT)
    public static void sendHasLEMPacket() {
        ClientLoginNetworking.registerGlobalReceiver(HAS_MODS_PACKET, (client, handler, buf, listenerAdder) -> {
            FabricLoader loader = FabricLoader.getInstance();

            FriendlyByteBuf respondeBuf = new FriendlyByteBuf(Unpooled.buffer());
            respondeBuf.writeBoolean(true); //LEMClientHelper, Always true
            // TODO: Start sending LCH version, this can help when diagnosing issues caused by differences between
            //       server Heirloom ver and client LCH ver.
            // respondeBuf.writeString(LEMClientHelperMod.MOD_VERSION);
            respondeBuf.writeBoolean(ClientDataMod.isOptifineLoaded(loader));
            respondeBuf.writeBoolean(ClientDataMod.isControllerModLoaded(loader));
            // TODO: Send L4J info, 
            //       (Note, probably should also send version for this, as L4J tends to change alot update to update.)
            // respondeBuf.writeBoolean(ClientDataMod.isL4JLoaded(loader));
            respondeBuf.writeInt(ServerConfigsMod.getConfig().guiScale);
            respondeBuf.writeInt(ServerConfigsMod.getConfig().panScale);

            return CompletableFuture.completedFuture(respondeBuf);
        });
    }
}
