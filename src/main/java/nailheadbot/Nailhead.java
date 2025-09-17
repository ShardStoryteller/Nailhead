package nailheadbot;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class Nailhead {
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
            "threat.png",
            "trash.png",
            "unknown.png",
            "ur_fucked_bruh.png",
            "widehead.png",
    };

    public static void nailhead(MessageReceivedEvent event) {
        MessageChannel ch = event.getChannel();
        Random random = new Random();
        int length = urls.length + 2;
        int index = random.nextInt(length);
        if (index == urls.length) {
            ch.sendMessage("https://www.youtube.com/watch?v=nhkaxtRJf-g").queue();
        } else if (index == urls.length + 1){
            ch.sendMessage("https://wplace.live/?lat=82.40080811524403&lng=-161.64483431572265&zoom=15").queue();
        } else {
            String imageURL = header + urls[index];
            EmbedHelper eb = new EmbedHelper(ch, "Nailhead", imageURL);
            eb.handleBasic("nailhead.png");
        }
    }
}
