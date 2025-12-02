package com.example.fantasysortinggame.datatypes;

/**
 * Represents a purchasable upgrade in the game.
 */
public class Upgrade {
    private String ability;
    private double cost;
    private boolean isBought;
    private String name;

    public Upgrade(String name, double cost, boolean isBought, String ability) {
        this.name = name;
        this.ability = ability;
        this.cost = cost;
        this.isBought = isBought;
    }

    /**
     * Returns the display name of the upgrade.
     *
     * @return upgrade name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of the upgrade.
     *
     * @param name upgrade name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the cost of the upgrade.
     *
     * @return cost as a double
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the cost of the upgrade.
     *
     * @param cost cost to set
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Returns the ability granted by this upgrade.
     *
     * @return ability description
     */
    public String getAbility() {
        return ability;
    }

    /**
     * Sets the ability granted by this upgrade.
     *
     * @param ability ability description
     */
    public void setAbility(String ability) {
        this.ability = ability;
    }

    /**
     * Returns whether the upgrade has been purchased.
     *
     * @return true if bought
     */
    public boolean isBought() {
        return isBought;
    }

    /**
     * Marks the upgrade as bought or not.
     *
     * @param bought true to mark as bought
     */
    public void setBought(boolean bought) {
        isBought = bought;
    }
}
