package net.kyrptonaught.lemclienthelper.ServerSwitcher;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.util.Identifier;

public class ServerSwitcherMod {
    public static String MOD_ID = "switcher";
    private static final Identifier SWITCH_SERVER = new Identifier("switcher", "switch_server");

    public static void onInitialize() {
        ClientPlayNetworking.registerGlobalReceiver(SWITCH_SERVER, (client, handler, buf, responseSender) -> {
            ServerSwitcher.SwitchServer(buf.readString());
        });
        //register /join command may be useful to players, runs on client
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(ClientCommandManager.literal("join")
                    .then(ClientCommandManager.argument("server", StringArgumentType.string())
                            .executes(context -> {
                                String message = StringArgumentType.getString(context, "server");
                                ServerSwitcher.SwitchServer(message);
                                return 1;
                            }))
            );
        });
    }
}
