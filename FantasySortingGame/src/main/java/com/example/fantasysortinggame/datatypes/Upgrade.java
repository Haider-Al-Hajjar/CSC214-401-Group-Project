package com.example.fantasysortinggame.datatypes;

public class Upgrade {
    String ability;
    int cost;
    boolean isBought;

    public Upgrade(String ability, int cost, boolean isBought) {
        this.ability = ability;
        this.cost = cost;
        this.isBought = isBought;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}
