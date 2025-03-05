package nailheadbot.battle_new;

public enum BattleStatus {
    DEFENDING(1, new int[]{0, 2, 0, 0}, 0, false, null),
    BURN(3, new int[]{0, 0, 0, 0}, -2, false, null)
    ;

    //Duration of the status
    private final int duration;
    //Health change over time
    private final int healthOverTime;
    //Whether the status can stack
    private final boolean stackable;
    //Stat modifiers
    private final int[] modifiers;
    //Special status properties
    private final StatusSpecial special;

    BattleStatus(int duration, int[] modifiers, int healthOverTime, boolean stackable, StatusSpecial special){
        this.duration = duration;
        this.modifiers = modifiers;
        this.healthOverTime = healthOverTime;
        this.special = special;
        this.stackable = stackable;
    }

    public int getAtkMod(){
        return modifiers[0];
    }

    public int getDefMod(){
        return modifiers[1];
    }

    public int getSpdMod(){
        return modifiers[2];
    }
    public int getAccMod() {
        return modifiers[3];
    }

    public boolean isStackable(){
        return stackable;
    }

    public void doTurn(BattleCharacter character){
        // If deals damage
        if(healthOverTime < 0){
            // Damage the character

        }
    }
}
