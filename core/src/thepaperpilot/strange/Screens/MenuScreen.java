package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.Max;
import thepaperpilot.strange.Levels.Entity;
import thepaperpilot.strange.Levels.Level;
import thepaperpilot.strange.Main;

import java.util.Random;

public class MenuScreen implements Screen {
    private static final Random ran = new Random();
    private Image[] backgrounds;
    private Image background;
    private Stage stage;
    private Max max;
    private Entity clock;
    private float time;

    @Override
    public void show() {
        // create a stage for everything
        stage = new Stage(new StretchViewport(256, 144));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        // create a title label
        Label title = new Label("12 is Strange", Main.skin, "large");
        // create a start game button
        Button start = new TextButton("Start Game", Main.skin);
        start.addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                Main.manager.get("audio/select.wav", Sound.class).play();
                stage.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        Main.manager.get("audio/bgm.ogg", Music.class).setLooping(true);
                        Main.manager.get("audio/bgm.ogg", Music.class).play();
                        Main.reverse = true;
                        Main.changeScreen(Level.readLevel("levels/first.json").firstScene);
                    }
                })));
            }
        });
        // add ui elements to the stage
        Table table = new Table(Main.skin);
        table.setFillParent(true);
        table.top().add(title).padTop(40).padBottom(10).row();
        table.add(start);
        stage.addActor(table);

        // add a clock to the background scene
        Entity.EntityPrototype clockPrototype = new Entity.EntityPrototype(null, "CLOCK", 3 * (int) stage.getWidth() / 4, (int) stage.getHeight() / 2, false, null);
        clockPrototype.attributes.put("texture", "clock");
        clockPrototype.attributes.put("numFrames", "12");
        clockPrototype.attributes.put("time", "12");
        clock = new Entity(clockPrototype, null);
        // add an AI max to the background scene
        max = new Max((int) stage.getWidth() / 4, 10);
        stage.addActor(max);
        stage.addActor(clock);
        // add a cat to the background scene
        Entity.EntityPrototype catPrototype = new Entity.EntityPrototype(null, "ANIMATION", (int) stage.getWidth() / 3, 10, false, null);
        catPrototype.attributes.put("texture", "catIdle");
        catPrototype.attributes.put("numFrames", "14");
        catPrototype.attributes.put("speed", Float.toString(1 / 6f));
        Entity cat = new Entity(catPrototype, null);
        stage.addActor(cat);

        // make the backgrounds transition between various area backgrounds
        backgrounds = new Image[]{
                new Image(Main.backgrounds.findRegion("school")),
                new Image(Main.backgrounds.findRegion("bathroom")),
                new Image(Main.backgrounds.findRegion("outside")),
                new Image(Main.backgrounds.findRegion("junkyard")),
                new Image(Main.backgrounds.findRegion("vortex"))
        };
        background = backgrounds[ran.nextInt(backgrounds.length)];
        stage.addActor(background);
        background.setZIndex(0);
    }

    @Override
    public void render(float delta) {
        // as time goes on, make stuff happen randomly
        time += ran.nextFloat() * delta;
        if (time > 1) {
            if (ran.nextInt(5) == 0)
                // change the background every ~5 seconds
                // potentially to the same background
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
            // make max move somewhere every ~3 seconds
            // potentially to the same spot
            if (ran.nextInt(3) == 0)
                max.target = ran.nextInt((int) stage.getWidth());
            // change the time on the clock every second
            // potentially to the same time
            clock.attributes.put("time", String.valueOf(1 + ran.nextInt(12)));
            clock.updateAppearance();
            time = 0;
        }
        Main.renderParticles(delta);
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
