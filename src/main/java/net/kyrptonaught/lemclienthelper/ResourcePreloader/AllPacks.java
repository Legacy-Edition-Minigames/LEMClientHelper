package net.kyrptonaught.lemclienthelper.ResourcePreloader;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AllPacks {
    public List<RPOption> packs = new ArrayList<>();

    public static class RPOption {
        public String packname;
        public String url;

        public UUID uuid;
        public Component status;
        public Component status2;

    }
}