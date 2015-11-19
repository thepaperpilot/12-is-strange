package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Max extends Image {
    public static final float ANIM_SPEED = .1f;
    private static final float WALK_SPEED = .8f;
    public final Animation shoot;
    private final Animation walk;
    private final Animation idle;
    public int target;
    public float time;
    public boolean shooting;

    public Max(int x, int y) {
        // create the walking animation
        TextureRegion maxWalkTexture = Main.animations.findRegion("maxWalk");
        setHeight(maxWalkTexture.getRegionHeight());
        setWidth(maxWalkTexture.getRegionWidth() / 14);
        TextureRegion[][] tmp = maxWalkTexture.split((int) getWidth(), (int) getHeight());
        walk = new Animation(ANIM_SPEED, tmp[0]);

        // TODO markov chains for idle animations?
        // create the idle animation
        TextureRegion maxIdleTexture = Main.animations.findRegion("maxIdle");
        tmp = maxIdleTexture.split((int) getWidth(), (int) getHeight());
        idle = new Animation(ANIM_SPEED, tmp[0]);

        // create the shooting animation
        TextureRegion maxShootingTexture = Main.animations.findRegion("maxShooting");
        tmp = maxShootingTexture.split((int) getWidth(), (int) getHeight());
        shoot = new Animation(ANIM_SPEED, tmp[0]);

        // set some initial variables
        time = 0;
        setX(target = x);
        setY(y);
    }

    public void act(float delta) {
        // TODO abstract this for any animation that should only play once
        // if max is done shooting, end the animation
        if (shooting && time > shoot.getAnimationDuration())
            shooting = false;

        // if max has a target, approach it
        if (getX() < target) {
            setX(Math.min(getX() + WALK_SPEED, target));
        } else if (getX() > target) {
            setX(Math.max(getX() - WALK_SPEED, target));
        }

        // find which animation we're doing, and which frame were on
        time += Gdx.graphics.getDeltaTime();
        if (shooting) {
            TextureRegion currentFrame = new TextureRegion(shoot.getKeyFrame(time, false));
            setDrawable(new TextureRegionDrawable(currentFrame));
        } else if (getX() == target) {
            TextureRegion currentFrame = new TextureRegion(idle.getKeyFrame(time, true));
            currentFrame.flip(getX() > target, false);
            setDrawable(new TextureRegionDrawable(currentFrame));
        } else {
            TextureRegion currentFrame = new TextureRegion(walk.getKeyFrame(time, true));
            currentFrame.flip(getX() > target, false);
            setDrawable(new TextureRegionDrawable(currentFrame));
        }
    }
}
