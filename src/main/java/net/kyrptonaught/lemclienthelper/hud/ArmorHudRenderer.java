package net.kyrptonaught.lemclienthelper.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

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

            for (int i = 0; i < 4; i++) {
                ItemStack armorStack = client.player.getInventory().getArmorStack(i);
                int y = 16 * (3 - i);
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
