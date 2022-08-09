package net.kyrptonaught.lemclienthelper.mixin.resourcepreloader;

import net.minecraft.client.resource.ClientBuiltinResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientBuiltinResourcePackProvider.class)
public class ClientBuiltInResourcePackProviderMixin {

    @ModifyConstant(method = "deleteOldServerPack", constant = @Constant(intValue = 10))
    private int dontDeleteLEMPacks(int constant) {
        return 30;
    }
}
