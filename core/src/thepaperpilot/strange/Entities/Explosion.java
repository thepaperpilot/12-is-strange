package thepaperpilot.strange.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Explosion extends Image {
    private static final float ANIM_SPEED = .2f;
    private final Animation explosion;
    private float time;

    public Explosion(int x, int y) {
        Texture explosionTexture = Main.manager.get("explosion.png", Texture.class);
        setHeight(explosionTexture.getHeight());
        setWidth(explosionTexture.getWidth() / 4);
        TextureRegion[][] tmp = TextureRegion.split(explosionTexture, (int) getWidth(), (int) getHeight());
        explosion = new Animation(ANIM_SPEED, tmp[0]);
        time = 0;
        setPosition(x, y);
    }

    public void act(float delta) {
        if (time > explosion.getAnimationDuration())
            remove();
        time += delta;
        TextureRegion currentFrame = new TextureRegion(explosion.getKeyFrame(time, false));
        setDrawable(new TextureRegionDrawable(currentFrame));
    }
}
