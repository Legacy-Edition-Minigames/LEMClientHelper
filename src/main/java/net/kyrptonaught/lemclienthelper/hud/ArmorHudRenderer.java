package net.kyrptonaught.lemclienthelper.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.util.List;

public class ArmorHudRenderer {
    private static final Identifier[] EMPTY_SLOTS = new Identifier[]{
            new Identifier("minecraft", "textures/item/empty_armor_slot_boots.png"),
            new Identifier("minecraft", "textures/item/empty_armor_slot_leggings.png"),
            new Identifier("minecraft", "textures/item/empty_armor_slot_chestplate.png"),
            new Identifier("minecraft", "textures/item/empty_armor_slot_helmet.png")
    };

    public static void onHudRender(DrawContext context, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        //HudMod.SHOULD_RENDER_ARMOR = true;
        if (client.player != null && HudMod.shouldDisplay()) {
            int height = client.getWindow().getScaledHeight();

            context.getMatrices().push();
            context.getMatrices().translate(HudMod.getConfig().xOffset, height / 2f, 0);
            context.getMatrices().scale(HudMod.getConfig().armorHudScale, HudMod.getConfig().armorHudScale, 1f);
            context.getMatrices().translate(0, -32, 0);
            context.setShaderColor(1f, 1f, 1f, HudMod.getConfig().transparency);

            for (int i = 0; i < 4; i++) {
                ItemStack armorStack = client.player.getInventory().getArmorStack(i);
                int y = 16 * (3 - i);
                if (armorStack.isEmpty()) {
                    draw(context, EMPTY_SLOTS[i], 0, y);
                } else {
                    context.drawItem(armorStack, 0, y);
                    context.drawItemInSlot(client.textRenderer, armorStack, 0, y);
                }
            }
            context.getMatrices().pop();
        }
    }

    public static void onHudRenderDummy(DrawContext context, float xOffset, float scale) {
        MinecraftClient client = MinecraftClient.getInstance();
        int height = client.getWindow().getScaledHeight();

        context.getMatrices().push();
        context.getMatrices().translate(xOffset, height / 2f, 0);
        context.getMatrices().scale(scale, scale, 1f);
        context.getMatrices().translate(0, -32, 0);
        context.setShaderColor(1f, 1f, 1f, HudMod.getConfig().transparency);

        List<ItemStack> armors = List.of(Items.GOLDEN_HELMET.getDefaultStack(), ItemStack.EMPTY, Items.GOLDEN_LEGGINGS.getDefaultStack(), Items.GOLDEN_BOOTS.getDefaultStack());

        for (int i = 0; i < 4; i++) {
            ItemStack armorStack = armors.get(i);
            int y = 16 * (3 - i);
            if (armorStack.isEmpty()) {
                draw(context, EMPTY_SLOTS[i], 0, y);
            } else {
                context.drawItem(armorStack, 0, y);
                context.drawItemInSlot(client.textRenderer, armorStack, 0, y);
            }
        }
        context.getMatrices().pop();

    }

    private static void draw(DrawContext context, Identifier texture, float x, float y) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferBuilder.vertex(matrix4f, x, y, 0f).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
        bufferBuilder.vertex(matrix4f, x, y + 16, 0f).color(1, 1f, 1f, 1f).texture(0f, 1f).next();
        bufferBuilder.vertex(matrix4f, x + 16, y + 16, 0f).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        bufferBuilder.vertex(matrix4f, x + 16, y, 0f).color(1f, 1f, 1f, 1f).texture(1f, 0f).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }
}
