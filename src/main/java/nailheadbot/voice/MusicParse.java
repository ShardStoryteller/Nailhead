package nailheadbot.voice;

import nailheadbot.EmbedHelper;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class MusicParse {
    private static final String path = "resources/audio/";

    private static final String[] urls = {
            "bad piggies theme.mp3",
            "bigshot.mp3",
            "bonetrousle.mp3",
            "breaking bad.mp3",
            "Delivery Dance.mp3",
            "duel of the fates.mp3",
            "gary come home.mp3",
            "it has to be this way.mp3",
            "its pizza time.mp3",
            "jetpack joyride.mp3",
            "kahoot.mp3",
            "Lancer.mp3",
            "live and learn.mp3",
            "megalovania.mp3",
            "mii channel.mp3",
            "nugget in a biscuit.mp3",
            "sad violin.mp3",
            "sans.mp3",
            "scheming weasel.mp3",
            "seinfeld.mp3",
            "slider.mp3",
            "speedrun.mp3",
            "stal.mp3",
            "Super Duper Jupiter.mp3",
            "terraria underground.mp3",
            "tomfoolery.mp3",
            "undefeatable.mp3",
            "vsauce theme.mp3"
    };

    private static final String[] trackNames = {
            "bad piggies",
            "big shot",
            "bonetrousle",
            "breaking bad",
            "delivery dance",
            "duel of the fates",
            "gary come home",
            "it has to be this way",
            "its pizza time",
            "jetpack joyride",
            "kahoot",
            "lancer",
            "live and learn",
            "megalovania",
            "mii channel",
            "nugget in a biscuit",
            "sad violin",
            "sans",
            "scheming weasel",
            "seinfeld",
            "slider",
            "speedrun",
            "stal",
            "super duper jupiter",
            "terraria underground",
            "tomfoolery",
            "undefeatable",
            "vsauce"
    };

    private static final String[] secretUrls = {
            "all star.mp3",
            "angry birds.mp3",
            "bad to the bone.mp3",
            "billie jean.mp3",
            "bury the light.mp3",
            "cha cha slide.mp3",
            "final countdown.mp3",
            "fnaf song.mp3",
            "funkytown.mp3",
            "gangnam style.mp3",
            "get lucky.mp3",
            "hey ya.mp3",
            "party rock.mp3",
            "rasputin.mp3",
            "revenge.mp3",
            "rickroll.mp3",
            "royalty free youtube music.mp3",
            "september.mp3",
            "sneaky snitch.mp3",
            "terraria day.mp3",
            "ymca.mp3",
            "zombie on your lawn.mp3"
    };

    private static final String[] secretTrackNames = {
            "all star",
            "angry birds",
            "bad to the bone",
            "billie jean",
            "bury the light",
            "cha cha slide",
            "final countdown",
            "fnaf",
            "funkytown",
            "gangnam style",
            "get lucky",
            "hey ya",
            "party rock",
            "rasputin",
            "revenge",
            "rickroll",
            "royalty free youtube",
            "september",
            "sneaky snitch",
            "terraria day",
            "ymca",
            "zombie on your lawn"
    };

    public static String getURL(String request) {
        for (int i = 0; i < urls.length; i++) {
            if (request.equals(trackNames[i])) {
                return path + urls[i];
            }
        }
        return null;
    }

    public static String getSecretURL(String request) {
        for (int i = 0; i < secretUrls.length; i++) {
            if (request.equals(secretTrackNames[i])) {
                return path + "low quality " + secretUrls[i];
            }
        }
        return null;
    }

    public static void trackList(MessageChannel channel) {
        EmbedHelper eb = new EmbedHelper(channel, "Track List", trackNames);
        eb.handleList();
    }

    public static String[] getTrackList() {
        return trackNames;
    }


}
