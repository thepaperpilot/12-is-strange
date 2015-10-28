package thepaperpilot.strange;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import thepaperpilot.strange.Screens.MenuScreen;

import java.util.ArrayList;

public class Main extends Game {
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

    public static void changeScreen(Screen screen) {
        instance.setScreen(screen);
    }

    @Override
    public void create() {
        instance = this;
        manager.load("textures.json", Skin.class);

        manager.load("animations.atlas", TextureAtlas.class);
        manager.load("backgrounds.atlas", TextureAtlas.class);
        manager.load("entities.atlas", TextureAtlas.class);

        manager.load("audio/bgm.ogg", Music.class);
        manager.load("audio/error.wav", Sound.class);
        manager.load("audio/explosion.wav", Sound.class);
        manager.load("audio/pickup.wav", Sound.class);
        manager.load("audio/rewind.wav", Sound.class);
        manager.load("audio/select.wav", Sound.class);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (manager.update() && getScreen() == null) {
            skin = manager.get("textures.json", Skin.class);
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
        } else
            try { //This makes it so that, if it errors, it just skips the frame as opposed to stopping the entire program
                getScreen().render(Gdx.graphics.getDeltaTime());
            } catch (NullPointerException ignored) {
            } catch (final Exception e) {
                e.printStackTrace();
            }
    }
}
