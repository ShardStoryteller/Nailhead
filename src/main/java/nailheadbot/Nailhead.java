package nailheadbot;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Random;

public class Nailhead {
    private static final Logger logger = LoggerFactory.getLogger(Nailhead.class);
    public static String header = "/home/container/resources/images/";

    public static String[] urls = {
            "Angry Nailhead.png",
            "Ascended.png",
            "everyonedies.png",
            "Expand Nailhead.png",
            "Fancy Nailhead.png",
            "flosshead.png",
            "flowchart.png",
            "gunhead.png",
            "gunhold nailhead.png",
            "Hiking.png",
            "hmm.png",
            "hmmtodayiwill.png",
            "holyshit.png",
            "hostnail.png",
            "jojo nailhead.png",
            "meganailheadreal.png",
            "miencraft.png",
            "n.png",
            "nail.png",
            "Nailhead roblox.png",
            "nailhead sprite.png",
            "Nailhead_prototypes_03.png",
            "nailheadception.png",
            "nailheadhorny.png",
            "nailheadhornyreal.png",
            "nailheadREAL.png",
            "nailheadvibing.png",
            "ohno.png",
            "open your eyes to the truth of this world.gif",
            "preview.png",
            "smallhead.png",
            "smalln.png",
            "smallnail.png",
            "swagger nailhead.png",
            "swoleheadbig.png",
            "thepower.png",
            "trash.png",
            "unknown.png",
            "ur_fucked_bruh.png",
            "widehead.png",
    };

    public static void nailhead(MessageReceivedEvent event) {
        MessageChannel ch = event.getChannel();
        String guildID = event.getGuild().getId();
        Random random = new Random();
        int length = urls.length + 1;
        int index = random.nextInt(length);
        if (index == urls.length) {
            ch.sendMessage("https://www.youtube.com/watch?v=nhkaxtRJf-g").queue();
        } else {
            String imageURL = header + urls[index];
            logger.info(imageURL);
            EmbedHelper eb = new EmbedHelper(ch, "Nailhead", imageURL);
            eb.handleBasic("nailhead.png");
        }
    }
}
