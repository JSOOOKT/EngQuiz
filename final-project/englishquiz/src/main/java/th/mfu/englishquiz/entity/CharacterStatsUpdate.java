package th.mfu.englishquiz.entity;

public class CharacterStatsUpdate {
    private int attack;
    private int defense;
    private int health;

    // --- Constructor (Optional but Recommended) ---
    public CharacterStatsUpdate() {
    }

    // --- Getters and Setters (Required for Spring to read the JSON) ---
    
    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    
}
