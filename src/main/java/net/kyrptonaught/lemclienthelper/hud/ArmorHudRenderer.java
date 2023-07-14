package net.kyrptonaught.lemclienthelper.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
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

    public static void onHudRender(DrawContext context, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        //HudMod.SHOULD_RENDER_ARMOR = true;
        if (client.player != null && HudMod.SHOULD_RENDER_ARMOR) {
            int height = client.getWindow().getScaledHeight();

            for (int i = 0; i < 4; i++) {
                ItemStack armorStack = client.player.getInventory().getArmorStack(i);
                int y = (height / 2) + ((4 - i) * HUD_SCALE) - ((HUD_SCALE / 2) * 4) - HUD_SCALE;
                if (armorStack.isEmpty()) {
                    context.drawTexture(EMPTY_SLOTS[i], 20, y, 0, 0, HUD_SCALE, HUD_SCALE, HUD_SCALE, HUD_SCALE);
                } else {
                    context.drawItem(armorStack, 20, y);
                    context.drawItemInSlot(client.textRenderer, armorStack, 20, y);
                }
            }
        }
    }
}
