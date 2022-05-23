package net.kyrptonaught.lemclienthelper.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.*;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.AllPacks;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloader;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloaderConfig;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvInit;
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
            ResourcePreloaderConfig config = ResourcePreloader.getConfig();
            ConfigScreen configScreen = new ConfigScreen(screen, new TranslatableText("key.lemclienthelper.title"));
            configScreen.setSavingEvent(() -> {
                LEMClientHelperMod.configManager.save();
            });

            ConfigSection rplSection = new ConfigSection(configScreen, new TranslatableText("key.lemclienthelper.resourcepreloader"));

            rplSection.addConfigItem(new TextItem(new TranslatableText("key.lemclienthelper.downloadurl"), config.URL, ResourcePreloaderConfig.DEFAULT_URL).setMaxLength(1024).setSaveConsumer(val -> config.URL = val));
            rplSection.addConfigItem(new BooleanItem(new TranslatableText("key.lemclienthelper.allowOptifine"), config.allowOptifine, false).setSaveConsumer(val -> config.allowOptifine = val));
            rplSection.addConfigItem(new BooleanItem(new TranslatableText("key.lemclienthelper.multiDownload"), config.multiDownload, true).setSaveConsumer(val -> config.multiDownload = val));
            rplSection.addConfigItem(new BooleanItem(new TranslatableText("key.lemclienthelper.toastcomplete"), config.toastComplete, true).setSaveConsumer(val -> config.toastComplete = val));

            rplSection.addConfigItem(new ButtonItem(new TranslatableText("key.lemclienthelper.deletePacks")).setClickEvent(() -> {
                configScreen.save();
                ResourcePreloader.deletePacks();
            }));

            SubItem<?> sub = new SubItem<>(new TranslatableText("key.lemclienthelper.packdownloads"), true);

            rplSection.addConfigItem(new ButtonItem(new TranslatableText("key.lemclienthelper.previewList")).setClickEvent(() -> {
                configScreen.save();
                ResourcePreloader.getPackList();
                addPacksToSub(sub);
            }));

            rplSection.addConfigItem(new ButtonItem(new TranslatableText("key.lemclienthelper.startdownload")).setClickEvent(() -> {
                configScreen.save();
                ResourcePreloader.getPackList();
                ResourcePreloader.downloadPacks();
                addPacksToSub(sub);
            }));

            rplSection.addConfigItem(sub);
            addPacksToSub(sub);

            ConfigSection smallInvSection = new ConfigSection(configScreen, new TranslatableText("key.lemclienthelper.smallinv"));
            smallInvSection.addConfigItem(new BooleanItem(new TranslatableText("key.lemclienthelper.smallinv.enabled"), SmallInvInit.getConfig().enabled, true).setSaveConsumer(val -> SmallInvInit.getConfig().enabled = val));

            return configScreen;
        };
    }

    public void addPacksToSub(SubItem<?> sub) {
        if (ResourcePreloader.allPacks != null && ResourcePreloader.allPacks.packs.size() > 0) {
            sub.clearConfigItems();
            ResourcePreloader.allPacks.packs.forEach(rpOption -> {
                sub.addConfigItem(new RPDownloadItem(rpOption).setToolTip(new LiteralText(rpOption.url)));
            });
        }
    }

    public static class RPDownloadItem extends ConfigItem<Object> {
        private final AllPacks.RPOption rpOption;

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