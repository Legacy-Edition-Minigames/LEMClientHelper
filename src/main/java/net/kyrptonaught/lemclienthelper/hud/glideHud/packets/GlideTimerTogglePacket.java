package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * GlideTimerTogglePacket starts/stops the timer.
 *
 * @param enabled boolean, is the timer running
 */
public record GlideTimerTogglePacket(boolean enabled) implements CustomPacketPayload {
    public static final Type<GlideTimerTogglePacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("glidehud", "glide_timer_toggle"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GlideTimerTogglePacket> codec = ByteBufCodecs.BOOL.map(GlideTimerTogglePacket::new, GlideTimerTogglePacket::enabled).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}

