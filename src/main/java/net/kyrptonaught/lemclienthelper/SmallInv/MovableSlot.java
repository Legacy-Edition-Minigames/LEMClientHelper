package net.kyrptonaught.lemclienthelper.SmallInv;

import net.minecraft.screen.slot.Slot;

public class MovableSlot extends ExtendedSlot {
    public boolean isEnabled = true;
    private int oldX = -1, oldY = -1;

    public MovableSlot(Slot slot) {
        super(slot);
    }

    @Override
    public boolean isEnabled() {
        return isEnabled && super.isEnabled();
    }

    public void setPos(int x, int y) {
        if (oldX == -1 && oldY == -1) {
            this.oldX = this.x;
            this.oldY = this.y;
        }
        this.x = x;
        this.y = y;
    }

    public void resetPos() {
        if (oldX != -1 && oldY != -1) {
            this.x = oldX;
            this.y = oldY;
        }
        oldX = -1;
        oldY = -1;
        isEnabled = true;
    }
}