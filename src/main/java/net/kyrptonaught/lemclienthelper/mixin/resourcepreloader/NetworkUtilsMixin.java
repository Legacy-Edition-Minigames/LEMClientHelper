package net.kyrptonaught.lemclienthelper.mixin.resourcepreloader;

import net.kyrptonaught.lemclienthelper.ResourcePreloader.AllPacks;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

@Mixin(NetworkUtils.class)
public class NetworkUtilsMixin {


    @Inject(method = "method_15303", at = @At(value = "INVOKE", target = "Ljava/net/HttpURLConnection;setInstanceFollowRedirects(Z)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void getHTTPCON(ProgressListener progressListener, String string, Proxy proxy, Map map, File file, int i, CallbackInfoReturnable<Object> cir, HttpURLConnection httpURLConnection) {
        if (progressListener instanceof AllPacks.Progress)
            ((AllPacks.Progress) progressListener).rpOption.MIXINISBEINGDUMB = httpURLConnection;
    }

    @Inject(method = "method_15303", at = @At(value = "INVOKE", target = "Ljava/lang/Throwable;printStackTrace()V"), cancellable = true)
    private static void catch404(ProgressListener progressListener, String string, Proxy proxy, Map map, File file, int i, CallbackInfoReturnable<Object> cir) throws IOException {
        if (progressListener instanceof AllPacks.Progress && ((AllPacks.Progress) progressListener).rpOption.MIXINISBEINGDUMB != null) {
            if (((AllPacks.Progress) progressListener).rpOption.MIXINISBEINGDUMB.getResponseCode() == 404) {
                ((AllPacks.Progress) progressListener).skip(new TranslatableText("key.lemclienthelper.downloaderror"));
                ((AllPacks.Progress) progressListener).rpOption.MIXINISBEINGDUMB = null;
                cir.setReturnValue(null);
            }
        }
    }
}
