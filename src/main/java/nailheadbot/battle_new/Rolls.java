package nailheadbot.battle_new;

import java.util.ArrayList;
import java.util.Random;

public interface Rolls {
    // random generator
    Random random = new Random();

    default int roll(int maxRoll){
        //random includes 0 but does not include max, so shift up by 1 to compensate
        return random.nextInt(maxRoll) + 1;
    }

    default <T> Object select(ArrayList<T> items){
        // return random item from within the array
        return items.get(random.nextInt(items.size()));
    }
}
