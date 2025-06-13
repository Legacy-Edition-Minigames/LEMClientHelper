package net.kyrptonaught.lemclienthelper.customWorldBorder;


import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CustomWorldBorderPacket(double xCenter, double zCenter, double xSize,
                                      double zSize) implements CustomPacketPayload {
    public static final Type<CustomWorldBorderPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("customworldborder", "customborder"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CustomWorldBorderPacket> codec = StreamCodec.ofMember(CustomWorldBorderPacket::write, CustomWorldBorderPacket::read);

    public static CustomWorldBorderPacket read(RegistryFriendlyByteBuf buf) {
        return new CustomWorldBorderPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeDouble(xCenter);
        buf.writeDouble(zCenter);
        buf.writeDouble(xSize);
        buf.writeDouble(zSize);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}