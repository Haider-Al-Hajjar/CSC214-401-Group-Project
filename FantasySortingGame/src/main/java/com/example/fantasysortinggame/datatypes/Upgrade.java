package com.example.fantasysortinggame.datatypes;

/**
 * Represents a purchasable upgrade in the game.
 */
public class Upgrade {
    private String ability;
    private double cost;
    private boolean isBought;
    private String name;
    public Upgrade(String ability, double cost, boolean isBought, String name) {
        this.ability = ability;
        this.cost = cost;
        this.isBought = isBought;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
