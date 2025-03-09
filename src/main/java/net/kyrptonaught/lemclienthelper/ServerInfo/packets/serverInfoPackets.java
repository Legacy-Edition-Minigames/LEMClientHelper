package net.kyrptonaught.lemclienthelper.ServerInfo.packets;

import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

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
    public record serverInfoPacket(ServerInfoData.MINIGAME_TYPES minigame, ServerInfoData.GAME_MODES gamemode, ServerInfoData.MINIGAME_PHASES phase, byte[] playerStatus) implements CustomPayload {
        public static final Id<serverInfoPacket> PACKET_ID = new Id<>(Identifier.of("serverinfo", "serverinfo_set"));
        public static final PacketCodec<RegistryByteBuf, serverInfoPacket> codec = PacketCodec.tuple(
                PacketCodecs.indexed(i -> ServerInfoData.MINIGAME_TYPES.values()[i], ServerInfoData.MINIGAME_TYPES::ordinal), serverInfoPacket::minigame,
                PacketCodecs.indexed(i -> ServerInfoData.GAME_MODES.values()[i], ServerInfoData.GAME_MODES::ordinal), serverInfoPacket::gamemode,
                PacketCodecs.indexed(i -> ServerInfoData.MINIGAME_PHASES.values()[i], ServerInfoData.MINIGAME_PHASES::ordinal), serverInfoPacket::phase,
                PacketCodecs.BYTE_ARRAY, serverInfoPacket::playerStatus,
                serverInfoPacket::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
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
    public record minigamePacket(ServerInfoData.MINIGAME_TYPES minigame) implements CustomPayload {
        public static final Id<minigamePacket> PACKET_ID = new Id<>(Identifier.of("serverinfo", "minigame_set"));
        public static final PacketCodec<RegistryByteBuf, minigamePacket> codec = PacketCodec.tuple(
                PacketCodecs.indexed(i -> ServerInfoData.MINIGAME_TYPES.values()[i], ServerInfoData.MINIGAME_TYPES::ordinal), minigamePacket::minigame,
                minigamePacket::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
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
    public record gamemodePacket(ServerInfoData.GAME_MODES gamemode) implements CustomPayload {
        public static final Id<gamemodePacket> PACKET_ID = new Id<>(Identifier.of("serverinfo", "gamemode_set"));
        public static final PacketCodec<RegistryByteBuf, gamemodePacket> codec = PacketCodec.tuple(
                PacketCodecs.indexed(i -> ServerInfoData.GAME_MODES.values()[i], ServerInfoData.GAME_MODES::ordinal), gamemodePacket::gamemode,
                gamemodePacket::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }

    /**
     * phasePacket, sends phase to client
     *
     * @param phase {@link ServerInfoData.MINIGAME_PHASES}. Phase the game is in.
     *
     */
    public record phasePacket(ServerInfoData.MINIGAME_PHASES phase) implements CustomPayload {
        public static final Id<phasePacket> PACKET_ID = new Id<>(Identifier.of("serverinfo", "phase_set"));
        public static final PacketCodec<RegistryByteBuf, phasePacket> codec = PacketCodec.tuple(
                PacketCodecs.indexed(i -> ServerInfoData.MINIGAME_PHASES.values()[i], ServerInfoData.MINIGAME_PHASES::ordinal), phasePacket::phase,
                phasePacket::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
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
    public record playerStatusPacket(byte[] playerStatus) implements CustomPayload {
        public static final Id<playerStatusPacket> PACKET_ID = new Id<>(Identifier.of("serverinfo", "playerstatus_set"));
        public static final PacketCodec<RegistryByteBuf, playerStatusPacket> codec = PacketCodec.tuple(
                PacketCodecs.BYTE_ARRAY, playerStatusPacket::playerStatus,
                playerStatusPacket::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
