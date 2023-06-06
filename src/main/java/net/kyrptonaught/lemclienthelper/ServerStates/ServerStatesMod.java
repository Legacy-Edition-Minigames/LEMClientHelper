package net.kyrptonaught.lemclienthelper.ServerStates;

public class ServerStatesMod {
    public static States states = new States();

    public static void onInitialize() {
        ServerStatesNetworking.registerPackets();
    }
}
