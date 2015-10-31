package thepaperpilot.strange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import thepaperpilot.strange.Levels.Effect;
import thepaperpilot.strange.Levels.Level;

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

        for (LinePrototype prototype : dialoguePrototype.dialogue) {
            lines.add(new Line(prototype, level));
        }

        if (lines.size() == 0)
            return;

        face.setScale(6); // exact value TBD
        messageLabel.setAlignment(Align.topLeft);
        messageLabel.setWrap(true);
        message.top().left();
        message.setBackground(Main.skin.getDrawable("default-round"));
        bottom().left().add(face).bottom().left().expand();
        add(name).bottom().row();
        add(message).colspan(2).expandX().fillX().height(100).row();

        addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                next();
            }
        });

        addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });

        next();
    }

    public static Dialogue readDialogue(String fileName, Level level) {
        return new Dialogue(json.fromJson(DialoguePrototype.class, LinePrototype.class, Gdx.files.internal("dialogue/" + fileName + ".json")), level);
    }

    private void next() {
        if (lines.size() <= line) {
            remove();
            return;
        }

        Line nextLine = lines.get(line);
        face.setDrawable(nextLine.face);
        name.setText(nextLine.name);
        messageLabel.setText(nextLine.message);
        message.clearChildren();
        message.add(messageLabel).left().padBottom(5).row();
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
            super("> " + prototype.message, Main.skin, "large");

            effects = new Effect[prototype.effects.length];
            for (int i = 0; i < prototype.effects.length; i++) {
                effects[i] = new Effect(prototype.effects[i], level);
            }

            addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    for (Effect effect : effects) {
                        effect.run();
                    }
                }
            });
        }
    }

    static class DialoguePrototype {
        LinePrototype[] dialogue;
    }

    static class LinePrototype {
        String name;
        String message;
        String face;
        OptionPrototype[] options;
    }

    static class OptionPrototype {
        String message;
        Effect.EffectPrototype[] effects;
    }
}
