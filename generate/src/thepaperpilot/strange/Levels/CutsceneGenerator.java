package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Arrays;

public class CutsceneGenerator {
    private final static Json json = new Json();

    public static void main(String[] args) {
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                // junkyard
                Cutscene.CutscenePrototype cutscenePrototype = new Cutscene.CutscenePrototype();
                ArrayList<Cutscene.LinePrototype> lines = new ArrayList<Cutscene.LinePrototype>();
                Cutscene.LinePrototype line = new Cutscene.LinePrototype();
                line.time = 2;
                ArrayList<Effect.EffectPrototype> effects = new ArrayList<Effect.EffectPrototype>();
                Effect.EffectPrototype effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_APPEARANCE";
                effect.attributes.put("targetEntity", "max");
                effect.attributes.put("numFrames", "14");
                effect.attributes.put("texture", "maxWalk");
                effects.add(effect);
                effect = new Effect.EffectPrototype();
                effect.type = "MOVE_ENTITY";
                effect.attributes.put("targetEntity", "max");
                effect.attributes.put("newX", "140");
                effect.attributes.put("newY", "10");
                effect.attributes.put("time", "2");
                effects.add(effect);
                line.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                lines.add(line);
                line = new Cutscene.LinePrototype();
                line.time = .5f;
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_APPEARANCE";
                effect.attributes.put("targetEntity", "max");
                effect.attributes.put("numFrames", "28");
                effect.attributes.put("texture", "maxIdle"); // TODO leaning down animation
                effects.add(effect);
                line.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                lines.add(line);
                line = new Cutscene.LinePrototype();
                line.time = 4;
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "ADD_ENTITY";
                effect.attributes.put("targetEntity", "bottles");
                effects.add(effect);
                effect = new Effect.EffectPrototype();
                effect.type = "REMOVE_ITEM";
                effect.attributes.put("targetItem", "bottles");
                effects.add(effect);
                effect = new Effect.EffectPrototype();
                effect.type = "MOVE_ENTITY";
                effect.attributes.put("targetEntity", "max");
                effect.attributes.put("newX", "50");
                effect.attributes.put("newY", "10");
                effect.attributes.put("time", "4");
                effects.add(effect);
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_APPEARANCE";
                effect.attributes.put("targetEntity", "max");
                effect.attributes.put("texture", "maxWalk");
                effect.attributes.put("flip", "true");
                effect.attributes.put("numFrames", "14");
                effects.add(effect);
                line.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                lines.add(line);
                line = new Cutscene.LinePrototype();
                line.time = .7f;
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_APPEARANCE";
                effect.attributes.put("targetEntity", "max");
                effect.attributes.put("texture", "maxShooting");
                effect.attributes.put("flip", "false");
                effects.add(effect);
                line.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                lines.add(line);
                line = new Cutscene.LinePrototype();
                line.time = .7f;
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "PLAY_SOUND";
                effect.attributes.put("sound", "explosion");
                effects.add(effect);
                effect = new Effect.EffectPrototype();
                effect.type = "REMOVE_ITEM";
                effect.attributes.put("targetItem", "gun");
                effects.add(effect);
                effect = new Effect.EffectPrototype();
                effect.type = "MOVE_ENTITY";
                effect.attributes.put("targetEntity", "car");
                effect.attributes.put("newX", "90");
                effect.attributes.put("newY", "16");
                effect.attributes.put("time", "1.7");
                effects.add(effect);
                line.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                lines.add(line);
                line = new Cutscene.LinePrototype();
                line.time = 1f;
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_APPEARANCE";
                effect.attributes.put("targetEntity", "max");
                effect.attributes.put("texture", "maxIdle");
                effect.attributes.put("numFrames", "28");
                effects.add(effect);
                line.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                lines.add(line);
                line = new Cutscene.LinePrototype();
                line.time = 1f;
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "PLAY_SOUND";
                effect.attributes.put("sound", "error");
                effects.add(effect);
                effect = new Effect.EffectPrototype();
                effect.type = "REMOVE_ENTITY";
                effect.attributes.put("targetEntity", "bottles");
                effects.add(effect);
                line.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                lines.add(line);
                cutscenePrototype.scenes = Arrays.copyOf(lines.toArray(), lines.size(), Cutscene.LinePrototype[].class);

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
