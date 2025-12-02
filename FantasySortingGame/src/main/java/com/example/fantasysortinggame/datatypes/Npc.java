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

    /**
     * Returns the NPC's display name.
     *
     * @return the NPC name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the NPC's display name.
     *
     * @param name the name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the path to the NPC's profile picture.
     * This is a string path only, not a File.
     *
     * @return the profile picture path
     */
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    /**
     * Sets the path to the NPC's profile picture.
     * This accepts a string path only.
     *
     * @param profilePicturePath the path to assign
     */
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
    /**
     * Returns the list of dialogues in which this NPC appears.
     *
     * @return list of dialogue entries
     */

    public ArrayList<Dialogue> getDialoguesAppearing() {
        return dialoguesAppearing;
    }
    /**
     * Replaces the list of dialogues in which this NPC appears.
     *
     * @param dialoguesAppearing the new dialogue list
     */

    public void setDialoguesAppearing(ArrayList<Dialogue> dialoguesAppearing) {
        this.dialoguesAppearing = dialoguesAppearing;
    }
}
