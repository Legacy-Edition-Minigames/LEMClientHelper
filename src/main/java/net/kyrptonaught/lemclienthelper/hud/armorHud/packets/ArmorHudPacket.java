package net.kyrptonaught.lemclienthelper.hud.armorHud.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ArmorHudPacket(boolean enabled) implements CustomPacketPayload {
    public static final Type<ArmorHudPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("armorhud", "armor_hud_render_enable"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorHudPacket> codec = ByteBufCodecs.BOOL.map(ArmorHudPacket::new, ArmorHudPacket::enabled).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
