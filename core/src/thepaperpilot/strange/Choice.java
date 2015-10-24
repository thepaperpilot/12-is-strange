package thepaperpilot.strange;

import thepaperpilot.strange.Screens.GameScreen;

public class Choice {
    public String name;
    public GameScreen nextScene;

    public Choice(String name, GameScreen nextScene) {
        this.name = name;
        this.nextScene = nextScene;
    }
}
