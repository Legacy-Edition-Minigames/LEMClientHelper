package net.kyrptonaught.lemclienthelper.SpectateSqueaker;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SqueakPacket(boolean enabled) implements CustomPacketPayload {
    public static final Type<SqueakPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath(SpectateSqueakerMod.MOD_ID, "squeak_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SqueakPacket> codec = ByteBufCodecs.BOOL.map(SqueakPacket::new, SqueakPacket::enabled).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
