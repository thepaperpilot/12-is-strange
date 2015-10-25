package thepaperpilot.strange;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
        manager.load("textures.json", Skin.class);
        manager.load("bgm.ogg", Music.class);
        manager.load("error.wav", Sound.class);
        manager.load("explosion.wav", Sound.class);
        manager.load("pickup.wav", Sound.class);
        manager.load("rewind.wav", Sound.class);
        manager.load("select.wav", Sound.class);
        // TODO make these a texture atlas
        manager.load("schoolBackground.png", Texture.class);
        manager.load("bathroomBackground.png", Texture.class);
        manager.load("outsideBackground.png", Texture.class);
        manager.load("junkyardBackground.png", Texture.class);
        manager.load("breakableDoorPuzzle.png", Texture.class);
        manager.load("explosion.png", Texture.class);
        manager.load("schoolDoor.png", Texture.class);
        manager.load("maxWalk.png", Texture.class);
        manager.load("maxIdle.png", Texture.class);
        manager.load("clock.png", Texture.class);
        manager.load("catIdle.png", Texture.class);
        manager.load("rightClickIndicator.png", Texture.class);
        manager.load("alcoholInv.png", Texture.class);
        manager.load("bottlesInv.png", Texture.class);
        manager.load("cameraInv.png", Texture.class);
        manager.load("catFoodInv.png", Texture.class);
        manager.load("dogBoneInv.png", Texture.class);
        manager.load("ductTapeInv.png", Texture.class);
        manager.load("filesInv.png", Texture.class);
        manager.load("fireExtinguishInv.png", Texture.class);
        manager.load("fortuneCookieCodeInv.png", Texture.class);
        manager.load("fortuneCookieInv.png", Texture.class);
        manager.load("gunInv.png", Texture.class);
        manager.load("hammerInv.png", Texture.class);
        manager.load("keysInv.png", Texture.class);
        manager.load("makeShiftBombInv.png", Texture.class);
        manager.load("notebookInv.png", Texture.class);
        manager.load("phoneInv.png", Texture.class);
        manager.load("photoInv.png", Texture.class);
        manager.load("soapInv.png", Texture.class);
        manager.load("sodaInv.png", Texture.class);
        manager.load("sugarInv.png", Texture.class);
        manager.load("usbInv.png", Texture.class);
        manager.load("weedKillerInv.png", Texture.class);
        manager.load("alcoholWorld.png", Texture.class);
        manager.load("bottlesWorld.png", Texture.class);
        manager.load("cameraWorld.png", Texture.class);
        manager.load("catFoodWorld.png", Texture.class);
        manager.load("dogBoneWorld.png", Texture.class);
        manager.load("ductTapeWorld.png", Texture.class);
        manager.load("filesWorld.png", Texture.class);
        manager.load("fireExtinguishWorld.png", Texture.class);
        manager.load("fortuneCookieWorld.png", Texture.class);
        manager.load("gunWorld.png", Texture.class);
        manager.load("hammerWorld.png", Texture.class);
        manager.load("keysWorld.png", Texture.class);
        manager.load("makeShiftBombWorld.png", Texture.class);
        manager.load("notebookWorld.png", Texture.class);
        manager.load("phoneWorld.png", Texture.class);
        manager.load("soapWorld.png", Texture.class);
        manager.load("sodaWorld.png", Texture.class);
        manager.load("sugarWorld.png", Texture.class);
        manager.load("usbWorld.png", Texture.class);
        manager.load("weedKillerWorld.png", Texture.class);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (manager.update() && getScreen() == null) {
            skin = manager.get("textures.json", Skin.class);
            skin.getFont("large").getData().setScale(.5f);
            skin.getFont("font").getData().setScale(.25f);
            Item.combine(); //trick to instantiate all the items
            inventory.add(Item.NOTEBOOK);
            Scene.updateInventory();
            manager.get("bgm.ogg", Music.class).setLooping(true);
            manager.get("bgm.ogg", Music.class).play();

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
