package net.kyrptonaught.lemclienthelper.syncedKeybinds;


import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public record SyncKeybindsPacket(
        HashMap<ResourceLocation, SyncedKeybindsConfig.KeybindConfigItem> keybinds) implements CustomPacketPayload {
    public static final Type<SyncKeybindsPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath(SyncedKeybindsMod.MOD_ID, "keybind_pressed_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncKeybindsPacket> codec = StreamCodec.ofMember(SyncKeybindsPacket::write, SyncKeybindsPacket::read);

    public static SyncKeybindsPacket read(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();
        HashMap<ResourceLocation, SyncedKeybindsConfig.KeybindConfigItem> keybinds = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            keybinds.put(buf.readResourceLocation(), new SyncedKeybindsConfig.KeybindConfigItem(buf.readUtf(), buf.readUtf()));
        }

        return new SyncKeybindsPacket(keybinds);
    }

    public void write(RegistryFriendlyByteBuf buf) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}