package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Item;
import thepaperpilot.strange.Screens.GameScreen;

public class AnimatedPuzzle extends Puzzle {
    public Animation animation;
    private float time;

    public AnimatedPuzzle(GameScreen game, int x, int y, Texture animSheet, int numFrames, float speed, Item[] keys) {
        super(game, keys);
        setAnimation(animSheet, numFrames, speed);
        time = 0;
        setPosition(x, y);
    }

    public AnimatedPuzzle(GameScreen game, int x, int y, Texture animSheet, int numFrames, float speed, Item key) {
        this(game, x, y, animSheet, numFrames, speed, new Item[]{key});
    }

    public void act(float delta) {
        time += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = new TextureRegion(animation.getKeyFrame(time, true));
        setDrawable(new TextureRegionDrawable(currentFrame));
    }

    public void setAnimation(Texture animSheet, int numFrames, float speed) {
        setHeight(animSheet.getHeight());
        setWidth(animSheet.getWidth() / numFrames);
        TextureRegion[][] tmp = TextureRegion.split(animSheet, (int) getWidth(), (int) getHeight());
        TextureRegion[] frames = tmp[0];
        animation = new Animation(speed, frames);
    }
}
