package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncedKeybindsNetworking {
    public static final Identifier SYNC_KEYBINDS_PACKET = new Identifier(SyncedKeybinds.MOD_ID, "sync_keybinds_packet");
    public static final Identifier KEYBIND_PRESSED_PACKET = new Identifier(SyncedKeybinds.MOD_ID, "keybind_pressed_packet");

    @Environment(EnvType.CLIENT)
    public static void sendKeyPacket(String keyID) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(keyID);
        ClientPlayNetworking.send(KEYBIND_PRESSED_PACKET, buf);
    }

    @Environment(EnvType.CLIENT)
    public static boolean canSendPacket() {
        return ClientPlayNetworking.canSend(KEYBIND_PRESSED_PACKET);
    }

    public static void registerReceivePacket() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_KEYBINDS_PACKET, (client, handler, packetByteBuf, responseSender) -> {
            int count = packetByteBuf.readInt();
            DataHolder[] dataHolders = new DataHolder[count];

            for (int i = 0; i < count; i++) {
                String id = packetByteBuf.readString();
                String keybinding = packetByteBuf.readString();
                String controllerBind = packetByteBuf.readString();
                dataHolders[i] = new DataHolder(id, keybinding, controllerBind);
            }
            client.execute(() -> {
                for (DataHolder dataHolder : dataHolders)
                    SyncedKeybinds.registerNewKeybind(dataHolder.id, dataHolder.keybinding, dataHolder.controllerBind);
                LEMClientHelperMod.configManager.save(SyncedKeybinds.MOD_ID);
            });
        });
    }

    public record DataHolder(String id, String keybinding, String controllerBind) {
    }
}
