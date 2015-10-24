package thepaperpilot.strange;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import thepaperpilot.strange.Items.Item;
import thepaperpilot.strange.Screens.ChoicesScreen;
import thepaperpilot.strange.Screens.EndingScreen;
import thepaperpilot.strange.Screens.MenuScreen;

import java.util.ArrayList;

public class Main extends Game {
    public static final AssetManager manager = new AssetManager();
    public static Skin skin;
    public static int decision1 = -1;
    public static int decision2 = -1;
    public static int decision3 = -1;
    // TODO save/load-ing
    public static final ArrayList<Item> inventory = new ArrayList<Item>();
    private static Main instance;

    public static void findEnding() {
        inventory.add(new Item("testing fonts", new Image(skin, "coin100")));
        instance.setScreen(new EndingScreen(0));
    }

    public static void changeScreen(Screen screen) {
        instance.setScreen(screen);
    }

    @Override
    public void create() {
        instance = this;
        manager.load("assets/textures.json", Skin.class);
        // TODO make these a texture atlas
        manager.load("assets/maxStill.png", Texture.class);
        manager.load("assets/maxWalk.png", Texture.class);
        manager.load("assets/clock.png", Texture.class);
        manager.load("assets/catIdle.png", Texture.class);
        manager.load("assets/rightClickIndicator.png", Texture.class);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (manager.update() && getScreen() == null) {
            skin = manager.get("assets/textures.json", Skin.class);
            skin.getFont("large").getData().setScale(.5f);
            skin.getFont("font").getData().setScale(.25f);

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
