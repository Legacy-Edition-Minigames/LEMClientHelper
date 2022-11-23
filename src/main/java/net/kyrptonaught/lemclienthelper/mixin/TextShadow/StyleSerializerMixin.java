package net.kyrptonaught.lemclienthelper.mixin.TextShadow;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.kyrptonaught.lemclienthelper.TextShadow.StyleWShadow;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(Style.Serializer.class)
public class StyleSerializerMixin {

    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/Style;", at = @At("RETURN"), cancellable = true)
    public void deserializeShadow(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<Style> cir) {
        Style style = cir.getReturnValue();
        if (style == null) return;

        JsonObject root = jsonElement.getAsJsonObject();
        if (root.has("shadow")) {
            ((StyleWShadow) style).setShadow(root.get("shadow").getAsBoolean() ? StyleWShadow.ShadowType.FORCE : StyleWShadow.ShadowType.HIDE);
            cir.setReturnValue(style);
        }
    }

    @Inject(method = "serialize(Lnet/minecraft/text/Style;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;", at = @At("RETURN"), cancellable = true)
    public void serializeShadow(Style style, Type type, JsonSerializationContext jsonSerializationContext, CallbackInfoReturnable<JsonElement> cir) {
        JsonElement jsonElement = cir.getReturnValue();
        if (jsonElement == null)
            return;

        JsonObject root = jsonElement.getAsJsonObject();
        StyleWShadow.ShadowType shadowType = ((StyleWShadow) style).hasShadow();

        if (shadowType != StyleWShadow.ShadowType.DEFAULT) {
            root.addProperty("shadow", shadowType == StyleWShadow.ShadowType.FORCE);
            cir.setReturnValue(root);
        }
    }
}
