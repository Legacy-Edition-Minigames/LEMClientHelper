package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * GlideHudPacket, tells client to enable/disable the glide hud.
 *
 * @param enabled boolean, is the hud enabled.
 * @param score boolean, is it a score attack round.
 */
public record GlideHudPacket(boolean enabled, boolean score) implements CustomPayload {
    public static final Id<GlideHudPacket> PACKET_ID = new Id<>(Identifier.of("glidehud", "glide_hud_render_enable"));
    public static final PacketCodec<RegistryByteBuf, GlideHudPacket> codec = PacketCodec.tuple(
            PacketCodecs.BOOL, GlideHudPacket::enabled,
            PacketCodecs.BOOL, GlideHudPacket::score,
            GlideHudPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

