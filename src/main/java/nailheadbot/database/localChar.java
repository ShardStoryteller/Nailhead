package nailheadbot.database;

public class localChar {
    String name;
    String gender;
    int charID;
    int userID;
    int maxHP;
    int maxSP;
    int atk;
    int def;
    int spd;
    int acc;

    public localChar(String name, String gender, int charID, int userID,
                     int maxHP, int maxSP, int atk, int def, int spd, int acc) {
        this.name = name;
        this.gender = gender;
        this.charID = charID;
        this.userID = userID;
        this.maxHP = maxHP;
        this.maxSP = maxSP;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.acc = acc;
    }
}
