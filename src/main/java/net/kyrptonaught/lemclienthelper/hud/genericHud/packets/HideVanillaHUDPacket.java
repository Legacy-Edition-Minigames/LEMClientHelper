package net.kyrptonaught.lemclienthelper.hud.genericHud.packets;

import net.kyrptonaught.lemclienthelper.hud.genericHud.HideVanillaHUD;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * HideVanillaHUDPacket sends banner to client
 *
 * @param element HideVanillaHUD.HUD_ELEMENT, {@link HideVanillaHUD.HUD_ELEMENT}.
 * @param visible boolean, Is the element visible, false for hidden, true for shown.
 */
public record HideVanillaHUDPacket(HideVanillaHUD.HUD_ELEMENT element, boolean visible) implements CustomPacketPayload {
    public static final Type<HideVanillaHUDPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("hud", "hidevanilla"));
    public static final StreamCodec<RegistryFriendlyByteBuf, HideVanillaHUDPacket> codec = StreamCodec.composite(
            ByteBufCodecs.idMapper(i -> HideVanillaHUD.HUD_ELEMENT.values()[i], HideVanillaHUD.HUD_ELEMENT::ordinal), HideVanillaHUDPacket::element,
            ByteBufCodecs.BOOL, HideVanillaHUDPacket::visible,
            HideVanillaHUDPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}
