package net.kyrptonaught.lebclienthelper.mixin;

import net.kyrptonaught.lebclienthelper.ResourcePreloader.AllPacks;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.Map;

@Mixin(NetworkUtils.class)
public class NetworkUtilsMixin {

    //lambda method
    @Inject(method = "method_15303", at = @At(value = "INVOKE", target = "Ljava/net/HttpURLConnection;getInputStream()Ljava/io/InputStream;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void catch404(ProgressListener progressListener, String string, Proxy proxy, Map map, File file, int i, CallbackInfoReturnable<Object> cir, HttpURLConnection httpURLConnection) throws IOException {
        if (progressListener instanceof AllPacks.Progress) {
            if (httpURLConnection.getResponseCode() == 404) {
                progressListener.setDone();
                progressListener.setTitle(new LiteralText("Invalid Pack URL"));
                progressListener.setTask(null);
                cir.setReturnValue(null);
            }
        }
    }
}
