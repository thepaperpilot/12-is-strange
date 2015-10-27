package thepaperpilot.strange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

public class Dialogue {
    private final static Json json = new Json();
    public ArrayList<Line> dialogue = new ArrayList<Line>();

    public static Dialogue readDialogue(String fileName) {
        return json.fromJson(Dialogue.class, Gdx.files.internal(fileName));
    }

    // I'm going to leave this here as an example of how to write a dialogue and have it export to JSON.
    // The rest of the dialogues shouldn't be committed to the repo, just the exported JSON.
    // And absolutely make sure the code isn't running
    /*
    public static void main(String[] args) {
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                Dialogue dialogue1 = new Dialogue();
                Line line = new Line();
                line.name = "Max";
                line.message = "Alyssa! Do you know why that door over there is locked?";
                line.face = "maxStill.png";
                dialogue1.dialogue.add(line);

                line = new Line();
                line.name = "Alyssa";
                line.message = "Yeah, actually. Jefferson asked me to lock up. But I tell you what, I'll give you the keys in return for some soap.";
                dialogue1.dialogue.add(line);

                line = new Line();
                line.name = "Max";
                line.message = "Soap? Isn't that a bit asinine?";
                line.face = "maxStill.png";
                dialogue1.dialogue.add(line);

                line = new Line();
                line.name = "Alyssa";
                line.message = "Oh, absolutely. But a lot of tutorials are a lot worse. Just go do it.";
                dialogue1.dialogue.add(line);

                String fileName = "Alyssa1.json";
                String output = json.prettyPrint(dialogue1);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                Gdx.app.exit();
            }
        }, new LwjglApplicationConfiguration());
    }
    /**/

    static class Line {
        public String name;
        public String message;

        public String face;
    }
}
