package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * GlideHudPacket, tells client to enable/disable the glide hud.
 *
 * @param enabled boolean, is the hud enabled.
 */
public record GlideHudPacket(boolean enabled) implements CustomPacketPayload {
    public static final Type<GlideHudPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("glidehud", "glide_hud_render_enable"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GlideHudPacket> codec = StreamCodec.composite(
            ByteBufCodecs.BOOL, GlideHudPacket::enabled,
            GlideHudPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}

