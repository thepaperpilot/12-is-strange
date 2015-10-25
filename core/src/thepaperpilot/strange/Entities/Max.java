package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Max extends Image {
    private static final float WALK_SPEED = .8f;
    private static final float ANIM_SPEED = .1f;
    private final float y;
    private final Animation walk;
    private final Animation idle;
    public int target;
    public float x;
    private float time;

    public Max(int x, int y) {
        Texture maxWalkTexture = Main.manager.get("maxWalk.png", Texture.class);
        setHeight(maxWalkTexture.getHeight());
        setWidth(maxWalkTexture.getWidth() / 14);
        TextureRegion[][] tmp = TextureRegion.split(maxWalkTexture, (int) getWidth(), (int) getHeight());
        walk = new Animation(ANIM_SPEED, tmp[0]);
        Texture maxIdleTexture = Main.manager.get("maxIdle.png", Texture.class);
        tmp = TextureRegion.split(maxIdleTexture, (int) getWidth(), (int) getHeight());
        idle = new Animation(ANIM_SPEED, tmp[0]);
        time = 0;
        this.x = target = x;
        this.y = y;
    }

    public void act(float delta) {
        if (x < target) {
            x = Math.min(x + WALK_SPEED, target);
        } else if (x > target) {
            x = Math.max(x - WALK_SPEED, target);
        }
        time += Gdx.graphics.getDeltaTime();
        if (x == target) {
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
