package net.kyrptonaught.lemclienthelper.ResourcePreloader;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.mojang.realmsclient.Unit;
import com.mojang.util.UndashedUuid;
import net.kyrptonaught.jankson.Jankson;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.DownloadCacheCleaner;
import net.minecraft.server.packs.PackType;
import 	net.minecraft.util.HttpUtil;
import net.minecraft.Util;

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
        Path downloadsDirectory = Minecraft.getInstance().gameDirectory.toPath().resolve("downloads");
        HashFunction SHA1 = Hashing.sha1();
        Map<String, String> headers = getHeaders(Minecraft.getInstance().getUser());
        Proxy proxy = Minecraft.getInstance().getProxy();

        AtomicInteger counter = new AtomicInteger(allPacks.packs.size());
        for (AllPacks.RPOption rpOption : allPacks.packs) {
            if (!checkPack(rpOption.uuid)) {
                Util.nonCriticalIoPool().execute(() -> downloadPack(rpOption, downloadsDirectory, SHA1, headers, proxy, () -> {
                    counter.getAndDecrement();
                    if (counter.get() <= 0 && getConfig().toastComplete) {
                        SystemToast.add(Minecraft.getInstance().getToasts(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, Component.translatable("key.lemclienthelper.alldownloadcomplete"), null);
                    }
                }));
            }
        }
    }

    public static void downloadPack(AllPacks.RPOption rpOption, Path downloadsDirectory, HashFunction SHA1, Map<String, String> headers, Proxy proxy, Runnable onComplete) {
        try {
            HttpUtil.downloadFile(downloadsDirectory.resolve(rpOption.uuid.toString()), new URL(rpOption.url), headers, SHA1, null, 0xFA00000, proxy, createListener(rpOption, onComplete));
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(rpOption.uuid, Component.translatable("key.lemclienthelper.downloaderror"), null);
        }
    }

    public static void deletePacks() {
        DownloadCacheCleaner.vacuumCacheDir(Minecraft.getInstance().gameDirectory.toPath().resolve("downloads"), 0);
    }

    private static boolean checkPack(UUID packID) {
        Path downloadsDirectory = Minecraft.getInstance().gameDirectory.toPath().resolve("downloads");
        if (Files.exists(downloadsDirectory.resolve(packID.toString()))) {
            setStatus(packID, Component.translatable("key.lemclienthelper.alreadydownloaded"), null);
            return true;
        }

        setStatus(packID, null, null);
        return false;
    }

    public static void setStatus(UUID packID, Component status, Component status2) {
        for (AllPacks.RPOption rpOption : allPacks.packs) {
            if (rpOption.uuid == packID) {
                rpOption.status = status;
                rpOption.status2 = status2;
            }
        }
    }

    private static Map<String, String> getHeaders(User session) {
        WorldVersion gameVersion = SharedConstants.getCurrentVersion();
        return Map.of("X-Minecraft-Username", session.getName(), "X-Minecraft-UUID", UndashedUuid.toString(session.getProfileId()), "X-Minecraft-Version", gameVersion.getName(), "X-Minecraft-Version-ID", gameVersion.getId(), "X-Minecraft-Pack-Format", String.valueOf(gameVersion.getPackVersion(PackType.CLIENT_RESOURCES)), "User-Agent", "Minecraft Java/" + gameVersion.getName());
    }

    private static HttpUtil.DownloadProgressListener createListener(AllPacks.RPOption rpOption, Runnable onComplete) {
        return new HttpUtil.DownloadProgressListener() {
            private OptionalLong contentLength = OptionalLong.empty();

            private Component getProgress(long writtenBytes) {
                return this.contentLength.isPresent() ? Component.translatable("download.pack.progress.percent", writtenBytes * 100L / this.contentLength.getAsLong()) : Component.translatable("download.pack.progress.bytes", Unit.humanReadable(writtenBytes));
            }

            @Override
            public void requestStart() {
                setStatus(rpOption.uuid, Component.translatable("key.lemclienthelper.downloading"), null);
            }

            @Override
            public void downloadStart(OptionalLong contentLength) {
                this.contentLength = contentLength;
                setStatus(rpOption.uuid, Component.translatable("key.lemclienthelper.downloading"), getProgress(0L));
            }

            @Override
            public void downloadedBytes(long writtenBytes) {
                setStatus(rpOption.uuid, Component.translatable("key.lemclienthelper.downloading"), getProgress(writtenBytes));
            }

            @Override
            public void requestFinished(boolean success) {
                if (!success) {
                    setStatus(rpOption.uuid, Component.translatable("key.lemclienthelper.downloaderror"), null);
                } else {
                    setStatus(rpOption.uuid, Component.translatable("key.lemclienthelper.downloadcomplete"), null);
                }
                onComplete.run();
            }
        };
    }
}