package net.kyrptonaught.lemclienthelper.ResourcePreloader;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ProgressListener;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class AllPacks {
    public List<RPOption> packs = new ArrayList<>();

    public static class RPOption {
        public String packname;
        public String url;
        public String hash;
        public File downloadedFile;

        public HttpURLConnection MIXINISBEINGDUMB;

        public transient Progress progressListener = new Progress(this);

    }

    public static class Progress implements ProgressListener {
        public Text title;
        public Text task;
        public int progress = 0;
        public boolean completed = false;
        public final RPOption rpOption;

        public Progress(RPOption rpOption) {
            this.rpOption = rpOption;
        }

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

        public void skip(Text title) {
            done(title, null);
        }

        @Override
        public void setDone() {
            done(new TranslatableText("key.lemclienthelper.downloadcomplete"), this.task);
        }

        private void done(Text title, Text task) {
            this.completed = true;
            this.title = title;
            this.task = task;
            this.progressStagePercentage(100);
            ResourcePreloader.downloadComplete(rpOption);
        }
    }
}