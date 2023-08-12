package net.kyrptonaught.lemclienthelper.ServerSwitcher;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class ServerSwitcher {
    static MinecraftClient client = MinecraftClient.getInstance();
    public  static void SwitchServer(String serverAddress){
        System.out.println("Switching to server: " + serverAddress);
        //NOTE: sends client to the title screen temporarily, then to the server
        client.world.disconnect();
        ConnectScreen.connect(new DownloadingTerrainScreen(), client, ServerAddress.parse(serverAddress), new ServerInfo("lem",serverAddress,false),false);
    }
}

