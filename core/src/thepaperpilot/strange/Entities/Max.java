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
    private final float y;
    private final Animation walk;
    private final Animation idle;
    public int target;
    public float x;
    public float time;
    public boolean shooting;

    public Max(int x, int y) {
        TextureRegion maxWalkTexture = Main.animations.findRegion("maxWalk");
        setHeight(maxWalkTexture.getRegionHeight());
        setWidth(maxWalkTexture.getRegionWidth() / 14);
        TextureRegion[][] tmp = maxWalkTexture.split((int) getWidth(), (int) getHeight());
        walk = new Animation(ANIM_SPEED, tmp[0]);
        TextureRegion maxIdleTexture = Main.animations.findRegion("maxIdle");
        tmp = maxIdleTexture.split((int) getWidth(), (int) getHeight());
        idle = new Animation(ANIM_SPEED, tmp[0]);
        TextureRegion maxShootingTexture = Main.animations.findRegion("maxShooting");
        tmp = maxShootingTexture.split((int) getWidth(), (int) getHeight());
        shoot = new Animation(ANIM_SPEED, tmp[0]);
        time = 0;
        this.x = target = x;
        this.y = y;
    }

    public void act(float delta) {
        if (shooting && time > shoot.getAnimationDuration())
            shooting = false;
        if (x < target) {
            x = Math.min(x + WALK_SPEED, target);
        } else if (x > target) {
            x = Math.max(x - WALK_SPEED, target);
        }
        time += Gdx.graphics.getDeltaTime();
        if (shooting) {
            TextureRegion currentFrame = new TextureRegion(shoot.getKeyFrame(time, false));
            setDrawable(new TextureRegionDrawable(currentFrame));
        } else if (x == target) {
            TextureRegion currentFrame = new TextureRegion(idle.getKeyFrame(time, true));
            currentFrame.flip(x > target, false);
            setDrawable(new TextureRegionDrawable(currentFrame));
        } else {
            TextureRegion currentFrame = new TextureRegion(walk.getKeyFrame(time, true));
            currentFrame.flip(x > target, false);
            setDrawable(new TextureRegionDrawable(currentFrame));
        }
        setPosition(x, y);
    }
}
