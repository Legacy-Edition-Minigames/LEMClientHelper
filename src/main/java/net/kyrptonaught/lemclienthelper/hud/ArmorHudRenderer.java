package net.kyrptonaught.lemclienthelper.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ArmorHudRenderer {
    private static final Identifier EMPTY_HEAD = new Identifier("minecraft", "textures/item/empty_armor_slot_helmet.png");
    private static final Identifier EMPTY_CHEST = new Identifier("minecraft", "textures/item/empty_armor_slot_chestplate.png");
    private static final Identifier EMPTY_LEGS = new Identifier("minecraft", "textures/item/empty_armor_slot_leggings.png");
    private static final Identifier EMPTY_FEET = new Identifier("minecraft", "textures/item/empty_armor_slot_boots.png");

    public static void onHudRender(MatrixStack matrices, float tickdelta){
        //change gameID check to null and gameActive to false when testing
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player !=null && HudMod.SHOULD_RENDER_ARMOR){
            int count =4;
            int height = client.getWindow().getScaledHeight();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1f,1f,1f,0.75f);

            for(ItemStack item : client.player.getArmorItems()){
                int y = (height/2)+(count*16)-(8*4)-16;
                if (item.getItem() == Items.AIR){
                    switch (count) {
                        case 4 -> RenderSystem.setShaderTexture(0, EMPTY_FEET);
                        case 3 -> RenderSystem.setShaderTexture(0, EMPTY_LEGS);
                        case 2 -> RenderSystem.setShaderTexture(0, EMPTY_CHEST);
                        case 1 -> RenderSystem.setShaderTexture(0, EMPTY_HEAD);
                    }
                    DrawableHelper.drawTexture(matrices, 20, y, 0, 0, 16, 16, 16, 16);
                }
                count--;
            }
            count =4;
            for(ItemStack item : client.player.getArmorItems()){
                int y = (height/2)+(count*16)-(8*4)-16;
                if (item.getItem() != Items.AIR){
                    client.getItemRenderer().renderInGui(matrices,item,20,y);
                    client.getItemRenderer().renderGuiItemOverlay(matrices,client.textRenderer,item,20,y);

                }
                count--;
            }
            RenderSystem.setShaderColor(1,1,1,1);
        }
    }
}
