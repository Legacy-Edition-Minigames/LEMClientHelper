package net.kyrptonaught.lemclienthelper.SpectateSqueaker;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SpectateSqueakerNetworking {
    private static final Identifier SQUEAK_PACKET = new Identifier(SpectateSqueakerMod.MOD_ID, "squeak_packet");

    @Environment(EnvType.CLIENT)
    public static void sendTakeEverythingPacket() {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            ClientPlayNetworking.send(SQUEAK_PACKET, buf);
    }
}
