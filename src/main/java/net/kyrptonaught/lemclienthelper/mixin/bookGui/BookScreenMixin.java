package net.kyrptonaught.lemclienthelper.mixin.bookGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Style;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookViewScreen.class)
public class BookScreenMixin {

    @Unique
    private static Double mouseX, mouseY;

    @Inject(method = "handleComponentClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/BookViewScreen;closeScreen()V"))
    public void saveMouse(Style style, CallbackInfoReturnable<Boolean> cir) {
        mouseX = Minecraft.getInstance().mouseHandler.xpos();
        mouseY = Minecraft.getInstance().mouseHandler.ypos();
    }

    @Inject(method = "init", at = @At(value = "HEAD"))
    public void loadMouse(CallbackInfo ci) {
        if (mouseX != null && mouseY != null) {
            GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), mouseX, mouseY);
            mouseX = null;
            mouseY = null;
        }
    }
}
