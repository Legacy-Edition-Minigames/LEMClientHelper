package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * GlideTimerTogglePacket starts/stops the timer.
 *
 * @param enabled boolean, is the timer running
 */
public record GlideTimerTogglePacket(boolean enabled) implements CustomPayload {
    public static final Id<GlideTimerTogglePacket> PACKET_ID = new Id<>(Identifier.of("glidehud", "glide_timer_toggle"));
    public static final PacketCodec<RegistryByteBuf, GlideTimerTogglePacket> codec = PacketCodecs.BOOL.xmap(GlideTimerTogglePacket::new, GlideTimerTogglePacket::enabled).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

