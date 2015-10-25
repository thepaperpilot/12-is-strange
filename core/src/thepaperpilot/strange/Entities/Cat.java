package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Cat extends Image {
    private static final float ANIM_SPEED = 1 / 6f;

    private Animation cat;
    private float time;

    public Cat(int x, int y) {
        Texture catSheet = Main.manager.get("catIdle.png", Texture.class);
        setHeight(catSheet.getHeight());
        setWidth(catSheet.getWidth() / 14);
        TextureRegion[][] tmp = TextureRegion.split(catSheet, (int) getWidth(), (int) getHeight());
        TextureRegion[] frames = tmp[0];
        cat = new Animation(ANIM_SPEED, frames);
        time = 0;
        setPosition(x, y);
    }

    public void act(float delta) {
        time += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = new TextureRegion(cat.getKeyFrame(time, true));
        setDrawable(new TextureRegionDrawable(currentFrame));
    }
}
