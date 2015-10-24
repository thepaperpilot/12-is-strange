package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Max extends Image {
    private static final float WALK_SPEED = .8f;
    private static final float ANIM_SPEED = .1f;

    public float x;
    public float y;
    public int target;
    public Animation walk;
    public Drawable still;
    float time;

    public Max(int x, int y) {
        still = new TextureRegionDrawable(new TextureRegion(Main.manager.get("assets/maxStill.png", Texture.class), 16, 32));
        Texture maxWalkTexture = Main.manager.get("assets/maxWalk.png", Texture.class);
        setHeight(maxWalkTexture.getHeight());
        setWidth(maxWalkTexture.getWidth() / 14);
        TextureRegion[][] tmp = TextureRegion.split(maxWalkTexture, (int) getWidth(), (int) getHeight());
        TextureRegion[] frames = new TextureRegion[14];
        int index = 0;
        for (int i = 0; i < 14; i++) {
            frames[index++] = tmp[0][i];
        }
        walk = new Animation(ANIM_SPEED, frames);
        time = 0;
        this.x = target = x;
        this.y = y;
    }

    public void act(float delta) {
        if(x < target) {
            x = Math.min(x + WALK_SPEED, target);
        } else if(x > target) {
            x = Math.max(x - WALK_SPEED, target);
        }
        if(x == target) {
            setDrawable(still);
        } else {
            time += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = new TextureRegion(walk.getKeyFrame(time, true));
            currentFrame.flip(x > target, false);
            setDrawable(new TextureRegionDrawable(currentFrame));
        }
        setPosition(x, y);
    }
}
