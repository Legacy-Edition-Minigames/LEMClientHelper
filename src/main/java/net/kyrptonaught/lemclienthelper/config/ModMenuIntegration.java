package net.kyrptonaught.lemclienthelper.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.*;
import net.kyrptonaught.kyrptconfig.config.screen.items.number.FloatItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.number.IntegerItem;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.hud.HudConfig;
import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloaderConfig;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloaderMod;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsConfig;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvMod;
import net.kyrptonaught.lemclienthelper.syncedKeybinds.SyncedKeybind;
import net.kyrptonaught.lemclienthelper.syncedKeybinds.SyncedKeybindsConfig;
import net.kyrptonaught.lemclienthelper.syncedKeybinds.SyncedKeybindsMod;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            ConfigScreen configScreen = new ConfigScreen(screen, Text.translatable("key.lemclienthelper.title"));
            configScreen.setSavingEvent(() -> {
                LEMClientHelperMod.configManager.save();
            });

            //Resource Preloader
            ResourcePreloaderConfig config = ResourcePreloaderMod.getConfig();
            ConfigSection rplSection = new ConfigSection(configScreen, Text.translatable("key.lemclienthelper.resourcepreloader"));

            rplSection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.toastcomplete"), config.toastComplete, true).setSaveConsumer(val -> config.toastComplete = val));

            SubItem<?> sub = new SubItem<>(Text.translatable("key.lemclienthelper.packdownloads"), true);

            rplSection.addConfigItem(new ButtonItem(Text.translatable("key.lemclienthelper.deletePacks")).setClickEvent(() -> {
                configScreen.save();
                ResourcePreloaderMod.deletePacks();
                ResourcePreloaderMod.getPackList();
                addPacksToSub(sub);
            }));


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

            //Server Configs
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


            //Hud
            HudConfig clientGUI = HudMod.getConfig();
            ConfigSection clientGUISection = new ConfigSection(configScreen, Text.translatable("key.lemclienthelper.clientgui"));

            clientGUISection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.clientgui.enabled"), clientGUI.enabled, true).setSaveConsumer(val -> clientGUI.enabled = val));
            clientGUISection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.clientgui.alwaysshow"), clientGUI.alwaysEnabled, false).setSaveConsumer(val -> clientGUI.alwaysEnabled = val));

            FloatItem armorHudScale = (FloatItem) clientGUISection.addConfigItem(new FloatItem(Text.translatable("key.lemclienthelper.clientgui.armorscale"), clientGUI.armorHudScale, 1f));
            armorHudScale.setMinMax(1f,4f);
            armorHudScale.setSaveConsumer(val -> clientGUI.armorHudScale = val);
            armorHudScale.setToolTipWithNewLine("key.lemclienthelper.clientgui.armorscale.tooltip");

            IntegerItem armorHudXOffset = (IntegerItem) clientGUISection.addConfigItem(new IntegerItem(Text.translatable("key.lemclienthelper.clientgui.xOffset"), clientGUI.xOffset, 20));
            armorHudXOffset.setMinMax(0,100);
            armorHudXOffset.setSaveConsumer(val -> clientGUI.xOffset = val);
            armorHudXOffset.setToolTipWithNewLine("key.lemclienthelper.clientgui.xOffset.tooltip");

            //clientGUISection.addConfigItem(new ArmorHudPreviewItem(Text.translatable("key.lemclienthelper.clientgui.displaypreview"), clientGUI.enabled, false));


            //Small Inv
            ConfigSection smallInvSection = new ConfigSection(configScreen, Text.translatable("key.lemclienthelper.smallinv"));
            smallInvSection.addConfigItem(new BooleanItem(Text.translatable("key.lemclienthelper.smallinv.enabled"), SmallInvMod.getConfig().enabled, true).setSaveConsumer(val -> SmallInvMod.getConfig().enabled = val));


            //Synced Keybinds
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
                sub.addConfigItem(new ResourcepackDownloadItem(rpOption).setToolTip(Text.literal(rpOption.url)));
            });
        }
    }
}