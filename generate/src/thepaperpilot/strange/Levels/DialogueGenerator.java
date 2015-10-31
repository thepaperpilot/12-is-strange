package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Json;

public class DialogueGenerator {
    private final static Json json = new Json();

    public static void main(String[] args) {
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                // final1
                Dialogue.DialoguePrototype dialoguePrototype = new Dialogue.DialoguePrototype();
                dialoguePrototype.dialogue = new Dialogue.LinePrototype[4];
                Dialogue.LinePrototype linePrototype = new Dialogue.LinePrototype();
                linePrototype.name = "Max";
                linePrototype.message = "Oh man! Wasn't that fun! A bit confusing, but satisfactory. Pretty short as well, though. And, what's up with all the Life is Strange puzzles, plot, characters, etc?";
                linePrototype.face = "maxcloseup";
                dialoguePrototype.dialogue[0] = linePrototype;
                linePrototype = new Dialogue.LinePrototype();
                linePrototype.name = "Max";
                linePrototype.message = "Well, I can't answer all those questions, but what I can tell you is that this is just the beginning! Now that the jam is over, this game is going to get some serious expansions. I'm talking 10x the content, no 100x the content! Yeah, maybe even, like, 200x. I guess it just depends on how you measure content. Hopefully not based on # of clicks or something stupid.";
                linePrototype.face = "maxcloseup";
                dialoguePrototype.dialogue[1] = linePrototype;
                linePrototype = new Dialogue.LinePrototype();
                linePrototype.name = "Max";
                linePrototype.message = "Whatever happens, I'm sure it'll be great. Bit of a shame this ending isn't that great, but, I mean, would you have preferred some rushed ending that didn't make sense? Like, less sense than this fourth wall breaking cop out? Yeah, exactly. The point is, keep watching this game, because it's going to get, like, a lot better. A lot a lot. For now, Thanks for Playing!!";
                linePrototype.face = "maxcloseup";
                dialoguePrototype.dialogue[2] = linePrototype;
                linePrototype = new Dialogue.LinePrototype();
                linePrototype.name = "Max";
                linePrototype.message = "Oh, and one last thing. There isn't actually currently a way to restart the game. No worries, that'll change, I've already worked out how to structure the scenes such that I can reset them all pretty easily. But for now, do me a solid and press refresh or re-open the application or whatever it is you kids do these days, and pretend it happened automatically. Cool? Cool.";
                linePrototype.face = "maxcloseup";
                dialoguePrototype.dialogue[3] = linePrototype;

                String fileName = "final1.json";
                String output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                System.out.println();
                Gdx.files.external(fileName).writeString(output, false);

                // door1
                dialoguePrototype = new Dialogue.DialoguePrototype();
                dialoguePrototype.dialogue = new Dialogue.LinePrototype[1];
                linePrototype = new Dialogue.LinePrototype();
                linePrototype.name = "Max";
                linePrototype.message = "Should I go outside the school?";
                linePrototype.face = "maxcloseup";
                linePrototype.options = new Dialogue.OptionPrototype[2];
                linePrototype.options[0] = new Dialogue.OptionPrototype();
                linePrototype.options[0].message = "Go outside";
                linePrototype.options[0].effects = new Effect.EffectPrototype[1];
                linePrototype.options[0].effects[0] = new Effect.EffectPrototype();
                linePrototype.options[0].effects[0].type = "CHANGE_SCREEN";
                linePrototype.options[0].effects[0].attributes.put("targetScene", "second");
                linePrototype.options[1] = new Dialogue.OptionPrototype();
                linePrototype.options[1].message = "Stay in school";
                linePrototype.options[1].effects = new Effect.EffectPrototype[1];
                linePrototype.options[1].effects[0] = new Effect.EffectPrototype();
                linePrototype.options[1].effects[0].type = "CHANGE_SCREEN";
                linePrototype.options[1].effects[0].attributes.put("targetScene", "third");
                dialoguePrototype.dialogue[0] = linePrototype;

                fileName = "door1.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                // balloon
                dialoguePrototype = new Dialogue.DialoguePrototype();
                dialoguePrototype.dialogue = new Dialogue.LinePrototype[1];
                linePrototype = new Dialogue.LinePrototype();
                linePrototype.name = "Max";
                linePrototype.message = "Do I have time to go party?";
                linePrototype.face = "maxcloseup";
                linePrototype.options = new Dialogue.OptionPrototype[2];
                linePrototype.options[0] = new Dialogue.OptionPrototype();
                linePrototype.options[0].message = "Get rekt";
                linePrototype.options[0].effects = new Effect.EffectPrototype[1];
                linePrototype.options[0].effects[0] = new Effect.EffectPrototype();
                linePrototype.options[0].effects[0].type = "CHANGE_SCREEN";
                linePrototype.options[0].effects[0].attributes.put("targetScene", "eighth");
                linePrototype.options[1] = new Dialogue.OptionPrototype();
                linePrototype.options[1].message = "Stay in school";
                linePrototype.options[1].effects = new Effect.EffectPrototype[1];
                linePrototype.options[1].effects[0] = new Effect.EffectPrototype();
                linePrototype.options[1].effects[0].type = "CHANGE_SCREEN";
                linePrototype.options[1].effects[0].attributes.put("targetScene", "ninth");
                dialoguePrototype.dialogue[0] = linePrototype;

                fileName = "balloon.json";
                output = json.prettyPrint(dialoguePrototype);
                System.out.println(Gdx.files.getExternalStoragePath() + fileName);
                System.out.println(output);
                Gdx.files.external(fileName).writeString(output, false);

                Gdx.app.exit();
            }
        }, new LwjglApplicationConfiguration());
    }
}
