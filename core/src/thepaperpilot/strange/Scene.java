package thepaperpilot.strange;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import thepaperpilot.strange.Entities.Cat;
import thepaperpilot.strange.Screens.ChoicesScreen;
import thepaperpilot.strange.Screens.GameScreen;
import thepaperpilot.strange.Screens.MenuScreen;

public enum Scene {
    FIRST {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            if(direction == 0)
                Main.changeScreen(SECOND.screen);
            else Main.changeScreen(THIRD.screen);
        }

        public void previous() {
            // lol
        }
    },
    SECOND {
        public void init() {
            screen.stage.addActor(new Cat((int) screen.stage.getWidth() / 3, 10));
        }

        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.changeScreen(FIRST.screen);
        }
    },
    THIRD("school") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            Main.changeScreen(new ChoicesScreen(1, "Why were you in the bathroom?", new String[]{"Tell the Truth", "Hide the Truth"}, FOURTH.screen, screen));
        }

        public void previous() {
            Main.changeScreen(FIRST.screen);
        }
    },
    FOURTH {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            if(direction == 0)
                Main.changeScreen(FIFTH.screen);
            else Main.changeScreen(SIXTH.screen);
        }

        public void previous() {
            Main.changeScreen(new ChoicesScreen(1, "Why were you in the bathroom?", new String[]{"Tell the Truth", "Hide the Truth"}, FOURTH.screen, THIRD.screen));
        }
    },
    FIFTH {
        public void init() {

        }

        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.changeScreen(FIRST.screen);
        }
    },
    SIXTH("school") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            Main.changeScreen(new ChoicesScreen(2, "Who is at fault?", new String[]{"Blame David", "Blame Nathan", "Blame Jefferson"}, SEVENTH.screen, screen));
        }

        public void previous() {
            Main.changeScreen(FOURTH.screen);
        }
    },
    SEVENTH {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }
        public void next(int direction) {
            if(direction == 0)
                Main.changeScreen(EIGTH.screen);
            else Main.changeScreen(NINTH.screen);
        }

        public void previous() {
            Main.changeScreen(new ChoicesScreen(2, "Who is at fault?", new String[]{"Blame David", "Blame Nathan", "Blame Jefferson"}, SEVENTH.screen, SIXTH.screen));
        }
    },
    EIGTH {
        public void init() {

        }

        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.changeScreen(SEVENTH.screen);
        }
    },
    NINTH("school") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            Main.changeScreen(new ChoicesScreen(3, "Warn Victoria her life is in danger?", new String[]{"Warn", "Don't Warn"}, TENTH.screen, screen));
        }

        public void previous() {
            Main.changeScreen(SEVENTH.screen);
        }
    },
    TENTH {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            if(direction == 0)
                Main.changeScreen(ELEVENTH.screen);
            else Main.changeScreen(TWELTH.screen);
        }

        public void previous() {
            Main.changeScreen(new ChoicesScreen(3, "Warn Victoria her life is in danger?", new String[]{"Warn", "Don't Warn"}, TENTH.screen, NINTH.screen));
        }
    },
    ELEVENTH {
        public void init() {

        }
        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.changeScreen(TENTH.screen);
        }
    },
    TWELTH("school") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }
        public void next(int direction) {
            Main.changeScreen(FINAL.screen);
        }

        public void previous() {
            Main.changeScreen(TENTH.screen);
        }
    },
    FINAL {
        public void init() {
            // show them one of 12 end cutscenes based on their choices
        }
        public void next(int direction) {
            Main.changeScreen(new MenuScreen());
        }

        public void previous() {
            // You won. You can't go back. muahaha
        }
    };

    public GameScreen screen;

    Scene() {
        screen = new GameScreen(this, new Image(Main.manager.get("assets/schoolBackground.png", Texture.class)));
        init();
    }

    Scene(String bg) {
        screen = new GameScreen(this, new Image(Main.manager.get("assets/" + bg + "Background.png", Texture.class)));
        init();
    }

    public static void updateInventory() {
        for (Scene scene : Scene.values()) {
            scene.screen.updateInventory();
        }
    }

    public abstract void init();

    public abstract void next(int direction);

    public abstract void previous();
}
