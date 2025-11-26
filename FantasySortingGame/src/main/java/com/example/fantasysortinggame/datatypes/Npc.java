package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

/**
 * Represents a non-player character in the game.
 */
public class Npc {

    private String name;
    private String profilePicturePath;  // <-- String only
    private ArrayList<Dialogue> dialoguesAppearing;

    public Npc() {
        this.name = "";
        this.profilePicturePath = "";
        this.dialoguesAppearing = new ArrayList<Dialogue>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // return String, not File
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    // accept String, not File
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public ArrayList<Dialogue> getDialoguesAppearing() {
        return dialoguesAppearing;
    }

    public void setDialoguesAppearing(ArrayList<Dialogue> dialoguesAppearing) {
        this.dialoguesAppearing = dialoguesAppearing;
    }
}
