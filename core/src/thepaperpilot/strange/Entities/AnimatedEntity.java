package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Screens.GameScreen;

public class AnimatedEntity extends Entity {
    public Animation animation;
    private float time;

    public AnimatedEntity(GameScreen game, int x, int y, Texture animSheet, int numFrames, float speed) {
        super(game);
        setHeight(animSheet.getHeight());
        setWidth(animSheet.getWidth() / numFrames);
        TextureRegion[][] tmp = TextureRegion.split(animSheet, (int) getWidth(), (int) getHeight());
        TextureRegion[] frames = tmp[0];
        animation = new Animation(speed, frames);
        time = 0;
        setPosition(x, y);
    }

    public void act(float delta) {
        time += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = new TextureRegion(animation.getKeyFrame(time, true));
        setDrawable(new TextureRegionDrawable(currentFrame));
    }
}
