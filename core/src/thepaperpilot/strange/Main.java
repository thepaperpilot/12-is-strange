package thepaperpilot.strange;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import thepaperpilot.strange.Screens.MenuScreen;

import java.util.ArrayList;

public class Main extends Game {
    public static final AssetManager manager = new AssetManager();
    public static final ArrayList<Item> inventory = new ArrayList<Item>();
    public static final ArrayList<Item> selected = new ArrayList<Item>();
    public static Skin skin;
    public static int[] decisions = new int[3];
    private static Main instance;

    public static void changeScreen(Screen screen) {
        instance.setScreen(screen);
    }

    @Override
    public void create() {
        instance = this;
        manager.load("assets/textures.json", Skin.class);
        // TODO make these a texture atlas
        manager.load("assets/schoolBackground.png", Texture.class);
        manager.load("assets/maxWalk.png", Texture.class);
        manager.load("assets/maxIdle.png", Texture.class);
        manager.load("assets/clock.png", Texture.class);
        manager.load("assets/catIdle.png", Texture.class);
        manager.load("assets/rightClickIndicator.png", Texture.class);
        manager.load("assets/alcoholInv.png", Texture.class);
        manager.load("assets/bottlesInv.png", Texture.class);
        manager.load("assets/cameraInv.png", Texture.class);
        manager.load("assets/catFoodInv.png", Texture.class);
        manager.load("assets/dogBoneInv.png", Texture.class);
        manager.load("assets/ductTapeInv.png", Texture.class);
        manager.load("assets/filesInv.png", Texture.class);
        manager.load("assets/fireExtinguishInv.png", Texture.class);
        manager.load("assets/fortuneCookieCodeInv.png", Texture.class);
        manager.load("assets/fortuneCookieInv.png", Texture.class);
        manager.load("assets/gunInv.png", Texture.class);
        manager.load("assets/hammerInv.png", Texture.class);
        manager.load("assets/keysInv.png", Texture.class);
        manager.load("assets/makeShiftBombInv.png", Texture.class);
        manager.load("assets/notebookInv.png", Texture.class);
        manager.load("assets/phoneInv.png", Texture.class);
        manager.load("assets/photoInv.png", Texture.class);
        manager.load("assets/soapInv.png", Texture.class);
        manager.load("assets/sodaInv.png", Texture.class);
        manager.load("assets/sugarInv.png", Texture.class);
        manager.load("assets/usbInv.png", Texture.class);
        manager.load("assets/weedKillerInv.png", Texture.class);
        manager.load("assets/alcoholWorld.png", Texture.class);
        manager.load("assets/bottlesWorld.png", Texture.class);
        manager.load("assets/cameraWorld.png", Texture.class);
        manager.load("assets/catFoodWorld.png", Texture.class);
        manager.load("assets/dogBoneWorld.png", Texture.class);
        manager.load("assets/ductTapeWorld.png", Texture.class);
        manager.load("assets/filesWorld.png", Texture.class);
        manager.load("assets/fireExtinguishWorld.png", Texture.class);
        manager.load("assets/fortuneCookieWorld.png", Texture.class);
        manager.load("assets/gunWorld.png", Texture.class);
        manager.load("assets/hammerWorld.png", Texture.class);
        manager.load("assets/keysWorld.png", Texture.class);
        manager.load("assets/makeShiftBombWorld.png", Texture.class);
        manager.load("assets/notebookWorld.png", Texture.class);
        manager.load("assets/phoneWorld.png", Texture.class);
        manager.load("assets/soapWorld.png", Texture.class);
        manager.load("assets/sodaWorld.png", Texture.class);
        manager.load("assets/sugarWorld.png", Texture.class);
        manager.load("assets/usbWorld.png", Texture.class);
        manager.load("assets/weedKillerWorld.png", Texture.class);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (manager.update() && getScreen() == null) {
            skin = manager.get("assets/textures.json", Skin.class);
            skin.getFont("large").getData().setScale(.5f);
            skin.getFont("font").getData().setScale(.25f);
            Item.combine(); //trick to instantiate all the items
            inventory.add(Item.NOTEBOOK);
            Scene.updateInventory();

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
