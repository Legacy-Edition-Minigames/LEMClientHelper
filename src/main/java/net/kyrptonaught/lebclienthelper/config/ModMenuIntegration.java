package net.kyrptonaught.lebclienthelper.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.ButtonItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.ConfigItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.SubItem;
import net.kyrptonaught.lebclienthelper.ResourcePreloader.AllPacks;
import net.kyrptonaught.lebclienthelper.ResourcePreloader.ResourcePreloader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            //ConfigOptions options = InventorySorterModClient.getConfig();

            ConfigScreen configScreen = new ConfigScreen(screen, new TranslatableText("Inventory Sorting Config"));
            configScreen.setSavingEvent(() -> {
            });

            ConfigSection displaySection = new ConfigSection(configScreen, new TranslatableText("key.lemclienthelper.resourcepreloader"));

            SubItem sub = (SubItem) displaySection.addConfigItem(new SubItem(new TranslatableText("key.lemclienthelper.packdownloads"), true));
            addPacksToSub(sub);

            displaySection.insertConfigItem(new ButtonItem(new TranslatableText("key.lemclienthelper.startdownload")).setClickEvent(() -> {
                ResourcePreloader.preload();
                addPacksToSub(sub);
            }), 0);
            return configScreen;
        };
    }

    public void addPacksToSub(SubItem sub) {
        if (ResourcePreloader.allPacks != null && ResourcePreloader.allPacks.packs.size() > 0) {
            sub.clearConfigItems();
            ResourcePreloader.allPacks.packs.forEach(rpOption -> {
                sub.addConfigItem(new RPDownloadItem(rpOption));
            });
        }
    }

    public class RPDownloadItem extends ConfigItem {
        AllPacks.RPOption rpOption;

        public RPDownloadItem(AllPacks.RPOption option) {
            super(new LiteralText(option.packname), null, null);
            this.rpOption = option;
        }

        @Override
        public int getSize() {
            return 30;
        }

        @Override
        public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
            super.render(matrices, x, y, mouseX, mouseY, delta);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            AllPacks.Progress progressListener = rpOption.progressListener;
            if (progressListener.title != null) {
                int titleX = MinecraftClient.getInstance().getWindow().getScaledWidth() - 90;
                Screen.drawCenteredText(matrices, textRenderer, progressListener.title, titleX, y, 16777215);

                if (rpOption.progressListener.task != null) {
                    Text task = (new LiteralText("")).append(progressListener.task).append(" " + progressListener.progress + "%");
                    Screen.drawCenteredText(matrices, textRenderer, task, titleX, y + 10, 16777215);
                }
            }
        }
    }
}