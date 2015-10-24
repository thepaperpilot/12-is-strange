package thepaperpilot.strange;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import thepaperpilot.strange.Screens.ChoicesScreen;

public class Main extends Game {
	public static Skin skin;
	public static AssetManager manager = new AssetManager();

	public static enum decision {
		ENDING1,
		ENDING2,
		ENDING3,
		ENDING4,
		ENDING5,
		ENDING6,
		ENDING7,
		ENDING8,
		ENDING9,
		ENDING10,
		ENDING11,
		ENDING12
	}
	
	@Override
	public void create () {
		manager.load("assets/textures.json", Skin.class);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(manager.update() && getScreen() == null) {
			skin = manager.get("assets/textures.json", Skin.class);

			setScreen(new ChoicesScreen());
		}
		else try { //This makes it so that, if it errors, it just skips the frame as opposed to stopping the entire program
			getScreen().render(Gdx.graphics.getDeltaTime());
		} catch (NullPointerException ignored) {
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
