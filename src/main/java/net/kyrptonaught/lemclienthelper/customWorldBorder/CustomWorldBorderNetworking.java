package net.kyrptonaught.lemclienthelper.customWorldBorder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.lemclienthelper.customWorldBorder.duckInterface.CustomWorldBorder;
import net.minecraft.server.level.ServerPlayer;

public class CustomWorldBorderNetworking {

    public static void sendCustomWorldBorderPacket(ServerPlayer player, double xCenter, double zCenter, double xSize, double zSize) {
        ServerPlayNetworking.send(player, new CustomWorldBorderPacket(xCenter, zCenter, xSize, zSize));
    }

    @Environment(EnvType.CLIENT)
    public static void registerReceive() {
        ClientPlayNetworking.registerGlobalReceiver(CustomWorldBorderPacket.PACKET_ID, ((payload, context) -> {
            context.client().execute(() -> {
                ((CustomWorldBorder) context.client().level.getWorldBorder()).setShape(payload.xCenter(), payload.zCenter(), payload.xSize(), payload.zSize());
                context.client().level.getWorldBorder().setWarningBlocks(0);
            });
        }));
    }
}
