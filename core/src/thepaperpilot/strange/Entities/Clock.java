package thepaperpilot.strange.Entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Clock extends Image {
    TextureRegion clockSheet;
    TextureRegion[] frames;

    public Clock(int scene, int x, int y) {
        clockSheet = Main.animations.findRegion("clock");
        setWidth(clockSheet.getRegionWidth() / 12);
        setHeight(clockSheet.getRegionHeight());
        TextureRegion[][] tmp = clockSheet.split((int) getWidth(), (int) getHeight());
        frames = tmp[0];
        setTime(scene);
        setPosition(x, y);
    }

    public void setTime(int time) {
        setDrawable(new TextureRegionDrawable(frames[Math.min(11, time)]));
    }
}
