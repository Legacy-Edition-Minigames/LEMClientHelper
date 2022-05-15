package net.kyrptonaught.lemclienthelper.mixin;

import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Redirect(method = "handleInputEvents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onInventoryOpened()V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;getAdvancementHandler()Lnet/minecraft/client/network/ClientAdvancementManager;")))
    public void openSmallInv(MinecraftClient instance, Screen screen) {
        //todo add check if small inv is enabled
        if (true)
            instance.setScreen(new SmallInvScreen(instance.player));
        else
            instance.setScreen(screen);
    }
}
