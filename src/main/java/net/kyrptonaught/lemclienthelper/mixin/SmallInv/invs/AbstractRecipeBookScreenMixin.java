package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractRecipeBookScreen.class)
public class AbstractRecipeBookScreenMixin {

    @ModifyArg(method = "initButton", at = @At(target = "Lnet/minecraft/client/gui/screens/inventory/AbstractRecipeBookScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", value = "INVOKE"))
    public GuiEventListener fkRecipeBook(GuiEventListener element) {
        if (element instanceof ImageButton button)
            RecipeBookWidget.bookWidget = button;
        return element;
    }
}
