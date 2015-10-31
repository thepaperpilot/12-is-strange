package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

import java.util.HashMap;
import java.util.Map;

public class Entity extends Image {
    final Map<String, String> attributes;
    Type type;
    String name;
    String[] requiredItems;
    Effect[] successEffects;
    Effect[] failEffects;
    Effect[] doneEffects;
    float time;
    Animation animation;
    Scene scene;
    boolean locked = true;
    boolean visible;

    public Entity(EntityPrototype prototype, Scene scene) {
        name = prototype.name;
        this.scene = scene;
        type = Type.valueOf(prototype.type);
        attributes = prototype.attributes;
        requiredItems = prototype.requiredItems;
        updateAppearance();
        setPosition(prototype.x, prototype.y);

        if (prototype.visible)
            scene.stage.addActor(this);

        if (prototype.successEffects != null) {
            successEffects = new Effect[prototype.successEffects.length];
            for (int i = 0; i < prototype.successEffects.length; i++) {
                successEffects[i] = new Effect(prototype.successEffects[i], scene.level);
            }
        }

        if (prototype.failEffects != null) {
            failEffects = new Effect[prototype.failEffects.length];
            for (int i = 0; i < prototype.failEffects.length; i++) {
                failEffects[i] = new Effect(prototype.failEffects[i], scene.level);
            }
        }

        if (prototype.doneEffects != null) {
            doneEffects = new Effect[prototype.doneEffects.length];
            for (int i = 0; i < prototype.doneEffects.length; i++) {
                doneEffects[i] = new Effect(prototype.doneEffects[i], scene.level);
            }
        }

        if (prototype.successEffects == null) {
            successEffects = doneEffects;
        }
    }

    public void updateAppearance() {
        switch (type) {
            default:
            case IMAGE:
                setDrawable(new TextureRegionDrawable(Main.entities.findRegion(attributes.get("texture"))));
                setSize(getDrawable().getMinWidth(), getDrawable().getMinHeight());
                break;
            case ANIMATION:
                TextureRegion animSheet = Main.animations.findRegion(attributes.get("texture"));
                setHeight(animSheet.getRegionHeight());
                setWidth(animSheet.getRegionWidth() / Integer.parseInt(attributes.get("numFrames")));
                TextureRegion[][] tmp = animSheet.split((int) getWidth(), (int) getHeight());
                TextureRegion[] frames = tmp[0];
                animation = new Animation(Float.parseFloat(attributes.get("speed")), frames);
                break;
            case CLOCK:
                animSheet = Main.animations.findRegion(attributes.get("texture"));
                setHeight(animSheet.getRegionHeight());
                setWidth(animSheet.getRegionWidth() / Integer.parseInt(attributes.get("numFrames")));
                tmp = animSheet.split((int) getWidth(), (int) getHeight());
                frames = tmp[0];
                setDrawable(new TextureRegionDrawable(frames[Integer.parseInt(attributes.get("time")) - 1]));
                break;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void act(float delta) {
        if (type == Type.ANIMATION) {
            time += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = new TextureRegion(animation.getKeyFrame(time, true));
            setDrawable(new TextureRegionDrawable(currentFrame));
        }
    }

    public void onTouch() {
        if (requiredItems == null) {
            if (successEffects != null)
                for (Effect effect : successEffects) effect.run();
        } else if (locked) {
            boolean success = true;
            for (String item : requiredItems) {
                if (!scene.level.selected.contains(scene.level.items.get(item))) {
                    success = false;
                    break;
                }
            }
            if (success) {
                locked = false;
                if (successEffects != null) for (Effect effect : successEffects) effect.run();
            } else {
                if (failEffects != null)
                    for (Effect effect : failEffects) effect.run();
            }
        } else if (doneEffects != null) for (Effect effect : doneEffects) {
            effect.run();
        }
    }

    public enum Type {
        IMAGE,
        ANIMATION,
        CLOCK
    }

    public static class EntityPrototype {
        Map<String, String> attributes = new HashMap<String, String>();
        String name;
        String type;
        boolean visible;
        int x;
        int y;
        String[] requiredItems;
        Effect.EffectPrototype[] successEffects;
        Effect.EffectPrototype[] failEffects;
        Effect.EffectPrototype[] doneEffects;
    }
}
