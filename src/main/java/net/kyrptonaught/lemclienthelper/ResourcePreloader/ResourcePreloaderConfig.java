package net.kyrptonaught.lemclienthelper.ResourcePreloader;

import blue.endless.jankson.Comment;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

public class ResourcePreloaderConfig implements AbstractConfigFile {
    public static final String DEFAULT_URL = "https://raw.githubusercontent.com/Legacy-Edition-Minigames/Minigames/main/config/serverutils/switchableresourcepacks.json5";

    @Comment("Should display toast's for completed downloads")
    public boolean toastComplete = true;
}
