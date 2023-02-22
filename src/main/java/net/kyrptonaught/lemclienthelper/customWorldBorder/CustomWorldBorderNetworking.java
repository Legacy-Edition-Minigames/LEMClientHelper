package net.kyrptonaught.lemclienthelper.customWorldBorder;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.lemclienthelper.customWorldBorder.duckInterface.CustomWorldBorder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CustomWorldBorderNetworking {
    public static final Identifier CUSTOM_BORDER_PACKET = new Identifier("customworldborder", "customborder");


    public static void sendCustomWorldBorderPacket(ServerPlayerEntity player, double xCenter, double zCenter, double xSize, double zSize) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(xCenter);
        buf.writeDouble(zCenter);
        buf.writeDouble(xSize);
        buf.writeDouble(zSize);

        ServerPlayNetworking.send(player, CUSTOM_BORDER_PACKET, buf);
    }

    @Environment(EnvType.CLIENT)
    public static void registerReceive() {
        ClientPlayNetworking.registerGlobalReceiver(CUSTOM_BORDER_PACKET, (client, handler, packet, sender) -> {
            double xCenter = packet.readDouble();
            double zCenter = packet.readDouble();
            double xSize = packet.readDouble();
            double zSize = packet.readDouble();
            ((CustomWorldBorder) client.world.getWorldBorder()).setShape(xCenter, zCenter, xSize, zSize);
        });
    }
}
