package thepaperpilot.strange;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import thepaperpilot.strange.Screens.MenuScreen;

import java.util.ArrayList;

public class Main extends Game implements Screen {
    public static final AssetManager manager = new AssetManager();
    public static final ArrayList<Item> inventory = new ArrayList<Item>();
    public static final ArrayList<Item> selected = new ArrayList<Item>();
    public static Skin skin;
    public static TextureAtlas animations;
    public static TextureAtlas backgrounds;
    public static TextureAtlas entities;
    public static int[] decisions = new int[3];
    public static boolean reverse;
    private static Main instance;
    private Stage loadingStage;

    public static void changeScreen(Screen screen) {
        instance.setScreen(screen);
    }

    @Override
    public void create() {
        instance = this;

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

        setScreen(this);
    }

    @Override
    public void show() {
        loadingStage = new Stage(new ExtendViewport(200, 200));

        Label loadingLabel = new Label("Loading...", new Skin(Gdx.files.internal("skin.json")));
        loadingLabel.setFillParent(true);
        loadingLabel.setAlignment(Align.center);
        loadingStage.addActor(loadingLabel);

        Gdx.input.setInputProcessor(loadingStage);
    }

    @Override
    public void render(float delta) {
        loadingStage.act();
        loadingStage.draw();

        if (manager.update()) {
            skin = manager.get("skin.json", Skin.class);
            animations = manager.get("animations.atlas", TextureAtlas.class);
            backgrounds = manager.get("backgrounds.atlas", TextureAtlas.class);
            entities = manager.get("entities.atlas", TextureAtlas.class);
            skin.getFont("large").getData().setScale(.5f);
            skin.getFont("font").getData().setScale(.25f);
            Item.combine(); //trick to instantiate all the items
            inventory.add(Item.NOTEBOOK);
            Scene.updateInventory();
            manager.get("audio/bgm.ogg", Music.class).setLooping(true);
            manager.get("audio/bgm.ogg", Music.class).play();

            setScreen(new MenuScreen());
        }
    }

    @Override
    public void hide() {
        loadingStage.dispose();
    }

    @Override
    public void pause() {
        if (getScreen() == this) return;
        super.pause();
    }

    @Override
    public void resume() {
        if (getScreen() == this) return;
        super.pause();
    }

    @Override
    public void resize(int width, int height) {
        if (getScreen() == this) return;
        if (getScreen() != null) {
            getScreen().resize(width, height);
        }
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        manager.dispose();
        skin.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(34 / 256f, 34 / 256f, 34 / 256f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getScreen().render(Gdx.graphics.getDeltaTime());
    }
}
