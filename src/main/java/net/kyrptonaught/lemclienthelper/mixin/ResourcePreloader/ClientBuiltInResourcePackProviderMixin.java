package net.kyrptonaught.lemclienthelper.mixin.ResourcePreloader;


import net.minecraft.client.resource.ServerResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerResourcePackProvider.class)
public class ClientBuiltInResourcePackProviderMixin {

    @ModifyConstant(method = "deleteOldServerPack", constant = @Constant(intValue = 10))
    private int dontDeleteLEMPacks(int constant) {
        return 30;
    }
}
