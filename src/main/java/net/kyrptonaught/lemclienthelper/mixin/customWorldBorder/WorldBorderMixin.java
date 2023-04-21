package net.kyrptonaught.lemclienthelper.mixin.customWorldBorder;

import net.kyrptonaught.lemclienthelper.customWorldBorder.CustomWorldBorderArea;
import net.kyrptonaught.lemclienthelper.customWorldBorder.duckInterface.CustomWorldBorder;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldBorder.class)
public abstract class WorldBorderMixin implements CustomWorldBorder {

    @Shadow
    private WorldBorder.Area area;

    @Shadow
    public abstract void setCenter(double x, double z);

    @Shadow
    public abstract void setSize(double size);

    @Override
    public void setShape(double xCenter, double zCenter, double xSize, double zSize) {
        setCenter(xCenter, zCenter);
        this.area = new CustomWorldBorderArea((WorldBorder) (Object) this, xSize, zSize);
    }

    @Override
    public void setShape(double xCenter, double zCenter, double size) {
        setCenter(xCenter, zCenter);
        setSize(size);
    }

    @Inject(method = "addListener", at = @At("HEAD"), cancellable = true)
    public void noListeners(WorldBorderListener listener, CallbackInfo ci) {
        ci.cancel();
    }
}
