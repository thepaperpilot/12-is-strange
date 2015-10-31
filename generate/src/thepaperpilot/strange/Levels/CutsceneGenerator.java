package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Json;

public class CutsceneGenerator {
    private final static Json json = new Json();

    public static void main(String[] args) {
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                // junkyard
                Cutscene.CutscenePrototype cutscenePrototype = new Cutscene.CutscenePrototype();

                String fileName = "junkyard.json";
                String output = json.prettyPrint(cutscenePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                System.out.println();
                Gdx.files.external(fileName).writeString(output, false);

                Gdx.app.exit();
            }
        }, new LwjglApplicationConfiguration());
    }
}
