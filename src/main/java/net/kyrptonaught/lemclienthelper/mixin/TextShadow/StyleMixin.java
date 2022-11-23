package net.kyrptonaught.lemclienthelper.mixin.TextShadow;

import net.kyrptonaught.lemclienthelper.TextShadow.StyleWShadow;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Style.class)
public class StyleMixin implements StyleWShadow {

    private ShadowType shadowType = ShadowType.DEFAULT;


    @Override
    public ShadowType hasShadow() {
        return shadowType;
    }

    @Override
    public void setShadow(ShadowType type) {
        this.shadowType = type;
    }
}
