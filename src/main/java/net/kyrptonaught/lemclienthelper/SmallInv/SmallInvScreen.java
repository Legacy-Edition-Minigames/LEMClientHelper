package net.kyrptonaught.lemclienthelper.SmallInv;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lebclienthelper.LEBClientHelperMod;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class SmallInvScreen extends AbstractInventoryScreen<SmallInvScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(LEBClientHelperMod.MOD_ID, "textures/gui/legacy_inventory.png");
    private float mouseX;
    private float mouseY;

    public SmallInvScreen(PlayerEntity player) {
        super(new SmallInvScreenHandler(player.playerScreenHandler.syncId, player.getInventory()), player.getInventory(), new TranslatableText("container.inventory"));
        this.passEvents = true;
        //this.titleX = 97;
        this.titleY = 78;
        this.backgroundHeight = 112;
    }

    @Override
    protected void init() {
        super.init();
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
        InventoryScreen.drawEntity(x + 95, y + 75, 30, (float) (x + 95) - this.mouseX, (float) (y + 75 - 50) - this.mouseY, this.client.player);

    }
}