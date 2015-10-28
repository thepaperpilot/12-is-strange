package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;
import thepaperpilot.strange.Screens.GameScreen;

public class Bird extends Entity {
    public boolean alive;
    private Animation animation;
    private float time;

    public Bird(GameScreen game, int x, int y) {
        super(new Image(Main.entities.findRegion("blueJayDead")).getDrawable(), game, x, y);
        TextureRegion animSheet = Main.animations.findRegion("blueJayIdle");
        setHeight(animSheet.getRegionHeight());
        setWidth(animSheet.getRegionWidth() / 14);
        TextureRegion[][] tmp = animSheet.split((int) getWidth(), (int) getHeight());
        TextureRegion[] frames = tmp[0];
        animation = new Animation(1 / 6f, frames);
        time = 0;
        setPosition(x, y);
    }

    public void act(float delta) {
        if (alive) {
            time += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = new TextureRegion(animation.getKeyFrame(time, true));
            setDrawable(new TextureRegionDrawable(currentFrame));
        }
    }
}
