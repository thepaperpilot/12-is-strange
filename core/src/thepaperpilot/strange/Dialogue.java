package thepaperpilot.strange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

public class Dialogue extends Table {
    private final static Json json = new Json();
    private final ArrayList<Line> lines = new ArrayList<Line>();
    private Image face = new Image();
    private Label name = new Label("", Main.skin, "dialogue");
    private Label message = new Label("", Main.skin, "dialogue");
    private int line = 0;

    public Dialogue(DialoguePrototype dialoguePrototype) {
        super(Main.skin);
        setFillParent(true);

        for (LinePrototype prototype : dialoguePrototype.dialogue) {
            lines.add(new Line(prototype));
        }

        if (lines.size() == 0)
            return;

        face.setScale(6); // exact value TBD
        message.setAlignment(Align.topLeft);
        message.setWrap(true);
        bottom().left().add(face).bottom().left().expand();
        add(name).bottom().row();
        add(message).colspan(2).expandX().fillX().height(100).row();

        addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                next();
            }
        });

        next();
    }

    public static Dialogue readDialogue(String fileName) {
        return new Dialogue(json.fromJson(DialoguePrototype.class, Gdx.files.internal(fileName)));
    }

    // I'm going to leave this here as an example of how to write a dialogue and have it export to JSON.
    // The rest of the dialogues shouldn't be committed to the repo, just the exported JSON.
    // And absolutely make sure the code isn't running
    /*
    public static void main(String[] args) {
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                DialoguePrototype dialoguePrototype = new DialoguePrototype();
                LinePrototype linePrototype = new LinePrototype();
                linePrototype.name = "Max";
                linePrototype.message = "Alyssa! Do you know why that door over there is locked?";
                linePrototype.face = "maxStill.png";
                dialoguePrototype.dialogue.add(linePrototype);

                linePrototype = new LinePrototype();
                linePrototype.name = "Alyssa";
                linePrototype.message = "Yeah, actually. Jefferson asked me to lock up. But I tell you what, I'll give you the keys in return for some soap.";
                linePrototype.face = "AlyssaSoapPuzzle.png";
                dialoguePrototype.dialogue.add(linePrototype);

                linePrototype = new LinePrototype();
                linePrototype.name = "Max";
                linePrototype.message = "Soap? Isn't that a bit asinine?";
                linePrototype.face = "maxStill.png";
                dialoguePrototype.dialogue.add(linePrototype);

                linePrototype = new LinePrototype();
                linePrototype.name = "Alyssa";
                linePrototype.message = "Oh, absolutely. But a lot of tutorials are a lot worse. Just go do it.";
                linePrototype.face = "AlyssaSoapPuzzle.png";
                dialoguePrototype.dialogue.add(linePrototype);

                String fileName = "Alyssa1.json";
                String output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                Gdx.app.exit();
            }
        }, new LwjglApplicationConfiguration());
    }
    /**/

    private void next() {
        if (lines.size() <= line) {
            remove();
            return;
        }

        face.setDrawable(lines.get(line).face);
        name.setText(lines.get(line).name);
        message.setText(lines.get(line).message);

        line++;
    }

    static class Line {
        public String name;
        public String message;
        public Drawable face;

        public Line(LinePrototype prototype) {
            name = prototype.name;
            message = prototype.message;
            if (prototype.face != null) {
                face = new Image(Main.manager.get(prototype.face, Texture.class)).getDrawable();
            }
        }
    }

    public static class DialoguePrototype {
        public ArrayList<LinePrototype> dialogue = new ArrayList<LinePrototype>();
    }

    public static class LinePrototype {
        public String name;
        public String message;

        public String face;
    }
}
