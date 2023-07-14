package net.kyrptonaught.lemclienthelper.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ArmorHudRenderer {
    private static final Identifier[] EMPTY_SLOTS = new Identifier[] {
            new Identifier("minecraft", "textures/item/empty_armor_slot_boots.png"),
            new Identifier("minecraft", "textures/item/empty_armor_slot_leggings.png"),
            new Identifier("minecraft", "textures/item/empty_armor_slot_chestplate.png"),
            new Identifier("minecraft", "textures/item/empty_armor_slot_helmet.png")
    };

    public static int HUD_SCALE = ((HudMod.getConfig().armorHudScale) + 16);

    public static void updateVars() {
        HUD_SCALE = ((HudMod.getConfig().armorHudScale) + 16);
    }

    private static void renderGuiItemModel(DrawContext context, MinecraftClient client, ItemStack stack, int x, int y, int scale) {
        BakedModel model = client.getItemRenderer().getModel(stack, null, null, 0);
        context.getMatrices().push();
        client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        context.getMatrices().translate(x, y, 100);
        context.getMatrices().translate((scale/2.0), (scale/2.0), 0.0);
        context.getMatrices().scale(1.0f, -1.0f, 1.0f);
        context.getMatrices().scale(scale, scale, scale);
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        boolean bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }
        client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, context.getMatrices(), immediate, 0xF000F0, OverlayTexture.DEFAULT_UV, model);

        immediate.draw();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }
        context.getMatrices().pop();
    }

    public static void onHudRender(DrawContext context, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        // HudMod.SHOULD_RENDER_ARMOR = true;
        if (client.player != null && HudMod.SHOULD_RENDER_ARMOR) {
            int height = client.getWindow().getScaledHeight();

            for (int i = 0; i < 4; i++) {
                ItemStack armorStack = client.player.getInventory().getArmorStack(i);
                int y = (height / 2) + ((4 - i) * HUD_SCALE) - ((HUD_SCALE / 2) * 4) - HUD_SCALE;
                if (armorStack.isEmpty()) {
                    context.drawTexture(EMPTY_SLOTS[i], 20, y, 0, 0, HUD_SCALE, HUD_SCALE, HUD_SCALE, HUD_SCALE);
                } else {
                    renderGuiItemModel(context, client, armorStack, 20, y, HUD_SCALE);
                    context.drawItemInSlot(client.textRenderer, armorStack, 20, y);
                }
            }
        }
    }
}
