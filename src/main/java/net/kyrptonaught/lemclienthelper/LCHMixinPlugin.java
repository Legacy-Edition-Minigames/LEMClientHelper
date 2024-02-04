package net.kyrptonaught.lemclienthelper;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class LCHMixinPlugin implements IMixinConfigPlugin {

    private static final String[] SMALLINV= {
        "net.kyrptonaught.lemclienthelper.mixin.SmallInv.HandledScreenMixin",
        "net.kyrptonaught.lemclienthelper.mixin.SmallInv.MinecraftClientMixin",
        "net.kyrptonaught.lemclienthelper.mixin.SmallInv.ScreenHandlerMixin",
        "net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs.GenericContainerScreenMixin",
        "net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs.InventoryScreenMixin",
        "net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs.MultipleScreenMixin"
    };

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (FabricLoader.getInstance().isModLoaded("legacy")) {
            for (String i : SMALLINV) {
                if (mixinClassName.equals(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onLoad(String mixinPackage) { }

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}
