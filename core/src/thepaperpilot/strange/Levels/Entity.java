package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

import java.util.Map;

public class Entity extends Image {
    final Type type;
    final Map<String, String> attributes;
    String name;
    Effect[] effects;
    float time;
    Animation animation;
    Scene scene;

    public Entity(EntityPrototype prototype, Scene scene) {
        name = prototype.name;
        this.scene = scene;
        type = Type.valueOf(prototype.type);
        attributes = prototype.attributes;
        updateAppearance();
        setPosition(prototype.x, prototype.y);

        if(prototype.visible)
            scene.stage.addActor(this);

        effects = new Effect[prototype.effects.length];
        for (int i = 0; i < prototype.effects.length; i++) {
            effects[i] = new Effect(prototype.effects[i], scene.level);
        }
    }

    public void updateAppearance() {
        switch (type) {
            default:case IMAGE:
                setDrawable(new TextureRegionDrawable(Main.backgrounds.findRegion(attributes.get("texture"))));
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
                setDrawable(new TextureRegionDrawable(frames[Integer.parseInt(attributes.get("time"))]));
                break;
        }
    }

    public void act(float delta) {
        if(type == Type.ANIMATION) {
            time += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = new TextureRegion(animation.getKeyFrame(time, true));
            setDrawable(new TextureRegionDrawable(currentFrame));
        }
    }

    public void onTouch() {
        for(Effect effect : effects) {
            effect.run();
        }
    }

    public static class EntityPrototype {
        String name;
        String type;
        Map<String, String> attributes;
        boolean visible;
        int x;
        int y;
        Effect.EffectPrototype[] effects;
    }

    public enum Type {
        IMAGE,
        ANIMATION,
        CLOCK
    }
}
