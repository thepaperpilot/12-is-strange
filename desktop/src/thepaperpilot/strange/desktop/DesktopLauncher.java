package thepaperpilot.strange.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import thepaperpilot.strange.Main;

class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "12 is Strange v.01";
        config.width = 1280;
        config.height = 720;
        config.fullscreen = false;
        config.vSyncEnabled = true;
        new LwjglApplication(new Main(), config);
    }
}
