package nailheadbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;

public class EmbedHelper {
    private final MessageChannel channel;
    private final String title;
    private String url;
    private String[] fields;
    private String[] values;

    //single image
    public EmbedHelper(MessageChannel channel, String title, String url) {
        this.channel = channel;
        this.title = title;
        this.url = url;
    }

    //stat or command lookup
    public EmbedHelper(MessageChannel channel, String title, String[] fields, String[] values) {
        this.channel = channel;
        this.title = title;
        this.fields = fields;
        this.values = values;
    }

    //biglist
    public EmbedHelper(MessageChannel channel, String title, String[] values) {
        this.channel = channel;
        this.title = title;
        this.values = values;
    }

    public void handleBasic(String attachmentName) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        File file = new File(url);
        eb.setImage("attachment://" + attachmentName);
        channel.sendMessage("").setEmbeds(eb.build()).addFiles(FileUpload.fromData(file, attachmentName)).queue();
    }

    public void handleValues() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);

        for (int i = 0; i < fields.length; i++) {
            eb.addField(fields[i], values[i], true);
        }

        channel.sendMessage(" ").setEmbeds(eb.build()).queue();
    }

    public void handleList() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);

        int pageLength = values.length / 10 + 1;
        eb.setFooter("Page 1/" + pageLength);

        for (int i = 0; i < 10; i++) {
            eb.addField(values[i], "", false);
        }

        channel.sendMessage(" ").setEmbeds(eb.build())
                .queue(
                        message -> {
                            message.addReaction(Emoji.fromUnicode("⏪")).queue();
                            message.addReaction(Emoji.fromUnicode("◀")).queue();
                            message.addReaction(Emoji.fromUnicode("▶")).queue();
                            message.addReaction(Emoji.fromUnicode("⏩")).queue();
                        }
                );
    }
}
