package net.kyrptonaught.lemclienthelper.mixin.DamageIndicator;

import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class DamageAngleMixin {
    @Inject(method = "animateDamage", at = @At("HEAD"))
    private void onAnimateDamage(float yaw, CallbackInfo ci) {
        // Ensure this logic only runs for the local player on the client
        if ((Object)this instanceof ClientPlayerEntity player) {
            // Fix MC related stupidity:
            float angle = MathHelper.wrapDegrees(-yaw + 90);

            HudMod.setDMGAngle(angle);
            HudMod.resetTimeForAngle(angle);
        }
    }
}
