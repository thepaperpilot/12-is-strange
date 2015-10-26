package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;
import thepaperpilot.strange.Screens.GameScreen;

public class Bird extends Entity {
    public boolean alive;
    private Animation animation;
    private float time;
    private Drawable deadDrawable;

    public Bird(GameScreen game, int x, int y) {
        super(new Image(Main.manager.get("blueJayDead.png", Texture.class)).getDrawable(), game, x, y);
        deadDrawable = getDrawable();
        Texture animSheet = Main.manager.get("blueJayIdle.png", Texture.class);
        setHeight(animSheet.getHeight());
        setWidth(animSheet.getWidth() / 14);
        TextureRegion[][] tmp = TextureRegion.split(animSheet, (int) getWidth(), (int) getHeight());
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
