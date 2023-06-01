package net.kyrptonaught.lemclienthelper.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ArmorHud implements HudRenderCallback {
    MinecraftClient client = MinecraftClient.getInstance();
    private static final Identifier EMPTY_HEAD = new Identifier("minecraft", "textures/item/empty_armor_slot_helmet.png");
    private static final Identifier EMPTY_CHEST = new Identifier("minecraft", "textures/item/empty_armor_slot_chestplate.png");
    private static final Identifier EMPTY_LEGS = new Identifier("minecraft", "textures/item/empty_armor_slot_leggings.png");
    private static final Identifier EMPTY_FEET = new Identifier("minecraft", "textures/item/empty_armor_slot_boots.png");

    @Override
    public void onHudRender(MatrixStack matrices, float tickdelta){
        if(client.player !=null){
            NbtCompound nbt = new NbtCompound();
            client.player.writeCustomDataToNbt(nbt);
            int count = 4;
            int height = client.getWindow().getScaledHeight();
            for(ItemStack item : client.player.getArmorItems()){
                int y = (height/2)+(count*16)-(8*4)-16;
                if (item.getItem() == Items.AIR){
                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);

                    RenderSystem.setShaderColor(0f,0f,0f,0.75f);
                    switch (count){
                        case 4:
                            RenderSystem.setShaderTexture(0,EMPTY_FEET);
                            DrawableHelper.drawTexture(matrices,20,y,0,0,16,16,16,16);
                            break;
                        case 3:
                            RenderSystem.setShaderTexture(0,EMPTY_LEGS);
                            DrawableHelper.drawTexture(matrices,20,y,0,0,16,16,16,16);
                            break;
                        case 2:
                            RenderSystem.setShaderTexture(0,EMPTY_CHEST);
                            DrawableHelper.drawTexture(matrices,20,y,0,0,16,16,16,16);
                            break;
                        case 1:
                            RenderSystem.setShaderTexture(0,EMPTY_HEAD);
                            DrawableHelper.drawTexture(matrices,20,y,0,0,16,16,16,16);
                            break;
                    }
                }
                RenderSystem.setShaderColor(1f,1f,1f,0.75f);

                client.getItemRenderer().renderInGui(matrices,item,20,y);
                client.getItemRenderer().renderGuiItemOverlay(matrices,client.textRenderer,item,20,y);
                count--;
            }
        }
    }
}
