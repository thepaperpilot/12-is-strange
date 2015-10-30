package thepaperpilot.strange.Levels;

import thepaperpilot.strange.Dialogue;
import thepaperpilot.strange.Main;

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
                break;
            case ADD_ITEM:
                item = level.items.get(attributes.get("targetItem"));
                level.inventory.add(item);
                break;
            case CHANGE_APPEARANCE:
                scene = level.scenes.get(attributes.get("targetScene"));
                entity = scene.entities.get(attributes.get("targetEntity"));
                entity.attributes.putAll(attributes);
                entity.updateAppearance();
                break;
            case SAY:
                scene = level.scenes.get(attributes.get("targetScene"));
                scene.say(attributes.get("message"));
            case DIALOGUE:
                scene = level.scenes.get(attributes.get("targetScene"));
                scene.ui.addActor(Dialogue.readDialogue(attributes.get("dialogue")));
            case CHANGE_SCREEN:
                scene = level.scenes.get(attributes.get("targetScene"));
                Main.changeScreen(scene);
                break;
            case CHANGE_LEVEL:
                Level level = Level.readLevel(attributes.get("targetLevel"));
                Main.changeScreen(level.firstScene);
                break;
        }
    }

    public static class EffectPrototype {
        public String type;
        public Map<String, String> attributes;
    }

    public enum Type {
        REMOVE_ENTITY,
        ADD_ENTITY,
        REMOVE_ITEM,
        ADD_ITEM,
        CHANGE_APPEARANCE,
        SAY,
        DIALOGUE,
        CHANGE_SCREEN,
        CHANGE_LEVEL;
    }
}
