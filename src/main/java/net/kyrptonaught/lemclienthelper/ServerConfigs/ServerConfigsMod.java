package net.kyrptonaught.lemclienthelper.ServerConfigs;

import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;

public class ServerConfigsMod {
    public static String MOD_ID = "serverconfigs";

    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new ServerConfigsConfig());
    }

    public static ServerConfigsConfig getConfig() {
        return (ServerConfigsConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
