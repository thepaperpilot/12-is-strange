package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.AnimatedEntity;
import thepaperpilot.strange.Entities.Clock;
import thepaperpilot.strange.Entities.Entity;
import thepaperpilot.strange.Entities.Max;
import thepaperpilot.strange.Main;
import thepaperpilot.strange.Scene;

import java.util.Random;

public class MenuScreen implements Screen {
    private static final Random ran = new Random();
    private Image[] backgrounds;
    private Image background;
    private Stage stage;
    private Max max;
    private Clock clock;
    private float time;

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(256, 144));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        Label title = new Label("12 is Strange", Main.skin, "large");
        Button start = new TextButton("Start Game", Main.skin);
        start.addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                Main.manager.get("select.wav", Sound.class).play();
                stage.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        Main.reverse = true;
                        Main.changeScreen(Scene.FIRST.screen);
                    }
                })));
            }
        });
        Table table = new Table(Main.skin);
        table.setFillParent(true);
        table.top().add(title).padTop(40).padBottom(10).row();
        table.add(start);
        stage.addActor(table);

        clock = new Clock(ran.nextInt(12), 3 * (int) stage.getWidth() / 4, (int) stage.getHeight() / 2);
        max = new Max((int) stage.getWidth() / 4, 10);
        stage.addActor(max);
        stage.addActor(clock);
        Entity cat = new AnimatedEntity(Scene.FIRST.screen, (int) stage.getWidth() / 3, 10, Main.manager.get("catIdle.png", Texture.class), 14, 1 / 6f);
        cat.remove();
        stage.addActor(cat);

        backgrounds = new Image[]{
                new Image(Main.manager.get("schoolBackground.png", Texture.class)),
                new Image(Main.manager.get("bathroomBackground.png", Texture.class)),
                new Image(Main.manager.get("outsideBackground.png", Texture.class)),
                new Image(Main.manager.get("junkyardBackground.png", Texture.class)),
                new Image(Main.manager.get("vortexBackground.png", Texture.class)),
                //new Image(Main.manager.get("officeBackground.png", Texture.class)),
                //new Image(Main.manager.get("dormBackground.png", Texture.class))
        };
        background = backgrounds[ran.nextInt(backgrounds.length)];
        stage.addActor(background);
        background.setZIndex(0);
    }

    @Override
    public void render(float delta) {
        time += ran.nextFloat() * delta;
        if (time > 1) {
            if (ran.nextInt(5) == 0)
                background.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        background.remove();
                        Image oldBackground = background;
                        while (oldBackground == background)
                            background = backgrounds[ran.nextInt(backgrounds.length)];
                        stage.addActor(background);
                        background.setZIndex(0);
                        background.setColor(1, 1, 1, 0);
                        background.addAction(Actions.fadeIn(.5f));
                    }
                })));
            if (ran.nextInt(3) == 0)
                max.target = ran.nextInt((int) stage.getWidth());
            clock.setTime(ran.nextInt(12));
            time = 0;
        }
        ChoicesScreen.renderParticles(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
