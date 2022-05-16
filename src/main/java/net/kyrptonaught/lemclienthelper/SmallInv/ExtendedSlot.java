package net.kyrptonaught.lemclienthelper.SmallInv;


import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ExtendedSlot extends Slot {
    private final Slot baseSlot;

    public ExtendedSlot(Slot slot) {
        super(slot.inventory, slot.getIndex(), slot.x, slot.y);
        baseSlot = slot;
        this.id = slot.id;
    }

    public void onQuickTransfer(ItemStack newItem, ItemStack original) {
        baseSlot.onQuickTransfer(newItem, original);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        super.onCrafted(stack, amount);
    }

    protected void onTake(int amount) {
        super.onTake(amount);
    }

    protected void onCrafted(ItemStack stack) {
        super.onCrafted(stack);
    }

    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        baseSlot.onTakeItem(player, stack);
    }

    public boolean canInsert(ItemStack stack) {
        return baseSlot.canInsert(stack);
    }

    public ItemStack getStack() {
        return baseSlot.getStack();
    }

    public boolean hasStack() {
        return baseSlot.hasStack();
    }

    public void setStack(ItemStack stack) {
        baseSlot.setStack(stack);
    }

    public void markDirty() {
        baseSlot.markDirty();
    }

    public int getMaxItemCount() {
        return baseSlot.getMaxItemCount();
    }

    public int getMaxItemCount(ItemStack stack) {
        return baseSlot.getMaxItemCount(stack);
    }

    @Nullable
    public Pair<Identifier, Identifier> getBackgroundSprite() {
        return baseSlot.getBackgroundSprite();
    }

    public ItemStack takeStack(int amount) {
        return baseSlot.takeStack(amount);
    }

    public boolean canTakeItems(PlayerEntity playerEntity) {
        return baseSlot.canTakeItems(playerEntity);
    }

    public boolean isEnabled() {
        return baseSlot.isEnabled();
    }

    public Optional<ItemStack> tryTakeStackRange(int min, int max, PlayerEntity player) {
        return baseSlot.tryTakeStackRange(min, max, player);
    }

    public ItemStack takeStackRange(int min, int max, PlayerEntity player) {
        return baseSlot.takeStackRange(min, max, player);
    }

    public ItemStack insertStack(ItemStack stack) {
        return baseSlot.insertStack(stack);
    }

    public ItemStack insertStack(ItemStack stack, int count) {
        return baseSlot.insertStack(stack, count);
    }

    public boolean canTakePartial(PlayerEntity player) {
        return baseSlot.canTakePartial(player);
    }

    public int getIndex() {
        return baseSlot.getIndex();
    }
}
