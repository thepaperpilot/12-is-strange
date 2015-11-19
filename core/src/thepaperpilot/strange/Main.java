package thepaperpilot.strange;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import thepaperpilot.strange.Screens.MenuScreen;

import java.util.ArrayList;

public class Main extends Game implements Screen {
    public static final AssetManager manager = new AssetManager();
    public static Skin skin;
    public static TextureAtlas animations;
    public static TextureAtlas backgrounds;
    public static TextureAtlas entities;
    public static boolean reverse;
    private static Main instance;
    private static SpriteBatch batch;
    private Stage loadingStage;
    private static ParticleEffect choicesParticle;

    public static void changeScreen(Screen screen) {
        if (screen == null)
            return;
        instance.setScreen(screen);
    }

    @Override
    public void create() {
        // use this so I can make a static changeScreen function
        // it basically makes Main a singleton
        instance = this;

        // load and create the choices particle effect
        choicesParticle = new ParticleEffect();
        choicesParticle.load(Gdx.files.internal("swirls.p"), Gdx.files.internal(""));
        choicesParticle.setPosition(320, 180);
        for (int i = 0; i < 10; i++) {
            choicesParticle.update(.1f);
        }

        batch = new SpriteBatch();

        // start loading all our assets
        manager.load("skin.json", Skin.class);

        manager.load("animations.atlas", TextureAtlas.class);
        manager.load("backgrounds.atlas", TextureAtlas.class);
        manager.load("entities.atlas", TextureAtlas.class);

        manager.load("audio/bgm.ogg", Music.class);
        manager.load("audio/error.wav", Sound.class);
        manager.load("audio/explosion.wav", Sound.class);
        manager.load("audio/pickup.wav", Sound.class);
        manager.load("audio/rewind.wav", Sound.class);
        manager.load("audio/select.wav", Sound.class);

        // show this screen while it loads
        setScreen(this);
    }

    @Override
    public void show() {
        // show a basic loading screen
        loadingStage = new Stage(new ExtendViewport(200, 200));

        Label loadingLabel = new Label("Loading...", new Skin(Gdx.files.internal("skin.json")));
        loadingLabel.setFillParent(true);
        loadingLabel.setAlignment(Align.center);
        loadingStage.addActor(loadingLabel);

        // basically a sanity check? loadingStage shouldn't have any input listeners
        // but I guess this'll help if the inputprocessor gets set to something it shouldn't
        Gdx.input.setInputProcessor(loadingStage);
    }

    @Override
    public void render(float delta) {
        // render the loading screen
        // act shouldn't do anything, but putting it here is good practice, I guess?
        loadingStage.act();
        loadingStage.draw();

        // continue loading. If complete, do shit
        if (manager.update()) {
            // set some stuff we need universally, now that their assets are loaded
            skin = manager.get("skin.json", Skin.class);
            animations = manager.get("animations.atlas", TextureAtlas.class);
            backgrounds = manager.get("backgrounds.atlas", TextureAtlas.class);
            entities = manager.get("entities.atlas", TextureAtlas.class);
            skin.getFont("large").getData().setScale(.5f);
            skin.getFont("font").getData().setScale(.25f);

            // go to the menu screen
            setScreen(new MenuScreen());
        }
    }

    public static void renderParticles(float delta) {
        // render the choice particles. Not actually used in the loading screen, but used everywhere else, so why not
        final Matrix4 trans = new Matrix4();
        trans.scale(Gdx.graphics.getWidth() / 640, Gdx.graphics.getHeight() / 360, 1);
        batch.setTransformMatrix(trans);
        batch.begin();
        choicesParticle.draw(batch, delta);
        batch.end();
    }

    @Override
    public void hide() {
        /// we're a good garbage collector
        loadingStage.dispose();
    }

    @Override
    public void pause() {
        // we're a passthrough!
        if (getScreen() == this) return;
        super.pause();
    }

    @Override
    public void resume() {
        // we're a passthrough!
        if (getScreen() == this) return;
        super.pause();
    }

    @Override
    public void resize(int width, int height) {
        // we're a passthrough!
        if (getScreen() == this) return;
        if (getScreen() != null) {
            getScreen().resize(width, height);
        }
    }

    @Override
    public void dispose() {
        // we're a passthrough!
        if (getScreen() != null) {
            getScreen().dispose();
        }
        // also clean up our shit
        manager.dispose();
        skin.dispose();
        batch.dispose();
    }

    @Override
    public void render() {
        // we're a passthrough!
        Gdx.gl.glClearColor(34 / 256f, 34 / 256f, 34 / 256f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getScreen().render(Gdx.graphics.getDeltaTime());
    }
}
