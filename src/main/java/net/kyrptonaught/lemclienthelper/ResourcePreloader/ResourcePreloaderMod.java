package net.kyrptonaught.lemclienthelper.ResourcePreloader;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.mojang.util.UndashedUuid;
import net.kyrptonaught.jankson.Jankson;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.GameVersion;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.realms.SizeUnit;
import net.minecraft.client.session.Session;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.NetworkUtils;
import net.minecraft.util.Util;
import net.minecraft.util.path.CacheFiles;

import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.OptionalLong;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ResourcePreloaderMod {
    public static String MOD_ID = "resourcepreloader";
    public static AllPacks allPacks;

    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new ResourcePreloaderConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);
    }

    public static ResourcePreloaderConfig getConfig() {
        return (ResourcePreloaderConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }

    public static void getPackList() {
        try {
            URL url = new URL(ResourcePreloaderConfig.DEFAULT_URL);

            Jankson jankson = LEMClientHelperMod.configManager.getJANKSON();
            try (InputStream in = url.openStream()) {
                allPacks = jankson.fromJson(jankson.load(in), AllPacks.class);
            }

            for (int i = allPacks.packs.size() - 1; i >= 0; i--) {
                AllPacks.RPOption rpOption = allPacks.packs.get(i);
                rpOption.uuid = UUID.nameUUIDFromBytes(rpOption.packname.getBytes(StandardCharsets.UTF_8));
                checkPack(rpOption.uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadPacks() {
        Path downloadsDirectory = MinecraftClient.getInstance().runDirectory.toPath().resolve("downloads");
        HashFunction SHA1 = Hashing.sha1();
        Map<String, String> headers = getHeaders(MinecraftClient.getInstance().getSession());
        Proxy proxy = MinecraftClient.getInstance().getNetworkProxy();

        AtomicInteger counter = new AtomicInteger(allPacks.packs.size());
        for (AllPacks.RPOption rpOption : allPacks.packs) {
            if (!checkPack(rpOption.uuid)) {
                Util.getDownloadWorkerExecutor().execute(() -> downloadPack(rpOption, downloadsDirectory, SHA1, headers, proxy, () -> {
                    counter.getAndDecrement();
                    if (counter.get() <= 0 && getConfig().toastComplete) {
                        SystemToast.add(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.PERIODIC_NOTIFICATION, Text.translatable("key.lemclienthelper.alldownloadcomplete"), null);
                    }
                }));
            }
        }
    }

    public static void downloadPack(AllPacks.RPOption rpOption, Path downloadsDirectory, HashFunction SHA1, Map<String, String> headers, Proxy proxy, Runnable onComplete) {
        try {
            NetworkUtils.download(downloadsDirectory.resolve(rpOption.uuid.toString()), new URL(rpOption.url), headers, SHA1, null, 0xFA00000, proxy, createListener(rpOption, onComplete));
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(rpOption.uuid, Text.translatable("key.lemclienthelper.downloaderror"), null);
        }
    }

    public static void deletePacks() {
        CacheFiles.clear(MinecraftClient.getInstance().runDirectory.toPath().resolve("downloads"), 0);
    }

    private static boolean checkPack(UUID packID) {
        Path downloadsDirectory = MinecraftClient.getInstance().runDirectory.toPath().resolve("downloads");
        if (Files.exists(downloadsDirectory.resolve(packID.toString()))) {
            setStatus(packID, Text.translatable("key.lemclienthelper.alreadydownloaded"), null);
            return true;
        }

        setStatus(packID, null, null);
        return false;
    }

    public static void setStatus(UUID packID, Text status, Text status2) {
        for (AllPacks.RPOption rpOption : allPacks.packs) {
            if (rpOption.uuid == packID) {
                rpOption.status = status;
                rpOption.status2 = status2;
            }
        }
    }

    private static Map<String, String> getHeaders(Session session) {
        GameVersion gameVersion = SharedConstants.getGameVersion();
        return Map.of("X-Minecraft-Username", session.getUsername(), "X-Minecraft-UUID", UndashedUuid.toString(session.getUuidOrNull()), "X-Minecraft-Version", gameVersion.getName(), "X-Minecraft-Version-ID", gameVersion.getId(), "X-Minecraft-Pack-Format", String.valueOf(gameVersion.getResourceVersion(ResourceType.CLIENT_RESOURCES)), "User-Agent", "Minecraft Java/" + gameVersion.getName());
    }

    private static NetworkUtils.DownloadListener createListener(AllPacks.RPOption rpOption, Runnable onComplete) {
        return new NetworkUtils.DownloadListener() {
            private OptionalLong contentLength = OptionalLong.empty();

            private Text getProgress(long writtenBytes) {
                return this.contentLength.isPresent() ? Text.translatable("download.pack.progress.percent", writtenBytes * 100L / this.contentLength.getAsLong()) : Text.translatable("download.pack.progress.bytes", SizeUnit.getUserFriendlyString(writtenBytes));
            }

            @Override
            public void onStart() {
                setStatus(rpOption.uuid, Text.translatable("key.lemclienthelper.downloading"), null);
            }

            @Override
            public void onContentLength(OptionalLong contentLength) {
                this.contentLength = contentLength;
                setStatus(rpOption.uuid, Text.translatable("key.lemclienthelper.downloading"), getProgress(0L));
            }

            @Override
            public void onProgress(long writtenBytes) {
                setStatus(rpOption.uuid, Text.translatable("key.lemclienthelper.downloading"), getProgress(writtenBytes));
            }

            @Override
            public void onFinish(boolean success) {
                if (!success) {
                    setStatus(rpOption.uuid, Text.translatable("key.lemclienthelper.downloaderror"), null);
                } else {
                    setStatus(rpOption.uuid, Text.translatable("key.lemclienthelper.downloadcomplete"), null);
                }
                onComplete.run();
            }
        };
    }
}