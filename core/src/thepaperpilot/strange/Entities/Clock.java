package thepaperpilot.strange.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Clock extends Image {
    Texture clockSheet;
    TextureRegion[] frames;

    public Clock(int scene, int x, int y) {
        clockSheet = Main.manager.get("clock.png", Texture.class);
        setWidth(clockSheet.getWidth() / 12);
        setHeight(clockSheet.getHeight());
        TextureRegion[][] tmp = TextureRegion.split(clockSheet, (int) getWidth(), (int) getHeight());
        frames = tmp[0];
        setTime(scene);
        setPosition(x, y);
    }

    public void setTime(int time) {
        setDrawable(new TextureRegionDrawable(frames[Math.min(11, time)]));
    }
}
