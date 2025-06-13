package net.kyrptonaught.lemclienthelper.config;

import net.kyrptonaught.kyrptconfig.config.screen.items.ConfigItem;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.AllPacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ResourcepackDownloadItem extends ConfigItem<Object> {
    private final AllPacks.RPOption rpOption;

    public ResourcepackDownloadItem(AllPacks.RPOption option) {
        super(Component.literal(option.packname), null, null);
        this.rpOption = option;
    }

    @Override
    public int getContentSize() {
        return 2;
    }

    @Override
    public void render(GuiGraphics context, int x, int y, int mouseX, int mouseY, float delta) {
        super.render(context, x, y, mouseX, mouseY, delta);
        Font textRenderer = Minecraft.getInstance().font;

        if (rpOption.status != null) {
            int titleX = Minecraft.getInstance().getWindow().getGuiScaledWidth() - 90;

            if (rpOption.status2 == null) {
                context.drawCenteredString(textRenderer, rpOption.status, titleX, y + 10 - 4, 16777215);
            } else {
                //Text task = (Text.literal("")).append(progressListener.task).append(" " + progressListener.progress + "%");

                context.drawCenteredString(textRenderer, rpOption.status, titleX, y + 2, 16777215);
                context.drawCenteredString(textRenderer, rpOption.status2, titleX, y + 11, 16777215);
            }
        }
    }
}