package nailheadbot;

import java.util.Random;

public class magic8Ball {
    public static String[] responses = {
            "yuh-huh",
            "correctamundo!",
            "so true bestie!!!",
            "survey says...*EXTREMELY LOUD CORRECT DING*",
            "YES! YES! YES!",
            "nuh-uh",
            "nah not happening",
            "so false worstie!!!",
            "survey says...*EXTREMELY LOUD INCORRECT BUZZER*",
            "nope.",
            "who tf knows bruh",
            "ask me that shit again some other time",
            "i got no clue but did you know that the mitochondria is the powerhouse of the cell"
    };

    public static final Random rand = new Random();

    public static String run8ball() {
        int index = rand.nextInt(responses.length);
        return responses[index];
    }
}
