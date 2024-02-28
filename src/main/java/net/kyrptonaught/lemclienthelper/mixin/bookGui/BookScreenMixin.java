package net.kyrptonaught.lemclienthelper.mixin.bookGui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.text.Style;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookScreen.class)
public class BookScreenMixin {

    @Unique
    private static Double mouseX, mouseY;

    @Inject(method = "handleTextClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen;closeScreen()V"))
    public void saveMouse(Style style, CallbackInfoReturnable<Boolean> cir) {
        mouseX = MinecraftClient.getInstance().mouse.getX();
        mouseY = MinecraftClient.getInstance().mouse.getY();
    }

    @Inject(method = "init", at = @At(value = "HEAD"))
    public void loadMouse(CallbackInfo ci) {
        if (mouseX != null && mouseY != null) {
            GLFW.glfwSetCursorPos(MinecraftClient.getInstance().getWindow().getHandle(), mouseX, mouseY);
            mouseX = null;
            mouseY = null;
        }
    }
}
