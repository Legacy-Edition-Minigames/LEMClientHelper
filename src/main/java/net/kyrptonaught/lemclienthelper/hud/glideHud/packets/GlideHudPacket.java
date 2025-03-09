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
 */
public record GlideHudPacket(boolean enabled) implements CustomPayload {
    public static final Id<GlideHudPacket> PACKET_ID = new Id<>(Identifier.of("glidehud", "glide_hud_render_enable"));
    public static final PacketCodec<RegistryByteBuf, GlideHudPacket> codec = PacketCodec.tuple(
            PacketCodecs.BOOL, GlideHudPacket::enabled,
            GlideHudPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

