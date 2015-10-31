package thepaperpilot.strange.Levels;

import com.badlogic.gdx.audio.Sound;
import thepaperpilot.strange.Dialogue;
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
                Scene scene = level.scenes.get(attributes.get("targetScene"));
                Entity entity = scene.entities.get(attributes.get("targetEntity"));
                entity.remove();
                break;
            case ADD_ENTITY:
                scene = level.scenes.get(attributes.get("targetScene"));
                entity = scene.entities.get(attributes.get("targetEntity"));
                scene.stage.addActor(entity);
                break;
            case REMOVE_ITEM:
                Item item = level.items.get(attributes.get("targetItem"));
                level.inventory.remove(item);
                level.selected.remove(item);
                level.updateInventory();
                break;
            case ADD_ITEM:
                Main.manager.get("audio/pickup.wav", Sound.class).play();
                item = level.items.get(attributes.get("targetItem"));
                level.inventory.add(item);
                level.updateInventory();
                break;
            case REMOVE_BARRIER:
                scene = level.scenes.get(attributes.get("targetScene"));
                scene.obstacles.remove(attributes.get("targetObstacle"));
            case CHANGE_APPEARANCE:
                scene = level.scenes.get(attributes.get("targetScene"));
                entity = scene.entities.get(attributes.get("targetEntity"));
                if(attributes.get("type") != null)
                    entity.type = Entity.Type.valueOf(attributes.get("type"));
                if(attributes.get("texture") != null)
                    entity.attributes.put("texture", attributes.get("texture"));
                if(attributes.get("numFrames") != null)
                    entity.attributes.put("numFrames", attributes.get("numFrames"));
                if(attributes.get("speed") != null)
                    entity.attributes.put("speed", attributes.get("speed"));
                if(attributes.get("time") != null)
                    entity.attributes.put("time", attributes.get("time"));
                entity.updateAppearance();
                break;
            case SAY:
                scene = level.scenes.get(attributes.get("targetScene"));
                scene.say(attributes.get("message"));
                break;
            case DIALOGUE:
                scene = level.scenes.get(attributes.get("targetScene"));
                scene.ui.addActor(Dialogue.readDialogue(attributes.get("dialogue"), level));
            case CHANGE_SCREEN:
                Main.manager.get("audio/error.wav", Sound.class).play();
                scene = level.scenes.get(attributes.get("targetScene"));
                Main.changeScreen(scene);
                break;
            case CHANGE_LEVEL:
                Level level = Level.readLevel(attributes.get("targetLevel"));
                Main.changeScreen(level.firstScene);
                break;
        }
    }

    public enum Type {
        REMOVE_ENTITY,
        ADD_ENTITY,
        REMOVE_ITEM,
        ADD_ITEM,
        REMOVE_BARRIER,
        CHANGE_APPEARANCE,
        SAY,
        DIALOGUE,
        CHANGE_SCREEN,
        CHANGE_LEVEL
    }

    public static class EffectPrototype {
        public String type;
        public Map<String, String> attributes = new HashMap<String, String>();
    }
}
