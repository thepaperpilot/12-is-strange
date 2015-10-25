package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.Cat;
import thepaperpilot.strange.Entities.Clock;
import thepaperpilot.strange.Entities.Max;
import thepaperpilot.strange.Main;
import thepaperpilot.strange.Scene;

import java.util.Random;

public class MenuScreen implements Screen {
    private static final Random ran = new Random();
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
                Gdx.audio.newSound(new FileHandle("assets/select.wav")).play();
                Main.changeScreen(Scene.FIRST.screen);
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
        stage.addActor(new Cat((int) stage.getWidth() / 3, 10));
    }

    @Override
    public void render(float delta) {
        time += ran.nextFloat() * delta;
        if (time > 2) {
            max.target = ran.nextInt((int) stage.getWidth());
            clock.setTime(ran.nextInt(12));
            time = 0;
        }
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
