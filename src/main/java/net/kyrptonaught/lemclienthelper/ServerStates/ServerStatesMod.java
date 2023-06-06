package net.kyrptonaught.lemclienthelper.ServerStates;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ServerStatesMod {
    public static States states = new States();

    public static void onInitialize() {
        ServerStatesNetworking.registerPackets();
    }
}
