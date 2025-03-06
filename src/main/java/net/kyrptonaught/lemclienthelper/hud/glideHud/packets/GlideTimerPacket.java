package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * GlideTimerPacket, sets the value of the timer
 *
 * @param ticks int, elapsed time in ticks.
 */
public record GlideTimerPacket(int ticks) implements CustomPayload {
    public static final Id<GlideTimerPacket> PACKET_ID = new Id<>(Identifier.of("glidehud", "glide_timer_set"));
    public static final PacketCodec<RegistryByteBuf, GlideTimerPacket> codec = PacketCodecs.INTEGER.xmap(GlideTimerPacket::new, GlideTimerPacket::ticks).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
