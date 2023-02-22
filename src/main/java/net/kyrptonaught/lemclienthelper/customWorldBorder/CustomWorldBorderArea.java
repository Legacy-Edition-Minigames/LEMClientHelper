package net.kyrptonaught.lemclienthelper.customWorldBorder;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderStage;

public class CustomWorldBorderArea implements WorldBorder.Area {
    private final WorldBorder worldBorder;
    private final double xSize, zSize;

    public CustomWorldBorderArea(WorldBorder worldBorder, double xSize, double zSize) {
        this.worldBorder = worldBorder;
        this.xSize = xSize;
        this.zSize = zSize;
    }


    @Override
    public double getBoundWest() {
        return worldBorder.getCenterX() - xSize;
    }

    @Override
    public double getBoundEast() {
        return worldBorder.getCenterX() + xSize;
    }

    @Override
    public double getBoundNorth() {
        return worldBorder.getCenterZ() - zSize;
    }

    @Override
    public double getBoundSouth() {
        return worldBorder.getCenterZ() + zSize;
    }

    @Override
    public double getSize() {
        return Math.max(xSize, zSize);
    }

    @Override
    public double getShrinkingSpeed() {
        return 0;
    }

    @Override
    public long getSizeLerpTime() {
        return 0;
    }

    @Override
    public double getSizeLerpTarget() {
        return 0;
    }

    @Override
    public WorldBorderStage getStage() {
        return WorldBorderStage.STATIONARY;
    }

    @Override
    public void onMaxRadiusChanged() {
    }

    @Override
    public void onCenterChanged() {
    }

    @Override
    public WorldBorder.Area getAreaInstance() {
        return this;
    }

    @Override
    public VoxelShape asVoxelShape() {
        return VoxelShapes.combineAndSimplify(VoxelShapes.UNBOUNDED, VoxelShapes.cuboid(Math.floor(this.getBoundWest()), Double.NEGATIVE_INFINITY, Math.floor(this.getBoundNorth()), Math.ceil(this.getBoundEast()), Double.POSITIVE_INFINITY, Math.ceil(this.getBoundSouth())), BooleanBiFunction.ONLY_FIRST);
    }
}
