package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * GlideTimerPacket, sets the value of the timer
 *
 * @param ticks int, elapsed time in ticks.
 */
public record GlideTimerPacket(int ticks) implements CustomPacketPayload {
    public static final Type<GlideTimerPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("glidehud", "glide_timer_set"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GlideTimerPacket> codec = ByteBufCodecs.INT.map(GlideTimerPacket::new, GlideTimerPacket::ticks).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
