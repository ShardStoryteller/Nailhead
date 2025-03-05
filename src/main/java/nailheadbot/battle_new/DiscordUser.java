package nailheadbot.battle_new;

public class DiscordUser {
    private int id;
    private String username;
    private BattleCharacter activeCharacter;

    public DiscordUser(int id, String username, BattleCharacter activeCharacter) {
        this.id = id;
        this.username = username;
        this.activeCharacter = activeCharacter;
    }

    public int getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public BattleCharacter getActiveCharacter() {
        return activeCharacter;
    }

    public void setActiveCharacter(BattleCharacter activeCharacter) {
        this.activeCharacter = activeCharacter;
    }
}
