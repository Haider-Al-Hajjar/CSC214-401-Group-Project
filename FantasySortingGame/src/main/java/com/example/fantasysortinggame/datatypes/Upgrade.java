package com.example.fantasysortinggame.datatypes;

/**
 * Represents a purchasable upgrade in the game.
 */
public class Upgrade {
    private String ability;
    private double cost;
    private boolean isBought;

    public Upgrade(String ability, double cost, boolean isBought) {
        this.ability = ability;
        this.cost = cost;
        this.isBought = isBought;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}
