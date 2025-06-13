package net.kyrptonaught.lemclienthelper.hud.armorHud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;

import java.util.List;

public class ArmorHudRenderer {
    private static final ResourceLocation[] EMPTY_SLOTS = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/empty_armor_slot_boots.png"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/empty_armor_slot_leggings.png"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/empty_armor_slot_chestplate.png"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/empty_armor_slot_helmet.png")
    };

    public static void onHudRender(GuiGraphics context, DeltaTracker v) {
        Minecraft client = Minecraft.getInstance();
        //HudMod.SHOULD_RENDER_ARMOR = true;
        if (client.player != null && ArmorHudMod.shouldDisplayArmor() && !client.options.hideGui) {
            int height = client.getWindow().getGuiScaledHeight();

            context.pose().pushPose();
            context.pose().translate(HudMod.getConfig().xOffset, height / 2f, 0);
            context.pose().scale(HudMod.getConfig().armorHudScale, HudMod.getConfig().armorHudScale, 1f);
            context.pose().translate(0, -32, 0);
            context.setColor(1f, 1f, 1f, HudMod.getConfig().transparency);

            for (int i = 0; i < 4; i++) {
                ItemStack armorStack = client.player.getInventory().getArmor(i);
                int y = 16 * (3 - i);
                if (armorStack.isEmpty()) {
                    draw(context, EMPTY_SLOTS[i], 0, y);
                } else {
                    context.renderItem(armorStack, 0, y);
                    context.renderItemDecorations(client.font, armorStack, 0, y);
                }
            }
            context.setColor(1f, 1f, 1f, 1f);
            context.pose().popPose();
        }
    }

    public static void onHudRenderDummy(GuiGraphics context, float xOffset, float scale) {
        Minecraft client = Minecraft.getInstance();
        int height = client.getWindow().getGuiScaledHeight();

        context.pose().pushPose();
        context.pose().translate(xOffset, height / 2f, 0);
        context.pose().scale(scale, scale, 1f);
        context.pose().translate(0, -32, 0);
        context.setColor(1f, 1f, 1f, HudMod.getConfig().transparency);

        List<ItemStack> armors = List.of(Items.GOLDEN_HELMET.getDefaultInstance(), ItemStack.EMPTY, Items.GOLDEN_LEGGINGS.getDefaultInstance(), Items.GOLDEN_BOOTS.getDefaultInstance());

        for (int i = 0; i < 4; i++) {
            ItemStack armorStack = armors.get(i);
            int y = 16 * (3 - i);
            if (armorStack.isEmpty()) {
                draw(context, EMPTY_SLOTS[i], 0, y);
            } else {
                context.renderItem(armorStack, 0, y);
                context.renderItemDecorations(client.font, armorStack, 0, y);
            }
        }
        context.setColor(1f, 1f, 1f, 1f);
        context.pose().popPose();
    }

    private static void draw(GuiGraphics context, ResourceLocation texture, float x, float y) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = context.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.addVertex(matrix4f, x, y, 0f).setColor(1f, 1f, 1f, 1f).setUv(0f, 0f);
        bufferBuilder.addVertex(matrix4f, x, y + 16, 0f).setColor(1, 1f, 1f, 1f).setUv(0f, 1f);
        bufferBuilder.addVertex(matrix4f, x + 16, y + 16, 0f).setColor(1f, 1f, 1f, 1f).setUv(1f, 1f);
        bufferBuilder.addVertex(matrix4f, x + 16, y, 0f).setColor(1f, 1f, 1f, 1f).setUv(1f, 0f);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }
}
