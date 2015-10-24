package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class RightClickIndicator extends Image {
    private static final float ANIM_SPEED = .5f;

    private Animation indicator;
    private float time;

    public RightClickIndicator() {
        Texture indicatorSheet = Main.manager.get("assets/rightClickIndicator.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(indicatorSheet, indicatorSheet.getWidth() / 2, indicatorSheet.getHeight());
        TextureRegion[] frames = tmp[0];
        indicator = new Animation(ANIM_SPEED, frames);
        time = 0;
    }

    public void draw (Batch batch, float parentAlpha) {
        time += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = new TextureRegion(indicator.getKeyFrame(time, true));
        setDrawable(new TextureRegionDrawable(currentFrame));
        super.draw(batch, parentAlpha);
    }
}