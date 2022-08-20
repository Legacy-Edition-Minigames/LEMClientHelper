package net.kyrptonaught.lemclienthelper.ClientData;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
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
        sender.sendPacket(HAS_MODS_PACKET, buf);
    }
}
