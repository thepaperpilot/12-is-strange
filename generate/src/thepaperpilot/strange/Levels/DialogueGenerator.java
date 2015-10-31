package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Arrays;

public class DialogueGenerator {
    private final static Json json = new Json();

    public static void main(String[] args) {
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                Dialogue.DialoguePrototype dialoguePrototype;
                ArrayList<Dialogue.LinePrototype> lines;
                Dialogue.LinePrototype line;
                ArrayList<Dialogue.OptionPrototype> options;
                Dialogue.OptionPrototype option;
                ArrayList<Effect.EffectPrototype> effects;
                Effect.EffectPrototype effect;
                String fileName;
                String output;

                // alyssa1
                dialoguePrototype = new Dialogue.DialoguePrototype();
                lines = new ArrayList<Dialogue.LinePrototype>();
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Alyssa! Do you know why that door over there is locked?";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Alyssa";
                line.message = "Yeah, actually. Jefferson asked me to lock up. But I tell you what, I'll give you the keys in return for some soap.";
                line.face = "AlyssaSoapPuzzle";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Soap? Isn't that a bit asinine?";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Alyssa";
                line.message = "Oh, absolutely. But a lot of tutorials are a lot worse. Just go do it.";
                line.face = "AlyssaSoapPuzzle";
                lines.add(line);
                dialoguePrototype.dialogue = Arrays.copyOf(lines.toArray(), lines.size(), Dialogue.LinePrototype[].class);

                fileName = "alyssa1.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                // balloon
                dialoguePrototype = new Dialogue.DialoguePrototype();
                lines = new ArrayList<Dialogue.LinePrototype>();
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Do I have time to go party?";
                line.face = "maxcloseup";
                options = new ArrayList<Dialogue.OptionPrototype>();
                option = new Dialogue.OptionPrototype();
                option.message = "Get rekt";
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_SCREEN";
                effect.attributes.put("targetScene", "eighth");
                effects.add(effect);
                option.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                options.add(option);
                option = new Dialogue.OptionPrototype();
                option.message = "Stay in school";
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_SCREEN";
                effect.attributes.put("targetScene", "ninth");
                effects.add(effect);
                option.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                options.add(option);
                line.options = Arrays.copyOf(options.toArray(), options.size(), Dialogue.OptionPrototype[].class);
                lines.add(line);
                dialoguePrototype.dialogue = Arrays.copyOf(lines.toArray(), lines.size(), Dialogue.LinePrototype[].class);

                fileName = "balloon.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                // detective1
                dialoguePrototype = new Dialogue.DialoguePrototype();
                lines = new ArrayList<Dialogue.LinePrototype>();
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Hello?? Who are you?";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "???";
                line.message = "No one important. What is important, is this cookie I have here.";
                line.face = "detective";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "A cookie? Surely you're kidding.";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "???";
                line.message = "I am not. You'll understand in time. But, before you get this, I'm going to need something from you. Return back here when you have a phone, photo, and files.";
                line.face = "detective";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "That's... oddly specific. I suppose there's no point in asking you what you need them for?";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "???";
                line.message = "I'm afraid not. Now, hurry!";
                line.face = "detective";
                lines.add(line);
                dialoguePrototype.dialogue = Arrays.copyOf(lines.toArray(), lines.size(), Dialogue.LinePrototype[].class);

                fileName = "detective1.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                // door1
                dialoguePrototype = new Dialogue.DialoguePrototype();
                lines = new ArrayList<Dialogue.LinePrototype>();
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Should I go outside the school?";
                line.face = "maxcloseup";
                options = new ArrayList<Dialogue.OptionPrototype>();
                option = new Dialogue.OptionPrototype();
                option.message = "Go outside";
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_SCREEN";
                effect.attributes.put("targetScene", "second");
                effects.add(effect);
                option.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                options.add(option);
                option = new Dialogue.OptionPrototype();
                option.message = "Stay in school";
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_SCREEN";
                effect.attributes.put("targetScene", "third");
                effects.add(effect);
                option.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                options.add(option);
                line.options = Arrays.copyOf(options.toArray(), options.size(), Dialogue.OptionPrototype[].class);
                lines.add(line);
                dialoguePrototype.dialogue = Arrays.copyOf(lines.toArray(), lines.size(), Dialogue.LinePrototype[].class);

                fileName = "door1.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                // door2
                dialoguePrototype = new Dialogue.DialoguePrototype();
                lines = new ArrayList<Dialogue.LinePrototype>();
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Should I go the halls or the restroom?";
                line.face = "maxcloseup";
                options = new ArrayList<Dialogue.OptionPrototype>();
                option = new Dialogue.OptionPrototype();
                option.message = "I have to go REALLY BADLY";
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_SCREEN";
                effect.attributes.put("targetScene", "eleventh");
                effects.add(effect);
                option.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                options.add(option);
                option = new Dialogue.OptionPrototype();
                option.message = "I can hold it";
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_SCREEN";
                effect.attributes.put("targetScene", "twelfth");
                effects.add(effect);
                option.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                options.add(option);
                line.options = Arrays.copyOf(options.toArray(), options.size(), Dialogue.OptionPrototype[].class);
                lines.add(line);
                dialoguePrototype.dialogue = Arrays.copyOf(lines.toArray(), lines.size(), Dialogue.LinePrototype[].class);

                fileName = "door2.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                // final1
                dialoguePrototype = new Dialogue.DialoguePrototype();
                lines = new ArrayList<Dialogue.LinePrototype>();
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Oh man! Wasn't that fun! A bit confusing, but satisfactory. Pretty short as well, though. And, what's up with all the Life is Strange puzzles, plot, characters, etc?";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Well, I can't answer all those questions, but what I can tell you is that this is just the beginning! Now that the jam is over, this game is going to get some serious expansions. I'm talking 10x the content, no 100x the content! Yeah, maybe even, like, 200x. I guess it just depends on how you measure content. Hopefully not based on # of clicks or something stupid.";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Whatever happens, I'm sure it'll be great. Bit of a shame this ending isn't that great, but, I mean, would you have preferred some rushed ending that didn't make sense? Like, less sense than this fourth wall breaking cop out? Yeah, exactly. The point is, keep watching this game, because it's going to get, like, a lot better. A lot a lot. For now, Thanks for Playing!!";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "I guess that just leaves one last option...";
                line.face = "maxcloseup";
                options = new ArrayList<Dialogue.OptionPrototype>();
                option = new Dialogue.OptionPrototype();
                option.message = "Restart Game";
                effects = new ArrayList<Effect.EffectPrototype>();
                effect = new Effect.EffectPrototype();
                effect.type = "CHANGE_LEVEL";
                effect.attributes.put("targetLevel", "first");
                effects.add(effect);
                option.effects = Arrays.copyOf(effects.toArray(), effects.size(), Effect.EffectPrototype[].class);
                options.add(option);
                line.options = Arrays.copyOf(options.toArray(), options.size(), Dialogue.OptionPrototype[].class);
                lines.add(line);
                dialoguePrototype.dialogue = Arrays.copyOf(lines.toArray(), lines.size(), Dialogue.LinePrototype[].class);

                fileName = "final1.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                System.out.println();
                Gdx.files.external(fileName).writeString(output, false);

                // Warren1
                dialoguePrototype = new Dialogue.DialoguePrototype();
                lines = new ArrayList<Dialogue.LinePrototype>();
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Warren, what are you doing here?";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Warren";
                line.message = "I was looking for you. Remember that USB I let you borrow? I need it back.";
                line.face = "warrenPuzzle";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Max";
                line.message = "Well, fair enough. It's yours, after all. I think I left it in the junkyard.";
                line.face = "maxcloseup";
                lines.add(line);
                line = new Dialogue.LinePrototype();
                line.name = "Warren";
                line.message = "Awesome. After you get it back, there's some guy looking for you. Something about a code. He looked kinda creepy. Anyways, I'll show you where he is once you get me my flash drive back.";
                line.face = "warrenPuzzle";
                lines.add(line);
                dialoguePrototype.dialogue = Arrays.copyOf(lines.toArray(), lines.size(), Dialogue.LinePrototype[].class);

                fileName = "warren1.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                System.out.println();
                Gdx.files.external(fileName).writeString(output, false);

                Gdx.app.exit();
            }
        }, new LwjglApplicationConfiguration());
    }
}
