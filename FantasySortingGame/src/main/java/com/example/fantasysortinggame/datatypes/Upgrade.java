package com.example.fantasysortinggame.datatypes;

public class Upgrade {
    String ability;
    double cost;
    boolean isBought;

    public Upgrade(String ability, double cost, boolean isBought) {
        this.ability = ability;
        this.cost = cost;
        this.isBought = isBought;
    }
}
