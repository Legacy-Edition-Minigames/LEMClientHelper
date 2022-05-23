package net.kyrptonaught.lemclienthelper.SmallInv;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class SmallInvScreen extends AbstractInventoryScreen<PlayerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/legacy_inventory.png");
    private float mouseX;
    private float mouseY;

    public SmallInvScreen(PlayerEntity player) {
        super(player.playerScreenHandler, player.getInventory(), new TranslatableText("container.inventory"));
        this.passEvents = false;
        this.titleY = 86;
        this.backgroundHeight = 124;
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (SmallInvInit.isKeybindPressed(keyCode, false)) {
            this.client.setScreen(new InventoryScreen(client.player));
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (SmallInvInit.isKeybindPressed(button, true)) {
            this.client.setScreen(new InventoryScreen(client.player));
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, (float) this.titleX, (float) this.titleY, 0x404040);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        //InventoryScreen.drawEntity(x + 97, y + 72 + 27, 30, (float) (x + 51) - this.mouseX, (float) (y + 75 - 50) - this.mouseY, this.client.player);
        //InventoryScreen.drawEntity(x + 95, y + 75, 30, (float) (x + 95) - this.mouseX, (float) (y + 75 - 50) - this.mouseY, this.client.player);
        //InventoryScreen.drawEntity(x + 51 + 46, y + 75, 30, (float) (x + 51 + 46) - this.mouseX, (float) (y + 75 - 50) - this.mouseY, this.client.player);
        InventoryScreen.drawEntity(x + 51 + 51, y + 75 - 2, 30, (float) (x + 51 + 51) - this.mouseX, (float) (y + 75 - 50 - 2) - this.mouseY, this.client.player);
    }
}