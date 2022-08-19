package net.kyrptonaught.lemclienthelper.ResourcePreloader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class ResourcePreloaderMod {
    public static String MOD_ID = "resourcepreloader";
    public static AllPacks allPacks;
    private static boolean downloadsComplete = false;

    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new ResourcePreloaderConfig());
    }

    public static ResourcePreloaderConfig getConfig() {
        return (ResourcePreloaderConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }

    public static void getPackList() {
        try {
            URL url = new URL(getConfig().URL);
            String downloaded = IOUtils.toString(url.openStream(), Charset.defaultCharset());
            allPacks = LEMClientHelperMod.configManager.getJANKSON().fromJson(downloaded, AllPacks.class);
            for (int i = allPacks.packs.size() - 1; i >= 0; i--) {
                AllPacks.RPOption rpOption = allPacks.packs.get(i);
                if (isCompatiblePack(rpOption))
                    download(rpOption, true);
                else if (getConfig().hideIncompatiblePacks)
                    allPacks.packs.remove(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePacks() {
        try {
            ArrayList<File> list = Lists.newArrayList(FileUtils.listFiles(new File(MinecraftClient.getInstance().runDirectory, "server-resource-packs"), TrueFileFilter.TRUE, null));
            for (File file : list) {
                FileUtils.deleteQuietly(file);
            }
        } catch (Exception ignored) {
        }
    }

    public static void downloadPacks() {
        downloadsComplete = false;
        if (getConfig().multiDownload)
            allPacks.packs.forEach(rpOption -> download(rpOption, false));
        else downloadNextPack();
    }

    static void downloadNextPack() {
        for (AllPacks.RPOption rpOption : allPacks.packs) {
            if (!rpOption.progressListener.completed) {
                download(rpOption, false);
                return;
            }
        }
    }

    static void downloadComplete(AllPacks.RPOption rpOption) {
        if (getConfig().toastComplete)
            SystemToast.show(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.TUTORIAL_HINT, rpOption.progressListener.title, Text.literal(rpOption.packname));
        if (!getConfig().multiDownload)
            downloadNextPack();
        if (verifyFile(rpOption.hash, rpOption.downloadedFile)) {
            //todo implement this
        }
        checkIfComplete();
    }

    private static void checkIfComplete() {
        if (downloadsComplete) return;
        for (AllPacks.RPOption rpOption : allPacks.packs) {
            if (!rpOption.progressListener.completed) {
                return;
            }
        }
        downloadsComplete = true;
        SystemToast.add(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.TUTORIAL_HINT, Text.translatable("key.lemclienthelper.alldownloadcomplete"), null);
    }

    private static void download(AllPacks.RPOption rpOption, boolean previewOnly) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        String urlHash = DigestUtils.sha1Hex(rpOption.url);
        File file = new File(new File(minecraftClient.runDirectory, "server-resource-packs"), urlHash);
        rpOption.downloadedFile = file;

        if (file.exists()) {
            rpOption.progressListener.skip(Text.translatable("key.lemclienthelper.alreadydownloaded"));
        } else if (!isCompatiblePack(rpOption)) {
            rpOption.progressListener.skip(Text.translatable("key.lemclienthelper.wrongpackcompatibility"));
        } else if (!previewOnly) {
            Map<String, String> map = getDownloadHeaders();
            try {
                NetworkUtils.downloadResourcePack(file, new URL(rpOption.url), map, Integer.MAX_VALUE, rpOption.progressListener, minecraftClient.getNetworkProxy());
            } catch (MalformedURLException exception) {
                System.out.println("Bad URL detected: " + rpOption.url);
            }
        }
    }

    private static boolean isCompatiblePack(AllPacks.RPOption rpOption) {
        return rpOption.packCompatibility == AllPacks.RPOption.PACKCOMPATIBILITY.BOTH ||
                (getConfig().allowOptifine && rpOption.packCompatibility == AllPacks.RPOption.PACKCOMPATIBILITY.OPTIFINE) ||
                (!getConfig().allowOptifine && rpOption.packCompatibility == AllPacks.RPOption.PACKCOMPATIBILITY.VANILLA);
    }

    private static boolean verifyFile(String expectedSha1, File file) {
        try {
            String string = com.google.common.io.Files.asByteSource(file).hash(Hashing.sha256()).toString();
            if (expectedSha1.isEmpty()) {
                //LOGGER.info("Found file {} without verification hash", (Object)file);
                return true;
            }
            if (string.toLowerCase(Locale.ROOT).equals(expectedSha1.toLowerCase(Locale.ROOT))) {
                //LOGGER.info("Found file {} matching requested hash {}", (Object)file, (Object)expectedSha1);
                return true;
            }
            // LOGGER.warn("File {} had wrong hash (expected {}, found {}).", file, expectedSha1, string);
        } catch (IOException iOException) {
            // LOGGER.warn("File {} couldn't be hashed.", (Object)file, (Object)iOException);
        }
        return false;
    }

    private static Map<String, String> getDownloadHeaders() {
        Map<String, String> map = Maps.newHashMap();
        map.put("X-Minecraft-Username", MinecraftClient.getInstance().getSession().getUsername());
        map.put("X-Minecraft-UUID", MinecraftClient.getInstance().getSession().getUuid());
        map.put("X-Minecraft-Version", SharedConstants.getGameVersion().getName());
        map.put("X-Minecraft-Version-ID", SharedConstants.getGameVersion().getId());
        map.put("X-Minecraft-Pack-Format", String.valueOf(ResourceType.CLIENT_RESOURCES.getPackVersion(SharedConstants.getGameVersion())));
        map.put("User-Agent", "Minecraft Java/" + SharedConstants.getGameVersion().getName());
        return map;
    }
}