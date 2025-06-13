package net.kyrptonaught.lemclienthelper.customWorldBorder;

import net.minecraft.world.phys.shapes.BooleanOp;
import 	net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.border.BorderStatus;
import net.minecraft.world.level.border.WorldBorder;

public class CustomWorldBorderArea implements WorldBorder.BorderExtent {
    private final WorldBorder worldBorder;
    private final double xSize, zSize;

    public CustomWorldBorderArea(WorldBorder worldBorder, double xSize, double zSize) {
        this.worldBorder = worldBorder;
        this.xSize = xSize;
        this.zSize = zSize;
    }


    @Override
    public double getMinX() {
        return worldBorder.getCenterX() - xSize;
    }

    @Override
    public double getMaxX() {
        return worldBorder.getCenterX() + xSize;
    }

    @Override
    public double getMinZ() {
        return worldBorder.getCenterZ() - zSize;
    }

    @Override
    public double getMaxZ() {
        return worldBorder.getCenterZ() + zSize;
    }

    @Override
    public double getSize() {
        return Math.max(xSize, zSize);
    }

    @Override
    public double getLerpSpeed() {
        return 0;
    }

    @Override
    public long getLerpRemainingTime() {
        return 0;
    }

    @Override
    public double getLerpTarget() {
        return 0;
    }

    @Override
    public BorderStatus getStatus() {
        return BorderStatus.STATIONARY;
    }

    @Override
    public void onAbsoluteMaxSizeChange() {
    }

    @Override
    public void onCenterChange() {
    }

    @Override
    public WorldBorder.BorderExtent update() {
        return this;
    }

    @Override
    public VoxelShape getCollisionShape() {
        return Shapes.join(Shapes.INFINITY, Shapes.box(Math.floor(this.getMinX()), Double.NEGATIVE_INFINITY, Math.floor(this.getMinZ()), Math.ceil(this.getMaxX()), Double.POSITIVE_INFINITY, Math.ceil(this.getMaxZ())), BooleanOp.ONLY_FIRST);
    }
}
