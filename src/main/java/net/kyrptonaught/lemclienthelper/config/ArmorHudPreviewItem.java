package net.kyrptonaught.lemclienthelper.config;

import net.kyrptonaught.kyrptconfig.config.screen.items.BooleanItem;
import net.kyrptonaught.lemclienthelper.hud.armorHud.ArmorHudRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ArmorHudPreviewItem extends BooleanItem {
    public ArmorHudPreviewItem(Component name, Boolean value, Boolean defaultValue) {
        super(name, value, defaultValue);
    }

    @Override
    public void render2(GuiGraphics context, int x, int y, int mouseX, int mouseY, float delta) {
        super.render2(context, x, y, mouseX, mouseY, delta);
        if (value) {
            ArmorHudRenderer.onHudRenderDummy(context, 20, 1);
        }
    }
}
