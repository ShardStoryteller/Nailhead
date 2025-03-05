package nailheadbot.battle_new;

public class StatusInstance {
    // The character who caused the status
    private final BattleCharacter user;
    // The status being used
    private final BattleStatus status;
    // The remaining turns for the status
    private int turnsActive;

    public StatusInstance(BattleCharacter user, BattleStatus status) {
        this.user = user;
        this.status = status;
        this.turnsActive = 0;
    }

    public BattleCharacter getUser() {
        return user;
    }
    public BattleStatus getStatus(){
        return status;
    }
    public int atkMod(){
        return status.getAtkMod();
    }
    public int defMod(){
        return status.getDefMod();
    }
    public int spdMod(){
        return status.getSpdMod();
    }
    public int accMod() {
        return status.getAccMod();
    }
    public boolean isStackable(){
        return status.isStackable();
    }
}
