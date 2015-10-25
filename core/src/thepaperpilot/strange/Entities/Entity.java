package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.strange.Screens.GameScreen;

public class Entity extends Image {
    public Entity(Drawable drawable, final GameScreen game, int x, int y) {
        setDrawable(drawable);
        setPosition(x, y);
        setSize(drawable.getMinWidth(), drawable.getMinHeight());
        game.stage.addActor(this);
        this.setZIndex(1);
        addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                game.max.target = (int) Entity.this.getX();
                game.target = Entity.this;
                event.reset();
            }
        });
    }

    public void onTouch() {

    }
}
