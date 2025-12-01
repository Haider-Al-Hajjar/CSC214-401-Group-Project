package com.example.fantasysortinggame.gamemodes;

public class GameModeFactory {

    public static GameMode create(GameModeNames type) {
        return switch (type) {
            case Story -> new StoryMode();
            case Endless -> new EndlessMode();
            case Zen -> new ZenMode();
            case Timed -> new TimedMode();
            case Scored -> new ScoredMode();
        };
    }
}
