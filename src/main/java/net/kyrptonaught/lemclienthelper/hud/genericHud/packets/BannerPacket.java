package net.kyrptonaught.lemclienthelper.hud.genericHud.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

/**
 * BannerPacket sends banner to client
 *
 * @param icon BannerPacket.icons, BATTLE, TUMBLE, GLIDE, GLIDE_TIME, GLIDE_SCORE.
 * @param text Text, banner contents, such as "PLAYER won","Showdown!","0:00,000", etc.
 */
public record BannerPacket(icons icon, Text text) implements CustomPayload{
    public enum icons {
        BATTLE, TUMBLE, GLIDE, GLIDE_TIME, GLIDE_SCORE
    }
    public static final Id<BannerPacket> PACKET_ID = new Id<>(Identifier.of("hud", "banner"));
    public static final PacketCodec<RegistryByteBuf, BannerPacket> codec = PacketCodec.tuple(
            PacketCodecs.indexed(i -> icons.values()[i], icons::ordinal), BannerPacket::icon,
            TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, BannerPacket::text,
            BannerPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
