package net.kyrptonaught.lemclienthelper.mixin.resourcepreloader;

import net.kyrptonaught.lemclienthelper.ResourcePreloader.AllPacks;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.text.Text;
import net.minecraft.util.ProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

@Mixin(NetworkUtils.class)
public class NetworkUtilsMixin {

    @Redirect(method = "method_15303", at = @At(value = "INVOKE", target = "Ljava/io/InputStream;read([B)I"))
    private static int catch404(InputStream instance, byte[] b, ProgressListener progressListener) throws IOException {
        if (progressListener instanceof AllPacks.Progress && instance == null) {
            ((AllPacks.Progress) progressListener).skip(Text.translatable("key.lemclienthelper.downloaderror"));
            return -1;
        }
        return instance.read(b);
    }

    @Redirect(method = "method_15303", at = @At(value = "INVOKE", target = "Ljava/net/HttpURLConnection;getInputStream()Ljava/io/InputStream;"))
    private static InputStream catch404(HttpURLConnection instance) {
        try {
            return instance.getInputStream();
        } catch (IOException ignored) {

        }
        return null;
    }
}