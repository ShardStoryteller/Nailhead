package nailheadbot.battle_new;

public enum BattleEnemy {
    DARK_AB1("Alloy A", "Inanimate", 50, 5, 10, 15, 15, 10),
    DARK_AB2("Alloy B", "Inanimate", 75, 5, 10, 15, 15, 10),
    DARK_CHRIS_1("Dark Chris", "Male", 400, 12, 12, 12, 12, 12),
    DARK_GRACE("Dark Grace", "Female", 500, 20, 12, 8, 16, 12),
    SCARJIA("Scarjia", "Female", 800, 20, 20, 12, 12, 10),
    DARK_MECCHRIS_V1("Alloy M", "Inanimate", 1000, 16, 16, 16, 4, 16),
    DARK_KORDOCK("Alloy K", "Inanimate", 750, 12, 20, 18, 8, 16),
    GABBY_CLONE("Warrior of Light", "Female", 300, 20, 16, 8, 12, 12),
    TWILIGHT("Twilight", "Female", 800, 20, 20, 16, 16, 16),
    DARK_PROGRAM("Alloy P", "Inanimate", 400, 8, 16, 16, 4, 14),
    DARK_QUERY("Alloy Q", "Inanimate", 400, 8, 12, 12, 12, 14),
    DARK_TD("Alloy T", "Inanimate", 300, 0, 0, 20, 12, 20),
    AB1_SPAWNER("", "Inanimate", 600, 20, 0, 20, 0, 0),
    AB2_SPAWNER("", "Inanimate", 600, 20, 0, 20, 0, 0),
    BIGBOT_SPAWNER("", "Inanimate", 800, 20, 0, 20, 0, 0),
    DARK_CHRIS_BOT("Alloy S", "Male", 400, 12, 12, 12, 12, 12),
    DARK_ZACHARY("Dark Zachary", "Male", 700, 20, 16, 8, 12, 16),
    JANE("Jane Shard", "Female", 700, 20, 10, 10, 10, 20),
    NYX("Nyx", "Nonbinary", 800, 20, 20, 8, 16, 16),
    DARK_MECCHRIS_V2("Alloy M", "Inanimate", 1200, 20, 20, 20, 4, 16),
    DARK_CHRIS_2("Dark Chris", "Male", 500, 20, 20, 16, 16, 16),
    JESSICA("Dark Dutchess Jessica", "Female", 1600, 30, 30, 16, 16, 16),
    NAILHEAD("Nailhead", "Male", 2000,  40, 20, 20, 16, 20),
    BUFF_NAILHEAD("Nailhead", "Male", 2000, 40, 40, 40, 12, 20);

    private static int ID = -1;

    private final String name;
    private final String gender;
    private final int HP;
    private final int SP;
    private final int ATK;
    private final int DEF;
    private final int SPD;
    private final int ACC;

    BattleEnemy(String name, String gender, int HP, int SP, int ATK, int DEF, int SPD, int ACC){
        this.name = name;
        this.gender = gender;
        this.HP = HP;
        this.SP = SP;
        this.ATK = ATK;
        this.DEF = DEF;
        this.SPD = SPD;
        this.ACC = ACC;
    }

    public BattleCharacter getInstance(){
        BattleCharacter returnChar = new BattleCharacter(name, ID, -1, gender, HP, SP, ATK, DEF, SPD, ACC);
        ID -= 1;
        if(ID < -256){
            ID = -1;
        }
        return returnChar;
    }
}
