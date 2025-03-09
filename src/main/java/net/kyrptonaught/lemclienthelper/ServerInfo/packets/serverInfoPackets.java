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
     *      false,
     *      new byte[]{0,0,1,1}
     * );</code>
     * </pre>
     *
     * @param minigame {@link ServerInfoData.MINIGAME_TYPES}. Minigame the client is in.
     *
     * @param gamemode {@link ServerInfoData.GAME_MODES}. Gamemode the client is in.
     *
     * @param inRound boolean, Is the client in a round?
     *
     * @param playerStatus byte[]. In Round: 0 - Dead; 1 - Alive; -1 - Spectating;<br>
     *                             In Lobby: 0 - Not Ready; 1 - Ready
     *
     * @see gamemodePacket gamemodePacket
     * @see playerStatusPacket playerStatusPacket
     * @see inRoundPacket inRoundPacket
     */
    public record serverInfoPacket(ServerInfoData.MINIGAME_TYPES minigame, ServerInfoData.GAME_MODES gamemode, boolean inRound, byte[] playerStatus) implements CustomPayload {
        public static final Id<serverInfoPacket> PACKET_ID = new Id<>(Identifier.of("serverinfo", "serverinfo_set"));
        public static final PacketCodec<RegistryByteBuf, serverInfoPacket> codec = PacketCodec.tuple(
                PacketCodecs.indexed(i -> ServerInfoData.MINIGAME_TYPES.values()[i], ServerInfoData.MINIGAME_TYPES::ordinal), serverInfoPacket::minigame,
                PacketCodecs.indexed(i -> ServerInfoData.GAME_MODES.values()[i], ServerInfoData.GAME_MODES::ordinal), serverInfoPacket::gamemode,
                PacketCodecs.BOOL, serverInfoPacket::inRound,
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
     * <pre>inRoundPacket, sends the current inRound status to client.
     * Intended for use at begining & end of round.</pre>
     *
     * @param inRound boolean, Is the client in a round?
     */
    public record inRoundPacket(boolean inRound) implements CustomPayload {
        public static final Id<inRoundPacket> PACKET_ID = new Id<>(Identifier.of("serverinfo", "inround_set"));
        public static final PacketCodec<RegistryByteBuf, inRoundPacket> codec = PacketCodec.tuple(
                PacketCodecs.BOOL, inRoundPacket::inRound,
                inRoundPacket::new
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
     * @param playerStatus byte[]. In Round: 0 - Dead; 1 - Alive; -1 - Spectating;<br>
     *                             In Lobby: 0 - Not Ready; 1 - Ready
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
