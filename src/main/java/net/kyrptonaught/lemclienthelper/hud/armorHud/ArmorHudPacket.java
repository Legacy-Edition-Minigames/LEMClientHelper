package net.kyrptonaught.lemclienthelper.hud.armorHud;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ArmorHudPacket(boolean enabled) implements CustomPayload {
    public static final Id<ArmorHudPacket> PACKET_ID = new Id<>(Identifier.of("armorhud", "armor_hud_render_enable"));
    public static final PacketCodec<RegistryByteBuf, ArmorHudPacket> codec = PacketCodecs.BOOL.xmap(ArmorHudPacket::new, ArmorHudPacket::enabled).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
