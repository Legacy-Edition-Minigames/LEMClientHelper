package net.kyrptonaught.lemclienthelper.mixin.ResourcePreloader;

import net.minecraft.server.packs.DownloadQueue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DownloadQueue.class)
public class ClientBuiltInResourcePackProviderMixin {

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 20))
    private int dontDeleteLEMPacks(int constant) {
        return 30;
    }
}
