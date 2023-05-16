package net.kyrptonaught.lemclienthelper.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.*;
import net.kyrptonaught.kyrptconfig.config.screen.items.number.IntegerItem;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.AllPacks;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloaderConfig;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloaderMod;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsConfig;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvMod;
import net.kyrptonaught.lemclienthelper.syncedKeybinds.SyncedKeybind;
import net.kyrptonaught.lemclienthelper.syncedKeybinds.SyncedKeybindsConfig;
import net.kyrptonaught.lemclienthelper.syncedKeybinds.SyncedKeybindsMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            ResourcePreloaderConfig config = ResourcePreloaderMod.getConfig();
            ConfigScreen configScreen = new ConfigScreen(screen, Text.translatable("key.lemclienthelper.title"));
            configScreen.setSavingEvent(() -> {
                LEMClientHelperMod.configManager.save();
            });

            ConfigSection rplSection = new ConfigSection(configScreen, Text.translatable("key.lemclienthelper.resourcepreloader"));

            rplSection.addConfigItem(new TextItem(Text.translatable("key.lemclienthelper.downloadurl"), config.URL, ResourcePreloaderConfig.DEFAULT_URL).setMaxLength(1024).setSaveConsumer(val -> config.URL = val));
            rplSection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.allowOptifine"), config.allowOptifine, false).setSaveConsumer(val -> config.allowOptifine = val));
            rplSection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.hideIncompatiblePacks"), config.hideIncompatiblePacks, true).setSaveConsumer(val -> config.hideIncompatiblePacks = val));

            rplSection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.multiDownload"), config.multiDownload, true).setSaveConsumer(val -> config.multiDownload = val));
            rplSection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.toastcomplete"), config.toastComplete, true).setSaveConsumer(val -> config.toastComplete = val));

            rplSection.addConfigItem(new ButtonItem(Text.translatable("key.lemclienthelper.deletePacks")).setClickEvent(() -> {
                configScreen.save();
                ResourcePreloaderMod.deletePacks();
            }));

            SubItem<?> sub = new SubItem<>(Text.translatable("key.lemclienthelper.packdownloads"), true);

            rplSection.addConfigItem(new ButtonItem(Text.translatable("key.lemclienthelper.previewList")).setClickEvent(() -> {
                configScreen.save();
                ResourcePreloaderMod.getPackList();
                addPacksToSub(sub);
            }));

            rplSection.addConfigItem(new ButtonItem(Text.translatable("key.lemclienthelper.startdownload")).setClickEvent(() -> {
                configScreen.save();
                ResourcePreloaderMod.getPackList();
                ResourcePreloaderMod.downloadPacks();
                addPacksToSub(sub);
            }));

            rplSection.addConfigItem(sub);
            addPacksToSub(sub);

            ServerConfigsConfig serverConfig = ServerConfigsMod.getConfig();
            ConfigSection serverConfigSection = new ConfigSection(configScreen, Text.translatable("key.lemclienthelper.serverconfig"));

            IntegerItem guiItem = (IntegerItem) serverConfigSection.addConfigItem(new IntegerItem(Text.translatable("key.lemclienthelper.serverconfig.guiscale"), serverConfig.guiScale, 0));
            guiItem.setMinMax(0, 4);
            guiItem.setSaveConsumer(val -> serverConfig.guiScale = val);
            guiItem.setToolTipWithNewLine("key.lemclienthelper.serverconfig.guiscale.tooltip");

            IntegerItem panItem = (IntegerItem) serverConfigSection.addConfigItem(new IntegerItem(Text.translatable("key.lemclienthelper.serverconfig.panscale"), serverConfig.panScale, 0));
            panItem.setMinMax(0, 4);
            panItem.setSaveConsumer(val -> serverConfig.panScale = val);
            panItem.setToolTipWithNewLine("key.lemclienthelper.serverconfig.panscale.tooltip");


            ConfigSection smallInvSection = new ConfigSection(configScreen, Text.translatable("key.lemclienthelper.smallinv"));
            smallInvSection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.smallinv.enabled"), SmallInvMod.getConfig().enabled, true).setSaveConsumer(val -> SmallInvMod.getConfig().enabled = val));


            SyncedKeybindsConfig syncedKeybindsConfig = SyncedKeybindsMod.getConfig();
            ConfigSection syncedKeybinds = new ConfigSection(configScreen, Text.translatable("key.lemclienthelper.syncedkeybinds"));

            SubItem<KeybindItem> syncedKeybindItems = new SubItem<>(Text.translatable("key.lemclienthelper.syncedkeys"), true);
            syncedKeybindItems.setToolTipWithNewLine("key.lemclienthelper.syncedkeys.tooltip");
            syncedKeybinds.addConfigItem(syncedKeybindItems);

            for (String id : syncedKeybindsConfig.keybinds.keySet()) {
                SyncedKeybindsConfig.KeybindConfigItem keybindConfigItem = syncedKeybindsConfig.keybinds.get(id);
                KeybindItem keybindItem = (KeybindItem) new KeybindItem(Text.translatable("lch.key.sync." + id.replace(":", ".")), keybindConfigItem.keybinding, keybindConfigItem.defaultKeybinding).setSaveConsumer(val -> {
                    keybindConfigItem.keybinding = val;
                    SyncedKeybind syncedKeybind = SyncedKeybindsMod.syncedKeybindList.get(id);
                    if (syncedKeybind != null)
                        syncedKeybind.updateBoundKey(val);
                });
                syncedKeybindItems.addConfigItem(keybindItem);
            }

            return configScreen;
        };
    }

    public void addPacksToSub(SubItem<?> sub) {
        if (ResourcePreloaderMod.allPacks != null && ResourcePreloaderMod.allPacks.packs.size() > 0) {
            sub.clearConfigItems();
            ResourcePreloaderMod.allPacks.packs.forEach(rpOption -> {
                sub.addConfigItem(new RPDownloadItem(rpOption).setToolTip(Text.literal(rpOption.url)));
            });
        }
    }

    public static class RPDownloadItem extends ConfigItem<Object> {
        private final AllPacks.RPOption rpOption;

        public RPDownloadItem(AllPacks.RPOption option) {
            super(Text.literal(option.packname), null, null);
            this.rpOption = option;
        }

        @Override
        public int getContentSize() {
            return 2;
        }

        @Override
        public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
            super.render(matrices, x, y, mouseX, mouseY, delta);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            AllPacks.Progress progressListener = rpOption.progressListener;
            if (progressListener.title != null) {
                int titleX = MinecraftClient.getInstance().getWindow().getScaledWidth() - 90;

                if (progressListener.task == null) {
                    Screen.drawCenteredTextWithShadow(matrices, textRenderer, progressListener.title, titleX, y + 10 - 4, 16777215);
                } else {
                    Text task = (Text.literal("")).append(progressListener.task).append(" " + progressListener.progress + "%");

                    Screen.drawCenteredTextWithShadow(matrices, textRenderer, progressListener.title, titleX, y + 2, 16777215);
                    Screen.drawCenteredTextWithShadow(matrices, textRenderer, task, titleX, y + 11, 16777215);
                }
            }
        }
    }
}