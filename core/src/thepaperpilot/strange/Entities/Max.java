package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import thepaperpilot.strange.Main;

public class Max implements Entity {
    private static final int WALK_SPEED = 4;
    private static final float ANIM_SPEED = .2f;

    public int x;
    public int y;
    public int target;
    public Animation walk;
    public Texture still;
    float time;

    public Max(int x, int y) {
        still = Main.manager.get("assets/maxStill.png", Texture.class);
        Texture maxWalkTexture = Main.manager.get("assets/maxWalk.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(maxWalkTexture, maxWalkTexture.getWidth() / 14, maxWalkTexture.getHeight());
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

    public void draw(Batch batch) {
        if(x < target) {
            x = Math.min(x + WALK_SPEED, target);
        } else if(x > target) {
            x = Math.max(x - WALK_SPEED, target);
        }
        if(x == target) {
            batch.draw(still, x, y, still.getWidth() * Main.pixelScaling, still.getHeight() * Main.pixelScaling);
        } else {
            time += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = walk.getKeyFrame(time, true);
            currentFrame.flip(x > target, false);
            batch.draw(currentFrame, x, y, currentFrame.getRegionWidth() * Main.pixelScaling, currentFrame.getRegionHeight() * Main.pixelScaling);
            currentFrame.flip(x > target, false);
        }
    }

    public int getWidth() {
        return still.getWidth() * Main.pixelScaling;
    }
}
