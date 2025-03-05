package nailheadbot.battle_new;

public enum Gender {
    MALE("He", "Him", "His", "His", "He's"),
    FEMALE("She", "Her", "Her", "Hers", "She's"),
    NONBINARY("They", "Them", "Their", "Theirs", "They're"),
    INANIMATE("It", "It", "Its", "Its", "It's");

    private final String personal;
    private final String objective;
    private final String possessive;
    private final String possessiveObjective;
    private final String referential;

    Gender(String p1, String p2, String p3, String p4, String p5){
        this.personal = p1;
        this.objective = p2;
        this.possessive = p3;
        this.possessiveObjective = p4;
        this.referential = p5;
    }

    public String getPersonal() {
        return personal;
    }
    public String getObjective() {
        return objective;
    }
    public String getPossessive() {
        return possessive;
    }
    public String getPossessiveObjective() {
        return possessiveObjective;
    }
    public String getReferential() {
        return referential;
    }
}
