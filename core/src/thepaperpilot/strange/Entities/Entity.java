package thepaperpilot.strange.Entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.strange.Screens.GameScreen;

public class Entity extends Image {
    public Entity(final Drawable drawable, final GameScreen game, int x, int y) {
        this(game);
        setDrawable(drawable);
        setPosition(x, y);
        setSize(drawable.getMinWidth(), drawable.getMinHeight());
    }

    public Entity(final GameScreen game) {
        game.stage.addActor(this);
        this.setZIndex(1);
        addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                game.target = Entity.this;
                x = (int) Entity.this.getX();
                for (Rectangle obstacle : game.obstacles) {
                    if (x >= obstacle.x - game.max.getWidth() / 2f && game.max.x <= obstacle.x) {
                        x = obstacle.x - game.max.getWidth() / 2f - 2;
                        if (Entity.this.getX() < x)
                            game.target = null;
                    }
                    if (x <= obstacle.x + obstacle.width + game.max.getWidth() / 2f && game.max.x >= obstacle.x) {
                        x = obstacle.x + obstacle.width + game.max.getWidth() / 2f + 2;
                        if (Entity.this.getX() > x)
                            game.target = null;
                    }
                }
                game.max.target = (int) x;
                event.reset();
            }
        });
    }

    public void onTouch() {

    }
}
