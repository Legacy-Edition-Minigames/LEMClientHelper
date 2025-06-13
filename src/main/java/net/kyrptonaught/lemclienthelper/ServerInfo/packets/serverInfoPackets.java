package net.kyrptonaught.lemclienthelper.ServerInfo.packets;

import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class serverInfoPackets {
    /**
     * <pre>serverInfoPacket, sends all information to client
     *
     * See Example Packet,
     * Casual Battle game, in the lobby, w/ 4 players, 2 are not ready.
     *
     * <code>
     * new serverInfoPacket(
     *      ServerInfoData.MINIGAME_TYPES.BATTLE,
     *      ServerInfoData.GAME_MODES.CASUAL,
     *      ServerInfoData.MINIGAME_PHASES.NONE,
     *      new byte[]{0,0,1,1}
     * );</code>
     * </pre>
     *
     * @param minigame {@link ServerInfoData.MINIGAME_TYPES}. Minigame the client is in.
     *
     * @param gamemode {@link ServerInfoData.GAME_MODES}. Gamemode the game is in.
     *
     * @param phase {@link ServerInfoData.MINIGAME_PHASES}, Phase the game is in.
     *
     * @param playerStatus byte[], -1 — Spectating; 0 — Dead/Not Ready; 1 — Alive/Ready;
     *
     * @see gamemodePacket gamemodePacket
     * @see playerStatusPacket playerStatusPacket
     * @see phasePacket phasePacket
     */
    public record serverInfoPacket(ServerInfoData.MINIGAME_TYPES minigame, ServerInfoData.GAME_MODES gamemode, ServerInfoData.MINIGAME_PHASES phase, byte[] playerStatus) implements CustomPacketPayload {
        public static final Type<serverInfoPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("serverinfo", "serverinfo_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, serverInfoPacket> codec = StreamCodec.composite(
                ByteBufCodecs.idMapper(i -> ServerInfoData.MINIGAME_TYPES.values()[i], ServerInfoData.MINIGAME_TYPES::ordinal), serverInfoPacket::minigame,
                ByteBufCodecs.idMapper(i -> ServerInfoData.GAME_MODES.values()[i], ServerInfoData.GAME_MODES::ordinal), serverInfoPacket::gamemode,
                ByteBufCodecs.idMapper(i -> ServerInfoData.MINIGAME_PHASES.values()[i], ServerInfoData.MINIGAME_PHASES::ordinal), serverInfoPacket::phase,
                ByteBufCodecs.BYTE_ARRAY, serverInfoPacket::playerStatus,
                serverInfoPacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
    public record serverInfoIndicesPacket(int minigame, int gamemode, int phase, byte[] playerStatus) implements CustomPacketPayload {
        public static final Type<serverInfoIndicesPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("serverinfo", "serverinfo_indices_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, serverInfoIndicesPacket> codec = StreamCodec.composite(
                ByteBufCodecs.INT, serverInfoIndicesPacket::minigame,
                ByteBufCodecs.INT, serverInfoIndicesPacket::gamemode,
                ByteBufCodecs.INT, serverInfoIndicesPacket::phase,
                ByteBufCodecs.BYTE_ARRAY, serverInfoIndicesPacket::playerStatus,
                serverInfoIndicesPacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    /**
     * minigamePacket, sends minigame to client
     *
     * @param minigame {@link ServerInfoData.MINIGAME_TYPES}. Minigame the client is in.
     *
     * @deprecated Minigame should not be changed after {@link serverInfoPacket serverInfoPacket} is sent.
     * @see serverInfoPacket serverInfoPacket
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public record minigamePacket(ServerInfoData.MINIGAME_TYPES minigame) implements CustomPacketPayload {
        public static final Type<minigamePacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("serverinfo", "minigame_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, minigamePacket> codec = StreamCodec.composite(
                ByteBufCodecs.idMapper(i -> ServerInfoData.MINIGAME_TYPES.values()[i], ServerInfoData.MINIGAME_TYPES::ordinal), minigamePacket::minigame,
                minigamePacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    /**
     * <pre>gamemodePacket, sends gamemode to client
     * Intended for use when not in round.</pre>
     *
     * @param gamemode {@link ServerInfoData.GAME_MODES}. Gamemode the client is in.
     *
     */
    public record gamemodePacket(ServerInfoData.GAME_MODES gamemode) implements CustomPacketPayload {
        public static final Type<gamemodePacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("serverinfo", "gamemode_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, gamemodePacket> codec = StreamCodec.composite(
                ByteBufCodecs.idMapper(i -> ServerInfoData.GAME_MODES.values()[i], ServerInfoData.GAME_MODES::ordinal), gamemodePacket::gamemode,
                gamemodePacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
    public record gamemodeIndexPacket(int gamemode) implements CustomPacketPayload {
        public static final Type<gamemodeIndexPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("serverinfo", "gamemode_index_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, gamemodeIndexPacket> codec = StreamCodec.composite(
                ByteBufCodecs.INT, gamemodeIndexPacket::gamemode,
                gamemodeIndexPacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    /**
     * phasePacket, sends phase to client
     *
     * @param phase {@link ServerInfoData.MINIGAME_PHASES}. Phase the game is in.
     *
     */
    public record phasePacket(ServerInfoData.MINIGAME_PHASES phase) implements CustomPacketPayload {
        public static final Type<phasePacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("serverinfo", "phase_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, phasePacket> codec = StreamCodec.composite(
                ByteBufCodecs.idMapper(i -> ServerInfoData.MINIGAME_PHASES.values()[i], ServerInfoData.MINIGAME_PHASES::ordinal), phasePacket::phase,
                phasePacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
    public record phaseIndexPacket(int phase) implements CustomPacketPayload {
        public static final Type<phaseIndexPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("serverinfo", "phase_index_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, phaseIndexPacket> codec = StreamCodec.composite(
                ByteBufCodecs.INT, phaseIndexPacket::phase,
                phaseIndexPacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    /**
     * <pre>playerStatusPacket, sends player statuses to client.
     *
     * <code>new byte[]{0,1,0,-1}</code> 4 players, players 1 & 3 are either not ready or dead, player 4 is spectating.
     * </pre>
     *
     * @param playerStatus byte[], -1 — Spectating; 0 — Dead/Not Ready; 1 — Alive/Ready;
     */
    public record playerStatusPacket(byte[] playerStatus) implements CustomPacketPayload {
        public static final Type<playerStatusPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("serverinfo", "playerstatus_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, playerStatusPacket> codec = StreamCodec.composite(
                ByteBufCodecs.BYTE_ARRAY, playerStatusPacket::playerStatus,
                playerStatusPacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
