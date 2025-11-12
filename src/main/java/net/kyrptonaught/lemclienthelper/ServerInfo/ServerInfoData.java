package net.kyrptonaught.lemclienthelper.ServerInfo;

public class ServerInfoData {
    private static GAME_MODES GAME_MODE = GAME_MODES.CASUAL;
    private static MINIGAME_TYPES MINIGAME = MINIGAME_TYPES.BATTLE;
    private static MINIGAME_PHASES PHASE = MINIGAME_PHASES.NONE;

    private static byte[] PLAYER_STATUS =  new byte[]{1,0,1,1,1,0,-1,-1};

    /**
     * Minigame types
     * <ul>
     *     <li> BATTLE
     *     <li> TUMBLE
     *     <li> GLIDE
     * </ul>
     */
    public enum MINIGAME_TYPES {
        BATTLE,
        TUMBLE,
        GLIDE,
    }

    /**
     * Gamemodes types
     *
     * <ul>
     *     <li> CUSTOM
     * </ul>
     * Battle specific:
     * <ul>
     *     <li> CASUAL
     *     <li> COMPETITIVE
     * </ul>
     * Tumble specific:
     * <ul>
     *     <li> SNOWBALLS
     *     <li> SHOVELS
     *     <li> MIXED
     * </ul>
     * Glide specific:
     * <ul>
     *     <li> TIME_ATTACK
     *     <li> SCORE_ATTACK
     * </ul>
     */
    public enum GAME_MODES {
        CUSTOM,

        CASUAL,
        COMPETITIVE,

        SNOWBALLS,
        SHOVELS,
        MIXED,

        TIME_ATTACK,
        SCORE_ATTACK
    }

    /**
     * Minigame phase
     * <ul>
     *     <li> NONE
     *     <li> LOADING
     *     <li> COUNTDOWN
     *     <li> RUNNING
     *     <li> SHOWDOWN
     *     <li> END
     * </ul>
     */
    public enum MINIGAME_PHASES {
        NONE,
        LOADING,
        COUNTDOWN,
        RUNNING,
        SHOWDOWN,
        END
    }

    public static void setMinigame(MINIGAME_TYPES minigame) {
        MINIGAME = minigame;
    }

    public static MINIGAME_TYPES getMinigame() {
        return MINIGAME;
    }

    public static void setGamemode(GAME_MODES gameMode) {
        GAME_MODE = gameMode;
    }

    public static GAME_MODES getGamemode() {
        return GAME_MODE;
    }

    public static void setPhase(MINIGAME_PHASES phase) {
        PHASE = phase;
    }

    public static MINIGAME_PHASES getPhase() {
        return PHASE;
    }

    public static void setPlayerStatus(byte[] playerStatus) {
        PLAYER_STATUS = playerStatus;
    }

    public static byte[] getPlayerStatus() {
        return PLAYER_STATUS;
    }
}
