package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import thepaperpilot.strange.Main;

import java.util.ArrayList;

public class Dialogue extends Table {
    private final static Json json = new Json();
    private final ArrayList<Line> lines = new ArrayList<Line>();
    private Image face = new Image();
    private Label name = new Label("", Main.skin, "dialogue");
    private Table message = new Table(Main.skin);
    private Label messageLabel = new Label("", Main.skin, "large");
    private int line = 0;

    public Dialogue(DialoguePrototype dialoguePrototype, Level level) {
        super(Main.skin);
        setFillParent(true);
        setTouchable(Touchable.enabled);

        // create each part of the dialogue
        for (LinePrototype prototype : dialoguePrototype.dialogue) {
            lines.add(new Line(prototype, level));
        }

        // if the dialogue is empty, let's go ahead and not do anything
        if (lines.size() == 0)
            return;

        // create the dialogue ui
        face.setScale(6); // exact value TBD
        messageLabel.setAlignment(Align.topLeft);
        messageLabel.setWrap(true);
        message.top().left();
        message.setBackground(Main.skin.getDrawable("default-round"));
        bottom().left().add(face).bottom().left().expand();
        add(name).bottom().row();
        add(message).colspan(2).expandX().fillX().height(100).row();

        // left click to advance the dialogue
        addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                next();
            }
        });

        // right click to undo the conversation
        // TODO make this enable/disable-able
        addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });

        // start the dialogue
        next();
    }

    public static Dialogue readDialogue(String fileName, Level level) {
        // load the cutscene from a file using voodoo magic (reflections)
        // yay for libGDX for making this so easy!
        // Also, it uses prototypes so the file can have only the necessary variables
        // without any boilerplating
        return new Dialogue(json.fromJson(DialoguePrototype.class, Gdx.files.internal("dialogue/" + fileName + ".json")), level);
    }

    private void next() {
        // check if we're done with the dialogue
        if (lines.size() <= line) {
            remove();
            return;
        }

        // update the dialogue ui for the next part of the dialogue
        Line nextLine = lines.get(line);
        face.setDrawable(nextLine.face);
        name.setText(nextLine.name);
        messageLabel.setText(nextLine.message);
        message.clearChildren();
        message.add(messageLabel).expandX().fillX().left().padBottom(5).row();
        if (nextLine.options != null) {
            message.row();
            for (int i = 0; i < nextLine.options.length; i++) {
                message.add(nextLine.options[i]).left().padLeft(10).row();
            }
        }

        line++;
    }

    static class Line {
        String name;
        String message;
        Drawable face;
        Option[] options;

        Line(LinePrototype prototype, Level level) {
            name = prototype.name;
            message = prototype.message;

            // create the face for the talker
            if (prototype.face != null) {
                face = new Image(Main.entities.findRegion(prototype.face)).getDrawable();
            }
            if (prototype.options != null) {
                options = new Option[prototype.options.length];
                for (int i = 0; i < prototype.options.length; i++) {
                    options[i] = new Option(prototype.options[i], level);
                }
            }
        }
    }

    static class Option extends Label {
        Effect[] effects;

        Option(OptionPrototype prototype, Level level) {
            // indicate this is a button by preceding it with a ">"
            super("> " + prototype.message, Main.skin, "large");

            // create the actions to occur when the option is selected
            effects = new Effect[prototype.effects.length];
            for (int i = 0; i < prototype.effects.length; i++) {
                effects[i] = new Effect(prototype.effects[i], level);
            }

            // do the actions when this button is clicked
            addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    Main.manager.get("audio/select.wav", Sound.class).play();
                    for (Effect effect : effects) {
                        effect.run();
                    }
                }
            });
        }
    }

    public static class DialoguePrototype {
        LinePrototype[] dialogue;
    }

    public static class LinePrototype {
        String name;
        String message;
        String face;
        OptionPrototype[] options;
    }

    public static class OptionPrototype {
        String message;
        Effect.EffectPrototype[] effects;
    }
}
