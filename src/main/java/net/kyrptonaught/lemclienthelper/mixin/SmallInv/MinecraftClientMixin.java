package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Redirect(method = "handleInputEvents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onInventoryOpened()V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;getAdvancementHandler()Lnet/minecraft/client/network/ClientAdvancementManager;")))
    public void openSmallInv(MinecraftClient instance, Screen screen) {
        //todo add check if small inv is enabled
        if (hasSmallInvItem()) {
            ((SmallInvPlayerInv) player.playerScreenHandler).setIsSmall(true);
            instance.setScreen(new SmallInvScreen(instance.player));
        } else {
            ((SmallInvPlayerInv) player.playerScreenHandler).setIsSmall(false);
            instance.setScreen(screen);
        }
    }

    private boolean hasSmallInvItem() {
        for (ItemStack itemStack : player.getInventory().main) {
            if (itemStack.hasNbt() && itemStack.getNbt().contains("SmallInv") && itemStack.getNbt().getInt("SmallInv") == 1)
                return true;
        }
        return false;
    }
}
