package net.kyrptonaught.lemclienthelper.ClientData;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ClientDataNetworking {
    public static final Identifier HAS_MODS_PACKET = new Identifier("scoreboardplayerinfo", "has_mods_packet");

    @Environment(EnvType.CLIENT)
    public static void sendHasLEMPacket(PacketSender sender, boolean hasOptifine, boolean hasController) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(true); //LEMClientHelper, Always true
        buf.writeBoolean(hasOptifine);
        buf.writeBoolean(hasController);
        buf.writeInt(ServerConfigsMod.getConfig().guiScale);
        buf.writeInt(ServerConfigsMod.getConfig().panScale);
        sender.sendPacket(HAS_MODS_PACKET, buf);
    }
}
