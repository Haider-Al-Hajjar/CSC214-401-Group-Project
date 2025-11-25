package com.example.fantasysortinggame.datatypes;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents a non-player character in the game.
 */
public class Npc {
    private String name;
    private File profilePicturePath;
    private ArrayList<Dialogue> dialoguesAppearing;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(File profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public ArrayList<Dialogue> getDialoguesAppearing() {
        return dialoguesAppearing;
    }

    public void setDialoguesAppearing(ArrayList<Dialogue> dialoguesAppearing) {
        this.dialoguesAppearing = dialoguesAppearing;
    }
}
