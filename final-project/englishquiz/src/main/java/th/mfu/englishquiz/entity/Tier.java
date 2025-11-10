package th.mfu.englishquiz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Tier { // Renamed from Quest

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tierName; // Renamed from questName
    private String description;
    private String difficultyLevel;
    private int rewardPoints;
    private String learningFocus;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTierName() { // Renamed getter
        return tierName;
    }
    public void setTierName(String tierName) { // Renamed setter
        this.tierName = tierName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDifficultyLevel() {
        return difficultyLevel;
    }
    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    public int getRewardPoints() {
        return rewardPoints;
    }
    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
    public String getLearningFocus() {
        return learningFocus;
    }
    public void setLearningFocus(String learningFocus) {
        this.learningFocus = learningFocus;
    }
}