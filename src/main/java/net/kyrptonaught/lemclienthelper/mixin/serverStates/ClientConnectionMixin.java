package net.kyrptonaught.lemclienthelper.mixin.serverStates;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.kyrptonaught.lemclienthelper.ServerStates.ServerStatesMod;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow private Channel channel;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callback) {
        if (this.channel.isOpen()&&packet != null) {
            if(packet instanceof LoginSuccessS2CPacket){
                //reset states that need to be reset on login/disconnect
                ServerStatesMod.states.isArmorHudEnabled=false;
            }
        }
    }

}
