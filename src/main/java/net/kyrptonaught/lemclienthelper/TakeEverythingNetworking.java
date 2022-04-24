package net.kyrptonaught.lemclienthelper;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class TakeEverythingNetworking {
    private static final Identifier TAKE_EVERYTHING_PACKET = new Identifier("takeeverything", "take_everything_packet");

    @Environment(EnvType.CLIENT)
    public static void sendSortPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ClientPlayNetworking.send(TAKE_EVERYTHING_PACKET, new PacketByteBuf(buf));
    }
}
