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
    public final Map<String, String> attributes;
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
    private boolean loop = true;
    private boolean flip;

    public Entity(EntityPrototype prototype, Scene scene) {
        // create the entity from the prototype
        name = prototype.name;
        this.scene = scene;
        this.visible = prototype.visible;
        type = Type.valueOf(prototype.type);
        attributes = prototype.attributes;
        requiredItems = prototype.requiredItems;
        updateAppearance();
        setPosition(prototype.x, prototype.y);

        if (visible)
            // add to the stage
            scene.stage.addActor(this);

        // load the effects for when this entity's puzzle is solved (if it has one)
        if (prototype.successEffects != null) {
            successEffects = new Effect[prototype.successEffects.length];
            for (int i = 0; i < prototype.successEffects.length; i++) {
                successEffects[i] = new Effect(prototype.successEffects[i], scene.level);
            }
        }

        // load the effects for when this entity's puzzle is not solved (if it has one)
        if (prototype.failEffects != null) {
            failEffects = new Effect[prototype.failEffects.length];
            for (int i = 0; i < prototype.failEffects.length; i++) {
                failEffects[i] = new Effect(prototype.failEffects[i], scene.level);
            }
        }

        // load the effects for when this entity's puzzle has already been solved (if it has one)
        if (prototype.doneEffects != null) {
            doneEffects = new Effect[prototype.doneEffects.length];
            for (int i = 0; i < prototype.doneEffects.length; i++) {
                doneEffects[i] = new Effect(prototype.doneEffects[i], scene.level);
            }
        }

        // so the JSON file doesn't have to write the same thing twice,
        // just don't add any success effects if they should be the same
        // as the done effects. This code will copy them over
        // (actually, its better than copying. Just copies the reference)
        if (prototype.successEffects == null) {
            successEffects = doneEffects;
        }
    }

    public void updateAppearance() {
        switch (type) {
            default:
            case IMAGE:
                // change the entity's look to an image
                setDrawable(new TextureRegionDrawable(Main.entities.findRegion(attributes.get("texture"))));
                setSize(getDrawable().getMinWidth(), getDrawable().getMinHeight());
                break;
            case ANIMATION:
                // change the entity's look to an animation
                // create the animation
                TextureRegion animSheet = Main.animations.findRegion(attributes.get("texture"));
                setHeight(animSheet.getRegionHeight());
                setWidth(animSheet.getRegionWidth() / Integer.parseInt(attributes.get("numFrames")));
                TextureRegion[][] tmp = animSheet.split((int) getWidth(), (int) getHeight());
                TextureRegion[] frames = tmp[0];
                animation = new Animation(Float.parseFloat(attributes.get("speed")), frames);
                // set some animation-specific variables
                loop = true;
                if (attributes.get("loop") != null)
                    loop = Boolean.valueOf(attributes.get("loop"));
                flip = Boolean.valueOf(attributes.get("flip"));
                time = 0;
                break;
            case CLOCK:
                // change the entity's look to a clock
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
        // check if an animation
        if (type == Type.ANIMATION) {
            // find the current frame
            time += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = new TextureRegion(animation.getKeyFrame(time, loop));
            if(flip)
                currentFrame.flip(true, false);
            setDrawable(new TextureRegionDrawable(currentFrame));
            // if the animation is done and doesn't loop, remove the entity
            if (!loop && time > animation.getAnimationDuration()) {
                visible = false;
                remove();
            }
        }
        // update actions
        super.act(delta);
    }

    public void onTouch() {
        // check if this is a puzzle
        if (requiredItems == null) {
            // if its not a puzzle, run successeffects
            if (successEffects != null)
                for (Effect effect : successEffects) effect.run();
        // if it is a puzzle, see if we've already completed it
        } else if (locked) {
            // if we haven't completed it, check if we can now
            boolean success = true;
            for (String item : requiredItems) {
                if (!scene.level.selected.contains(scene.level.items.get(item))) {
                    success = false;
                    break;
                }
            }
            // if we meet the requirements, do whatever we do when we complete the puzzle
            if (success) {
                locked = false;
                if (successEffects != null) for (Effect effect : successEffects) effect.run();
            // otherwise do whatever we do when the puzzle can't be completed yet
            } else {
                if (failEffects != null)
                    for (Effect effect : failEffects) effect.run();
            }
        // do whatever we do when the puzzle has already been completed
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
        public Map<String, String> attributes = new HashMap<String, String>();
        String name;
        String type;
        boolean visible;
        int x;
        int y;
        String[] requiredItems;
        Effect.EffectPrototype[] successEffects;
        Effect.EffectPrototype[] failEffects;
        Effect.EffectPrototype[] doneEffects;

        public EntityPrototype() {

        }

        public EntityPrototype(String name, String type, int x, int y, boolean visible, String[] requiredItems) {
            this.name = name;
            this.type = type;
            this.visible = visible;
            this.x = x;
            this.y = y;
            this.requiredItems = requiredItems;
        }
    }
}
