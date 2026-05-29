package nailheadbot;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.Random;

public class Nailhead {
    public static String header = "/home/container/resources/images/";
    public static Random random = new Random();

    public static String[] urls = {
            "Angry Nailhead.png",
            "Ascended.png",
            "bfdinail.png",
            "deez.png",
            "dont_ask.png",
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
            "machine.png",
            "meganailheadreal.png",
            "miencraft.png",
            "n.png",
            "nail.png",
            "Nailhead roblox.png",
            "nailhead sprite.png",
            "nailhead token.gif",
            "Nailhead_Caine.png",
            "Nailhead_prototypes_03.png",
            "nailhead2028transparent.png",
            "nailheadception.png",
            "nailheadcube.gif",
            "nailheadhorny.png",
            "nailheadhornyreal.png",
            "nailheadREAL.png",
            "nailheadtrophy.png",
            "nailheadvibing.png",
            "nailthumb.png",
            "ohno.png",
            "open your eyes to the truth of this world.gif",
            "preview.png",
            "smallhead.png",
            "smalln.png",
            "smallnail.png",
            "soundalike1.png",
            "soundalike2.png",
            "soundalike3.png",
            "soundalike4.png",
            "swagger nailhead.png",
            "swoleheadbig.png",
            "thechoice.png",
            "thepower.png",
            "threat.png",
            "trash.png",
            "this_guy_sucks.png",
            "unknown.png",
            "ur_fucked_bruh.png",
            "widehead.png",
    };

//    public static String[] announcementStrings =
//            {
//                  "Another day, another Nailhead! Here's today's Daily Nailhead!",
//                    "Feeling blue? Nailhead's here for you! Enjoy today's Daily Nailhead!",
//                    "Hot Nailheads in your area! Take a look at today's Daily Nailhead!",
//                    "The best part about NailheadBot? Why, it's the Daily Nailhead, of course!",
//                    "Ready or not, here comes Nailhead! Today's Daily Nailhead is here!",
//                    "Nailhead reporting for duty! Today's Daily Nailhead, presented front and center!"
//            };

    public static void nailhead(MessageChannel ch) {
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

//    public static void dailyNailhead(MessageChannel ch) {
//        int length = announcementStrings.length;
//        int index = random.nextInt(length);
//        ch.sendMessage(announcementStrings[index]).queue();
//        nailhead(ch);
//    }
}
