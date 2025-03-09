package net.kyrptonaught.lemclienthelper.ServerInfo;

/**
 * Any and all information that could possibly be used by the client, not tied to rendering.
 */
public class ServerInfoMod {
    public static void onInitialize() {
        ServerInfoNetworking.onInitialize();
    }
}
