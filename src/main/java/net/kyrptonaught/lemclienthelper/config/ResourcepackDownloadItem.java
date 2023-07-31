package net.kyrptonaught.lemclienthelper.config;

import net.kyrptonaught.kyrptconfig.config.screen.items.ConfigItem;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.AllPacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ResourcepackDownloadItem extends ConfigItem<Object> {
    private final AllPacks.RPOption rpOption;

    public ResourcepackDownloadItem(AllPacks.RPOption option) {
        super(Text.literal(option.packname), null, null);
        this.rpOption = option;
    }

    @Override
    public int getContentSize() {
        return 2;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        super.render(context, x, y, mouseX, mouseY, delta);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        AllPacks.Progress progressListener = rpOption.progressListener;
        if (progressListener.title != null) {
            int titleX = MinecraftClient.getInstance().getWindow().getScaledWidth() - 90;

            if (progressListener.task == null) {
                context.drawCenteredTextWithShadow(textRenderer, progressListener.title, titleX, y + 10 - 4, 16777215);
            } else {
                Text task = (Text.literal("")).append(progressListener.task).append(" " + progressListener.progress + "%");

                context.drawCenteredTextWithShadow(textRenderer, progressListener.title, titleX, y + 2, 16777215);
                context.drawCenteredTextWithShadow(textRenderer, task, titleX, y + 11, 16777215);
            }
        }
    }
}