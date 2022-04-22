package net.kyrptonaught.lebclienthelper.ResourcePreloader;

import blue.endless.jankson.Jankson;
import com.google.common.collect.Maps;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.LiteralText;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class ResourcePreloader {
    public static String URL = "https://raw.githubusercontent.com/DBTDerpbox/Legacy-Edition-Battle/main/config/switchableresourcepacksconfig.json5";
    public static AllPacks allPacks;

    public static void preload() {
        try {
            URL url = new URL(URL);
            String downloaded = IOUtils.toString(url.openStream());
            allPacks = Jankson.builder().build().fromJson(downloaded, AllPacks.class);

            //AllPacks.RPOption rpOption = allPacks.packs.get(1);
            allPacks.packs.forEach(rpOption -> {
                download(rpOption);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void download(AllPacks.RPOption rpOption) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        String urlHash = DigestUtils.sha1Hex(rpOption.url);
        File file = new File(new File(minecraftClient.runDirectory, "server-resource-packs"), urlHash);
        if (file.exists()) { //already downloaded
            rpOption.progressListener.setDone();
            rpOption.progressListener.title = new LiteralText("Already Downloaded");
        } else {
            Map<String, String> map = getDownloadHeaders();
            NetworkUtils.downloadResourcePack(file, rpOption.url, map, Integer.MAX_VALUE, rpOption.progressListener, minecraftClient.getNetworkProxy());
        }
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