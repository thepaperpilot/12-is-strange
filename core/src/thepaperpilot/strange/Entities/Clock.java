package thepaperpilot.strange.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Clock extends Image {
    public Clock(int ending, int x, int y) {
        Texture clockSheet = Main.manager.get("assets/clock.png", Texture.class);
        setWidth(clockSheet.getWidth() / 12);
        setHeight(clockSheet.getHeight());
        TextureRegion[][] tmp = TextureRegion.split(clockSheet, (int) getWidth(), (int) getHeight());
        setDrawable(new TextureRegionDrawable(tmp[0][ending]));
        setPosition(x, y);
    }
}
