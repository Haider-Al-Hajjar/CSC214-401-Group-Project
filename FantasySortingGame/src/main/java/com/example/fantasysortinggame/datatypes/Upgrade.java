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
}
