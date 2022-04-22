package net.kyrptonaught.lebclienthelper.ResourcePreloader;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ProgressListener;

import java.util.ArrayList;
import java.util.List;

public class AllPacks {
    public List<RPOption> packs = new ArrayList<>();

    public static class RPOption {
        public String packname;
        public String url;
        public String hash;

        public transient Progress progressListener = new Progress();

    }

    public static class Progress implements ProgressListener {
        public Text title;
        public Text task;
        public int progress = 0;
        boolean completed = false;

        public void setTitle(Text title) {
            this.setTitleAndTask(title);
        }

        public void setTitleAndTask(Text title) {
            this.title = title;
            this.setTask(new TranslatableText("progress.working"));
        }

        @Override
        public void setTask(Text task) {
            this.task = task;
        }

        @Override
        public void progressStagePercentage(int percentage) {
            this.progress = percentage;
        }

        @Override
        public void setDone() {
            this.completed = true;
            this.title = new LiteralText("Download Complete");
            this.progressStagePercentage(100);
        }
    }
}