package nailheadbot.battle_new;

import java.util.ArrayList;

public class BattleCharacter implements Rolls{
    // character's unique id
    private final int charID;
    // discord user's unique id
    private final int userID;
    // character's name
    private final String name;
    // character's gender
    private final Gender gender;
    // character's maximum hp
    private final int maxHP;
    // character's maximum sp
    private final int maxSP;
    // character's default attack value
    private final int defaultATK;
    // character's default defense value
    private final int defaultDEF;
    // character's default speed value
    private final int defaultSPEED;
    // character's default accuracy value
    private final int defaultACC;
    // charcter's position on the battlefield
    private final int[] coordinates;
    // character's priority targets
    private final ArrayList<BattleCharacter> priorityTargets;
    // statuses active on the current character
    private final ArrayList<StatusInstance> activeStatuses;

    // character's current hp
    private int currHP;
    // character's current sp
    private int currSP;

    public BattleCharacter(String name, int charID, int userID, String gender, int HP, int SP, int ATK, int DEF, int SPEED, int ACC) {
        this.name = name;
        this.charID = charID;
        this.userID = userID;

        this.maxHP = HP;
        this.maxSP = SP;
        this.defaultATK = ATK;
        this.defaultDEF = DEF;
        this.defaultSPEED = SPEED;
        this.defaultACC = ACC;

        switch(gender.toLowerCase()){
            case "male":
                this.gender = Gender.MALE;
                break;
            case "female":
                this.gender = Gender.FEMALE;
                break;
            case "inanimate":
                this.gender = Gender.INANIMATE;
                break;
            default:
                this.gender = Gender.NONBINARY;
                break;
        }

        coordinates = new int[3];
        priorityTargets = new ArrayList<>();
        activeStatuses = new ArrayList<>();

        this.currHP = HP;
        this.currSP = SP;
    }

    /// This function handles INCOMING attacks ON the current character
    public int attacked(int attackPower, int spdRoll) {
        // damage taken
        int damageValue = -1;

        // if nat20 speed
        if (spdRoll == 20) {
            damageValue = (int) Math.round((float) attackPower * 1.5 / this.getCurrDEF());
        }
        // if speed roll is above current speed
        if (spdRoll > this.getCurrSPD()) {
            damageValue = attackPower / this.getCurrDEF();
        }
        // if speed roll is equal to current speed
        if (spdRoll == this.getCurrSPD()) {
            damageValue = Math.round((float) (attackPower) / 2 / this.getCurrDEF());
        }
        // apply damage
        damage(damageValue);

        // return the damage number
        return damageValue;
    }

    /// This function handles OUTGOING attacks FROM the current character
    public String attack(BattleCharacter target) {



        return "";
    }

    public String healHandle(BattleCharacter target, float healPower) {
        //calculate the heal amount based on heal power
        float healAmt = roll(6) * healPower;
        //heal the user the correct heal amount
        target.heal((int)healAmt);
        //send message to the channel
        return target + " has been healed " + (int)healAmt + " HP!";
    }

    public void addStatus(StatusInstance newStatus){
        for(StatusInstance status : activeStatuses){
            // If status is already on character and not stackable
            if(status.getStatus().equals(newStatus.getStatus()) && !status.isStackable()){
                // Remove old status
                activeStatuses.remove(status);
            }
        }
        // Add new status
        activeStatuses.add(newStatus);
    }


    // Override
    public String toString(){
        return name;
    }
    public int getUserID() {return userID;}
    public int getCharID() {return charID;}
    public Gender getGender() {return gender;}
    public int getCurrATK() {
        int currATK = this.defaultATK;
        for(StatusInstance status : activeStatuses){
            currATK += status.atkMod();
        }
        return Math.max(currATK, 1);
    }
    public int getCurrDEF() {
        int currDEF = this.defaultDEF;
        for(StatusInstance status : activeStatuses){
            currDEF += status.defMod();
        }
        return Math.max(currDEF, 1);
    }
    public int getCurrSPD() {
        int currSPD = this.defaultSPEED;
        for(StatusInstance status : activeStatuses){
            currSPD += status.spdMod();
        }
        return Math.max(currSPD, 0);
    }
    public int getCurrACC(){
        int currACC = this.defaultACC;
        for(StatusInstance status : activeStatuses){
            currACC += status.accMod();
        }
        return Math.max(currACC, 0);
    }
    public int getHP() {return currHP;}
    public int getSP() {return currSP;}
    public int getDefaultATK() {return defaultATK;}
    public int getDefaultDEF() {return defaultDEF;}
    public int getDefaultSPEED() {return defaultSPEED;}
    public int getDefaultACC() {return defaultACC;}
    public int getX() {return coordinates[0];}
    public int getY() {return coordinates[1];}
    public int getZ() {return coordinates[2];}

    /// This function calculates the chance of a move hitting its target
    public boolean moveHits(BattleCharacter target, int roll){
        // Check if roll exceeds threshold
        return roll > 20.0 / Math.exp(this.distanceTo(target) * target.getCurrSPD() / 50.0 / this.getCurrACC());
    }

    /// This function moves a character to a specified coordinate position
    public void moveTo(int X, int Y, int Z){
        coordinates[0] = X;
        coordinates[1] = Y;
        coordinates[2] = Z;
    }

    /// This function returns the distance from this character to a specified other character
    public double distanceTo(BattleCharacter target){
        return Math.sqrt(
                (coordinates[0]-target.getX()) * (coordinates[0]-target.getX())
                + (coordinates[1]-target.getY()) * (coordinates[1]-target.getY())
                + (coordinates[2]-target.getZ()) * (coordinates[2]-target.getZ())
        );
    }

    /// This function damages the current character for the specified amount, if it is above 0
    public void damage(int damage) {
        if(damage > 0){
            currHP -= damage;
            if (currHP < 0) {
                currHP = 0;
            }
        }
    }

    /// This function heals the current character for the specified amount
    public void heal(int heal) {
        currHP += heal;
        if (currHP > maxHP) {
            currHP = maxHP;
        }
    }

    /// This function takes away the specified SP point cost from the character
    public void spendSP(int points) {
        currSP -= points;
    }

    /// This function recovers the specified SP point cost to the character
    public void recoverSP(int points) {
        currSP += points;
        if (currSP > maxSP) {
            currSP = maxSP;
        }
    }

    /// This function checks if the character is downed and unable to act
    public boolean isDowned(){
        return currHP == 0;
    }
}
