package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record KeybindPressPacket(ResourceLocation keybind) implements CustomPacketPayload {
    public static final Type<KeybindPressPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath(SyncedKeybindsMod.MOD_ID, "sync_keybinds_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, KeybindPressPacket> codec = ResourceLocation.STREAM_CODEC.map(KeybindPressPacket::new, KeybindPressPacket::keybind).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
