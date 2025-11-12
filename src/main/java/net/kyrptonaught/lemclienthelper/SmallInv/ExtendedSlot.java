package net.kyrptonaught.lemclienthelper.SmallInv;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ExtendedSlot extends Slot {
    private final Slot baseSlot;

    public ExtendedSlot(Slot slot) {
        super(slot.container, slot.getContainerSlot(), slot.x, slot.y);
        baseSlot = slot;
        this.index = slot.index;
    }

    public void onQuickCraft(ItemStack newItem, ItemStack original) {
        baseSlot.onQuickCraft(newItem, original);
    }

    protected void onQuickCraft(ItemStack stack, int amount) {
        super.onQuickCraft(stack, amount);
    }

    protected void onSwapCraft(int amount) {
        super.onSwapCraft(amount);
    }

    protected void checkTakeAchievements(ItemStack stack) {
        super.checkTakeAchievements(stack);
    }

    public void onTake(Player player, ItemStack stack) {
        baseSlot.onTake(player, stack);
    }

    public boolean mayPlace(ItemStack stack) {
        return baseSlot.mayPlace(stack);
    }

    public ItemStack getItem() {
        return baseSlot.getItem();
    }

    public boolean hasItem() {
        return baseSlot.hasItem();
    }

    public void setByPlayer(ItemStack stack) {
        baseSlot.setByPlayer(stack);
    }

    public void setChanged() {
        baseSlot.setChanged();
    }

    public int getMaxStackSize() {
        return baseSlot.getMaxStackSize();
    }

    public int getMaxStackSize(ItemStack stack) {
        return baseSlot.getMaxStackSize(stack);
    }

    public ResourceLocation getNoItemIcon() {
        return baseSlot.getNoItemIcon();
    }

    public ItemStack remove(int amount) {
        return baseSlot.remove(amount);
    }

    public boolean mayPickup(Player playerEntity) {
        return baseSlot.mayPickup(playerEntity);
    }

    public boolean isActive() {
        return baseSlot.isActive();
    }

    public Optional<ItemStack> tryRemove(int min, int max, Player player) {
        return baseSlot.tryRemove(min, max, player);
    }

    public ItemStack safeTake(int min, int max, Player player) {
        return baseSlot.safeTake(min, max, player);
    }

    public ItemStack safeInsert(ItemStack stack) {
        return baseSlot.safeInsert(stack);
    }

    public ItemStack safeInsert(ItemStack stack, int count) {
        return baseSlot.safeInsert(stack, count);
    }

    public boolean allowModification(Player player) {
        return baseSlot.allowModification(player);
    }

    public int getContainerSlot() {
        return baseSlot.getContainerSlot();
    }
}
