package net.kyrptonaught.lemclienthelper.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
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

    public static float HUD_SCALE = ((HudMod.getConfig().armorHudScale));

    public static void updateVars() {
        HUD_SCALE = ((HudMod.getConfig().armorHudScale));
    }

    public static void onHudRender(DrawContext context, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        //HudMod.SHOULD_RENDER_ARMOR = true;
        if (client.player != null && HudMod.SHOULD_RENDER_ARMOR) {
            int height = client.getWindow().getScaledHeight();

            context.getMatrices().push();
            context.getMatrices().translate(20,height/2f,0);
            context.getMatrices().scale(HUD_SCALE, HUD_SCALE, 1f);
            context.getMatrices().translate(0, -32,0);

            for (int i = 0; i < 4; i++) {
                ItemStack armorStack = client.player.getInventory().getArmorStack(i);
                int y = 16 * (3-i);
                //int y = ((height / 2) + ((4 - i) * size) - (8 * 4) - size);
                if (armorStack.isEmpty()) {
                    context.drawTexture(EMPTY_SLOTS[i], 0, y, 0, 0, 16, 16, 16, 16);
                } else {
                    context.drawItem(armorStack, 0, y);
                    context.drawItemInSlot(client.textRenderer, armorStack, 0, y);
                }
            }
            context.getMatrices().pop();
        }
    }
}
