package nailheadbot.database;

import nailheadbot.battle_new.BattleCharacter;

import java.util.ArrayList;

public class DatabaseHelper_Placeholder {
    //Character placeholder arraylist
    private static ArrayList<localChar> charList;

    public static void addChars(){
        charList = new ArrayList<>();
        charList.add(new localChar("ShadeScam", "Male", 1, 1, 80, 30, 11, 11, 11, 18));
        charList.add(new localChar("Gabby", "Female", 2, 1, 50, 38, 10, 9, 14, 12));
        charList.add(new localChar("Isabella", "Female", 3, 1, 70, 16, 16, 9, 17, 12));
    }

    public static boolean initiated(){
        return charList != null;
    }

    public static BattleCharacter getCharacter(String name){
        for(localChar character : charList){
            if(character.name.equals(name)) {
                return new BattleCharacter(character.name, character.charID, character.userID, character.gender,
                        character.maxHP, character.maxSP, character.atk, character.def, character.spd, character.acc);
            }
        }
        return null;
    }
}
