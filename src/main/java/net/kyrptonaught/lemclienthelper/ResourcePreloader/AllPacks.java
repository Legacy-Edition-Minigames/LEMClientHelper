package net.kyrptonaught.lemclienthelper.ResourcePreloader;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AllPacks {
    public List<RPOption> packs = new ArrayList<>();

    public static class RPOption {
        public String packname;
        public String url;

        public UUID uuid;
        public Text status;
        public Text status2;

    }
}