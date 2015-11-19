package thepaperpilot.strange.Levels;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import thepaperpilot.strange.Main;

import java.util.HashMap;
import java.util.Map;

public class Effect {
    public Map<String, String> attributes;
    public Type type;
    public Level level;

    public Effect(EffectPrototype prototype, Level level) {
        this.level = level;
        type = Type.valueOf(prototype.type);
        attributes = prototype.attributes;
    }

    public void run() {
        switch (type) {
            case REMOVE_ENTITY:
                // find an entity and make it invisible
                // its still there, but is not visible to the player and can't be interacted with
                // use ADD_ENTITY to make it visible again, and interactable
                Scene scene = level.scenes.get(attributes.get("targetScene"));
                Entity entity = scene.entities.get(attributes.get("targetEntity"));
                entity.visible = false;
                entity.remove();
                break;
            case ADD_ENTITY:
                // find an entity and make it visible
                scene = level.scenes.get(attributes.get("targetScene"));
                entity = scene.entities.get(attributes.get("targetEntity"));
                entity.visible = true;
                scene.stage.addActor(entity);
                break;
            case MOVE_ENTITY:
                // find an entity and move it, optionally over time
                scene = level.scenes.get(attributes.get("targetScene"));
                entity = scene.entities.get(attributes.get("targetEntity"));
                entity.addAction(Actions.moveTo(Float.valueOf(attributes.get("newX")), Float.valueOf(attributes.get("newY")), Float.valueOf(attributes.get("time"))));
                break;
            case REMOVE_ITEM:
                // remove an item from the player's inventory
                Item item = level.items.get(attributes.get("targetItem"));
                level.inventory.remove(item);
                level.selected.remove(item);
                level.updateInventory();
                break;
            case ADD_ITEM:
                // add an item to the player's inventory
                Main.manager.get("audio/pickup.wav", Sound.class).play();
                item = level.items.get(attributes.get("targetItem"));
                level.inventory.add(item);
                level.updateInventory();
                break;
            case REMOVE_BARRIER:
                // remove a barrier that blocks player movement
                scene = level.scenes.get(attributes.get("targetScene"));
                scene.obstacles.remove(attributes.get("targetObstacle"));
                break;
            // TODO ADD_BARRIER
            case CHANGE_APPEARANCE:
                // find an entity and change its appearance
                scene = level.scenes.get(attributes.get("targetScene"));
                entity = scene.entities.get(attributes.get("targetEntity"));
                // because the appearance can be a still or an animation or something else,
                // check for any possible attributes and pass them to the entity
                // a bit hard-code-y for me, but meh
                if (attributes.get("type") != null)
                    entity.type = Entity.Type.valueOf(attributes.get("type"));
                if (attributes.get("texture") != null)
                    entity.attributes.put("texture", attributes.get("texture"));
                if (attributes.get("numFrames") != null)
                    entity.attributes.put("numFrames", attributes.get("numFrames"));
                if (attributes.get("speed") != null)
                    entity.attributes.put("speed", attributes.get("speed"));
                if (attributes.get("time") != null)
                    entity.attributes.put("time", attributes.get("time"));
                if (attributes.get("loop") != null)
                    entity.attributes.put("loop", attributes.get("loop"));
                if (attributes.get("flip") != null)
                    entity.attributes.put("flip", attributes.get("flip"));
                entity.updateAppearance();
                break;
            case SAY:
                // create a label below the player that fades out/up
                scene = level.scenes.get(attributes.get("targetScene"));
                scene.say(attributes.get("message"));
                break;
            case DIALOGUE:
                // start a dialogue sequence
                scene = level.scenes.get(attributes.get("targetScene"));
                scene.ui.addActor(Dialogue.readDialogue(attributes.get("dialogue"), level));
                break;
            case PLAY_SOUND:
                // play a sound
                Main.manager.get("audio/" + attributes.get("sound") + ".wav", Sound.class).play();
                break;
            case CUTSCENE:
                // start a cutscene sequence
                Main.changeScreen(Cutscene.readCutscene(attributes.get("cutscene"), level.scenes.get(attributes.get("targetScene")), level));
                break;
            case CHANGE_SCREEN:
                // move to another area
                Main.manager.get("audio/error.wav", Sound.class).play();
                scene = level.scenes.get(attributes.get("targetScene"));
                Main.changeScreen(scene);
                break;
            case CHANGE_LEVEL:
                // move to another level
                Level level = Level.readLevel("levels/" + attributes.get("targetLevel") + ".json");
                Main.changeScreen(level.firstScene);
                break;
            case RUN_EFFECT:
                // just a passthrough, basically
                EffectPrototype prototype = new EffectPrototype();
                prototype.type = attributes.get("effect");
                // prevent infinite recursion
                if(prototype.type.equals("RUN_EFFECT"))
                    return; // nice try!
                prototype.attributes = attributes;
                break;
        }
    }

    public enum Type {
        REMOVE_ENTITY,
        ADD_ENTITY,
        MOVE_ENTITY,
        REMOVE_ITEM,
        ADD_ITEM,
        REMOVE_BARRIER,
        CHANGE_APPEARANCE,
        SAY,
        DIALOGUE,
        PLAY_SOUND,
        CUTSCENE,
        CHANGE_SCREEN,
        CHANGE_LEVEL,
        RUN_EFFECT // used in cutscenes
    }

    public static class EffectPrototype {
        public String type;
        public Map<String, String> attributes = new HashMap<String, String>();
    }
}
